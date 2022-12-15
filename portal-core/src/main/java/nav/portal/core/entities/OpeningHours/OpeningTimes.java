package nav.portal.core.entities.OpeningHours;

import net.sourceforge.jtds.jdbc.DateTime;

import java.text.DateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.text.SimpleDateFormat;

public class OpeningTimes {
    private String[] rules;

    //Kode som aksepterer oppføring av åpningstider regel
    //Returnerer en setning som sier om åpningstidsregelen er gyldig eller ikke
    public Boolean isAValidRule(String openingTimeRule){
        if( isAValidCheck(openingTimeRule)) {
            System.out.println("Gyldig regel: " + openingTimeRule);
            return true;
        }else{
            System.out.println("Ugyldig regel: " + openingTimeRule);
            return false;
        }
    }

    private Boolean isAValidCheck(String openingTimeRule){
        if (isAValidRuleLength(openingTimeRule)) {
            createRules(openingTimeRule);
            if (isValidTimes(rules)) {
                return isAValidDayDateOrPeriod(rules);
            }
        }
        return false;
    }

    private Boolean isAValidRuleLength(String openingTimeRule){
        String[] ruleParts = openingTimeRule.split("[\s]");
        return (ruleParts.length == 4);
    }

    private void createRules(String openingTimeRule){
        String[] ruleParts = openingTimeRule.split("[\s]");
        rules = new String [ruleParts.length];
        rules[0] = ruleParts[0];
        rules[1] = ruleParts[1];
        rules[2] = ruleParts[2];
        rules[3] = ruleParts[3];
    }

    private Boolean isValidTimes(String[] rules){
        if (isAValidTimesFormat(rules)) {
            int from = toMins(rules[1].substring(0, 5));
            int to = toMins(rules[1].substring(6, 11));
            return from < to;
        }else {
            return false;
        }
    }

    private Boolean isAValidTimesFormat(String[] rules){
        return (rules[1].trim().matches("([01]\\d:[0-5][0-9]|2[0-3]:[0-5][0-9])-" +
                "([01]\\d:[0-5][0-9]|2[0-3]:[0-5][0-9])"));
    }

    private static int toMins(String s) {
        String[] hourMin = s.split(":");
        int hour = Integer.parseInt(hourMin[0]);
        int mins = Integer.parseInt(hourMin[1]);
        int hoursInMins = hour * 60;
        return hoursInMins + mins;
    }

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

    //Test for dato med format dd.mm.yyyy, dekker også skuddår
    private boolean isAValidFormatForASpecifiedDate(String[] rules) {
        return (rules[0].matches("(((0[1-9]|[12][0-9]|3[01])([.])(0[13578]|10|12)([.])(\\d{4}))" +
            "|(([0][1-9]|[12][0-9]|30)([.])(0[469]|11)([.])(\\d{4}))|((0[1-9]|1[0-9]|2[0-8])([.])(02)([.])(\\d{4}))" +
            "|((29)(02)([.])([02468][048]00))|((29)([.])(02)([.])([13579][26]00))|((29)([.])(02)([.])([0-9][0-9][0][48]))" +
            "|((29)([.])(02)([.])([0-9][0-9][2468][048]))|((29)([.])(02)([.])([0-9][0-9][13579][26])))"));
    }

    private Boolean isValidFormatForWeekday (String[] rules) {
        if (rules[2].substring(0).matches("[1-7]\s*:\s*[1-7]")) {
            int range1 = Integer.parseInt(rules[2].substring(0,1));
            int range2 = Integer.parseInt(rules[2].substring(2));
            return range1 <= range2;
        }else{
            return false;
        }
    }

    private boolean isAValidDayInTheMonthFormat(String [] rules){
        return (rules[3].trim().matches("([1-9]|[12][0-9]|3[01])|-1"));
    }


}






