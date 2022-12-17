package nav.portal.core.entities.OpeningHours;

import net.sourceforge.jtds.jdbc.DateTime;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.text.SimpleDateFormat;

//Navs åpningstidskalender er bygget på regler som består av enten en dato,
// ukedager eller dag i måneden med tilsvarende åpnings- og sluttid.
// Koden kontrollerer gyldigheten av de ulike datoene og tidspunktene.

public class OpeningTimes {

    //Kode som aksepterer oppføring av åpningstider regel
    //Den skriver ut om åpningstidsregelen er gyldig eller ikke
    public Boolean isAValidRule(String openingTimeRule){
        if( isAValidCheck(openingTimeRule)) {
            System.out.println("Gyldig regel: " + openingTimeRule);
            return true;
        }else{
            System.out.println("Ugyldig regel: " + openingTimeRule);
            return false;
        }
    }

    //Oppretter en tabell for hver åpningstidsregler, og ber om tilbakemelding
    // på om åpningstidene har riktig format og om det er en gyldig ukedag,
    // dager i uke eller dag i måned. False, som indikerer en ugyldig regel
    // er retur hvis disse betingelsene ikke er oppfylt.
    private Boolean isAValidCheck(String openingTimeRule){
        if (isAValidRuleLength(openingTimeRule)) {
            String [] rules = createRules(openingTimeRule);
            if (isValidTimes(rules)) {
                return isAValidDayDateOrPeriod(rules);
            }
        }
        return false;
    }

    //Sjekker om åpningstidsregelen som består av fire deler er riktig,
    // ellers returnerer falsk;
    private Boolean isAValidRuleLength(String openingTimeRule){
        String[] ruleParts = openingTimeRule.split("[\s]");
        return (ruleParts.length == 4);
    }

    //Oppretter en tabell som består av de fire delene av regelen:
    // del 1: en dato, del 2: åpningstider, del 3: Ukedager del 4: dag i måneden
    private static String [] createRules(String openingTimeRule){
        return openingTimeRule.split("[\s]");
    }

    //Validerer for korrekt åpningstid format: tt:mm-tt:mm og riktig start- og sluttid,
    // ellers returnerer falskt.
    private Boolean isValidTimes(String[] rules){
        if (isAValidTimesFormat(rules)) {
            int from = toMins(rules[1].substring(0, 5));
            int to = toMins(rules[1].substring(6, 11));
            return from < to;
        }else {
            return false;
        }
    }

    //Tester for riktig åpningstidsformat: tt:mm-tt:mm ellers returnerer falskt.
    private Boolean isAValidTimesFormat(String[] rules){
        return (rules[1].trim().matches("([01]\\d:[0-5][0-9]|2[0-3]:[0-5][0-9])-" +
                "([01]\\d:[0-5][0-9]|2[0-3]:[0-5][0-9])"));
    }


    //Tester at starttidspunktet for åpningen er mindre enn slutttiden og returnerer falsk ellers.
     private static int toMins(String s) {
        String[] hourMin = s.split(":");
        int hour = Integer.parseInt(hourMin[0]);
        int mins = Integer.parseInt(hourMin[1]);
        int hoursInMins = hour * 60;
        return hoursInMins + mins;
    }

    //Sjekker at de ulike delene inneholder en annen verdi enn et spørsmålstegn,
    // del 1 indikerer en dato, del 3 ukedager og del 4 dager i måneden, ellers returnerer falsk.
    private Boolean isAValidDayDateOrPeriod(String[] rules){
        if (!rules[0].substring(0,2).equals("??")){
            return  isAValidFormatForASpecifiedDate(rules);
        }else if (!rules[2].matches("\s*[?*]\s*")){
            return isValidFormatForWeekday(rules);
        }else if (!rules[3].substring(0).equals("?")){
            return isAValidDayInTheMonthFormat(rules);
        }else{
            return false;
        }
    }

    //Tests for a valid date format of dd.mm.yyyy also covering also leap years; otherwise return false
    private boolean isAValidFormatForASpecifiedDate(String[] rules) {
        return (rules[0].matches("(((0[1-9]|[12][0-9]|3[01])([.])(0[13578]|10|12)([.])(\\d{4}))" +
            "|(([0][1-9]|[12][0-9]|30)([.])(0[469]|11)([.])(\\d{4}))|((0[1-9]|1[0-9]|2[0-8])([.])(02)([.])(\\d{4}))" +
            "|((29)(02)([.])([02468][048]00))|((29)([.])(02)([.])([13579][26]00))|((29)([.])(02)([.])([0-9][0-9][0][48]))" +
            "|((29)([.])(02)([.])([0-9][0-9][2468][048]))|((29)([.])(02)([.])([0-9][0-9][13579][26])))"));
    }


    //Tester gyldigheten for ukedagsregler formatet d:d der den representerer dagene i en uke i et område
    // (enhver variasjon fra mandag til og med søndag; ellers returneres falskt
    private Boolean isValidFormatForWeekday (String[] rules) {
        if (rules[2].substring(0).matches("[1-7]\s*:\s*[1-7]")) {
            int range1 = Integer.parseInt(rules[2].substring(0,1));
            int range2 = Integer.parseInt(rules[2].substring(2));
            return range1 <= range2;
        }else{
            return false;
        }
    }

    //Tester gyldigheten for dager i en måned, og inkluderer -1 to -10 som representerer den siste
    // dagen og 10 dager fra siste dagen i en måned, ellers returnerer falsk hvis betingelsen ikke er oppfylt
    private boolean isAValidDayInTheMonthFormat(String [] rules){
        return ((Integer.parseInt(rules[3].trim()) >= 1 && Integer.parseInt(rules[3].trim()) <= 31)||
        (Integer.parseInt(rules[3].trim()) <= -1 && Integer.parseInt(rules[3].trim()) >= -10));
    }
}






