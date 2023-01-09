package nav.portal.core.entities.OpeningHours;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.*;


//Navs åpningstidskalender er bygget på regler som består av enten en dato,
// ukedager eller dag i måneden med tilsvarende åpnings- og sluttid.
// Koden kontrollerer gyldigheten av de ulike datoene og tidspunktene.

public class OpeningTimes {

    private final List<String> openingTimesRules;

    //Kode som aksepterer oppføring av åpningstider regel
    //Den skriver ut om åpningstidsregelen er gyldig eller ikke
    public OpeningTimes() {
        this.openingTimesRules = new ArrayList<>();
    }

    public List<String> getOpeningTimesSet() {
        return openingTimesRules;
    }

    public Boolean isAValidRule(String openingTimeRule) {
        if (isAValidCheck(openingTimeRule)) {
            System.out.println("Gyldig regel: " + openingTimeRule);
            return openingTimesRules.add(openingTimeRule);
        } else {
            System.out.println("Ugyldig regel: " + openingTimeRule);
            return false;
        }
    }

    //Oppretter en tabell for hver åpningstidsregler, og ber om tilbakemelding
    // på om åpningstidene har riktig format og om det er en gyldig ukedag,
    // dager i uke eller dag i måned. False, som indikerer en ugyldig regel
    // er retur hvis disse betingelsene ikke er oppfylt.
    private Boolean isAValidCheck(String openingTimeRule) {
        if (isAValidRuleLength(openingTimeRule)) {
            String[] rules = createRules(openingTimeRule);
            if (isValidTimes(rules)) {
                return isAValidDayDateOrPeriod(rules);
            }
        }
        return false;
    }

    //Sjekker om åpningstidsregelen som består av fire deler er riktig,
    // ellers returnerer falsk;
    private Boolean isAValidRuleLength(String openingTimeRule) {
        String[] ruleParts = openingTimeRule.split("[\s]");
        return (ruleParts.length == 4);
    }

    //Oppretter en tabell som består av de fire delene av regelen:
    // del 1: en dato, del 2: åpningstider, del 3: Ukedager del 4: dag i måneden
    private static String[] createRules(String openingTimeRule) {
        return openingTimeRule.split("[\s]");
    }

    //Validerer for korrekt åpningstid format: tt:mm-tt:mm og riktig start- og sluttid,
    // og starttidspunktet for åpningen er mindre enn slutttiden ellers returnerer falskt.
    private Boolean isValidTimes(String[] rules) {
        if (isAValidTimesFormat(rules)) {
            return fromLessThanToTimes(rules) < 0;
        } else {
            return false;
        }
    }

    //Validerer for korrekt åpningstid format: tt:mm-tt:mm og riktig start- og sluttid,
    // ellers returnerer falskt.
    private static int fromLessThanToTimes(String[] rules) {
        LocalDate date = LocalDate.now();
        LocalTime timeFrom = LocalTime.of(Integer.parseInt(rules[1].substring(0, 2)), Integer.parseInt(rules[1].substring(3, 5)));
        LocalTime timeTo = LocalTime.of(Integer.parseInt(rules[1].substring(6, 8)), Integer.parseInt(rules[1].substring(9, 11)));
        LocalDateTime localDateTimeFrom = LocalDateTime.of(date, timeFrom);
        LocalDateTime localDateTimeTo = LocalDateTime.of(date, timeTo);
        return localDateTimeFrom.compareTo(localDateTimeTo);
    }

    //Tester for riktig åpningstidsformat: tt:mm-tt:mm ellers returnerer falskt.
    private Boolean isAValidTimesFormat(String[] rules) {
        return (rules[1].trim().matches("([01]\\d:[0-5][0-9]|2[0-3]:[0-5][0-9])-" +
                "([01]\\d:[0-5][0-9]|2[0-3]:[0-5][0-9])"));
    }

    //Sjekker at de ulike delene inneholder en annen verdi enn et spørsmålstegn,
    // del 1 indikerer en dato, del 3 ukedager og del 4 dager i måneden, ellers returnerer falsk.
    private Boolean isAValidDayDateOrPeriod(String[] rules) {
        if (!rules[0].startsWith("??")) {
            return isAValidFormatForASpecifiedDate(rules);
        } else if (!rules[2].matches("\s*[?*]\s*")) {
            return isValidFormatForWeekday(rules);
        } else if (!rules[3].equals("?")) {
            return isAValidDayInTheMonthFormat(rules);
        } else {
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
    private Boolean isValidFormatForWeekday(String[] rules) {
        if (rules[2].matches("[1-7]\s*:\s*[1-7]")) {
            int range1 = Integer.parseInt(rules[2].substring(0, 1));
            int range2 = Integer.parseInt(rules[2].substring(2));
            return range1 <= range2;
        } else {
            return false;
        }
    }

    //Tester gyldigheten for dager i en måned, og inkluderer -1 to -10 som representerer den siste
    // dagen og 10 dager fra siste dagen i en måned, ellers returnerer falsk hvis betingelsen ikke er oppfylt
    private boolean isAValidDayInTheMonthFormat(String[] rules) {
        return ((Integer.parseInt(rules[3].trim()) >= 1 && Integer.parseInt(rules[3].trim()) <= 31) ||
                (Integer.parseInt(rules[3].trim()) <= -1 && Integer.parseInt(rules[3].trim()) >= -10));
    }

    //Formaterer datainnføringsdatoen før du sjekker om det er samsvar for en bestemt dato i åpningstidsarrayList.
    public boolean isASpecificDate(String dataEntry) {
        int dateFoundAtPosition = isAValidSpecificDate(formatDateToString(dataEntry));
        if (dateFoundAtPosition > -1){
            return isAValidOpeningTime(dateFoundAtPosition);
        }
        return false;
    }

    //Formaterer dato @Param dataEntry-formatet fra LocalDate til String
    private String formatDateToString(String dataEntry) {
        LocalDate formattedDataEntry = getFormattedDate(dataEntry);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formattedDataEntry.format(formatter);
    }


    /*Inspiserer @param date for en verdi, bruker gjeldende dato når verdien til strengen er tom,
     ellers formaterer den spesifiserte datoen til åååå-mm-dd før månedsdagen returneres som et heltall.
     */
    private LocalDate getFormattedDate(String date){
        LocalDate formattedDate;
        if (date.isEmpty()){
            return LocalDate.now();
        }else{
            return LocalDate.parse(formatOpeningDate(date));
        }
    }

    /*Inspiserer hver spesifisert datoregel, regel[0] returnerer posisjonen i matrisen hvis
    den finnes, eller en negativ verdi hvis ikke
     */
    private int isAValidSpecificDate(String dateEntry) {
        for (int i = 0; i < openingTimesRules.size(); i++) {
            String[] rule = createRules(openingTimesRules.get(i));
            if (!rule[0].startsWith("??")) {
                if (formatOpeningDate(rule[0]).equals(dateEntry)) {
                    System.out.println("is True for isAValidSpecifiedDate");
                        return i;
                }
            }
        }
        return -1;
    }

    /*Bruker posisjonen til regelen i matrisen @ for å finne åpningstidene, regel[1]
     returnerer verdien sann hvis den blir funnet; usant ellers
     */
    private boolean isAValidOpeningTime(int i){
        String[] rule = createRules(openingTimesRules.get(i));
        if (LocalTime.now().isBefore(LocalTime.parse(rule[1].substring(6)))&&
                LocalTime.now().isAfter(LocalTime.parse(rule[1].substring(0,5)))){
                System.out.println("is True for isAValidOpeningTime");
                return true;
        }
        return false;
    }


    //Formaterer en dato oppføring fra dd.mm.åååå og returnerer åååå-mm-dd fra localDate.
    private String formatLocalDate() {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(formatter);
    }

    //Formaterer en dato oppføring fra dd.mm.åååå og returnerer åååå-mm-dd.
    private static String formatOpeningDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(LocalDate.parse(date, formatter).format(formatter2));
        return LocalDate.parse(date, formatter).format(formatter2);
    }

    /*Formaterer dataene fra @Param-datoen før dagen sammenlignes med dagen for regel 3
     i åpningstidsreglene og returnerer sann hvis en gyldig åpningstid; ellers falsk.
     */
    public boolean isAValidOTForDayInMonth(String date){
        LocalDate formattedDate = getFormattedDate(date);
        int dayInMonthAtPositionInRulesList = isAValidDayInMonth(formattedDate.getDayOfMonth());
        if (dayInMonthAtPositionInRulesList != -1){
            return isAValidOpeningTime(dayInMonthAtPositionInRulesList);
        }
        return false;
    }

   /*Inspiserer hver dag i måned-regel, @param DateEntry for en match mot den forespurte
    dagen og returnerer verdien av regelposisjonen i arrayList,
    ellers returnerer -1 som indikerer ingen match.
    */
    private int isAValidDayInMonth(int DateEntry){
        for(int i = 0; i < openingTimesRules.size(); i++){
            String[] rule = createRules(openingTimesRules.get(i));
            if (!rule[3].startsWith("?")){
                String day = Integer.toString(DateEntry);
                String rule3 = rule[3];
                if (dayNumberFromRule(rule3).equals(day)){
                    System.out.println("return i:  " + i);
                    return i;
                }
            }
        }
        return -1;
    }

    /*Sjekker parameterverdiregel 3 for en positiv verdi som representerer
    en dag fra begynnelsen av måneden eller en negativ verdi som representerer dagen fra
    slutten av måneden før dagen representert som en streng.
     */
    private String dayNumberFromRule(String rule3){
        int intOfRule3 = Integer.parseInt(rule3.trim());
        if (intOfRule3 < 0){
            return dateFromLastDayInMonth(rule3);
        }else{
            return rule3;
        }
    }

   /*Beregner fra siste dag i måneden hvis datainntasting er en negativ verdi.
   @param regel3 representerer en dag fra slutten av en måned opp til -10 dager
    */
   private String dateFromLastDayInMonth(String rule3){
       int dayNumbersBack = Integer.parseInt(rule3);
       Calendar cal = Calendar.getInstance();
       cal.add(Calendar.MONTH, 1);
       cal.set(Calendar.DATE, 1);
       cal.add(Calendar.DATE, dayNumbersBack);
       int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
       System.out.println("day with substring: " + dayOfMonth);
       return String.valueOf(dayOfMonth);
   }

    public boolean isAValidOTForWeekday(String date){
       for (int i = 0; i < openingTimesRules.size(); i++) {
           String[] rule = createRules(openingTimesRules.get(i));
           if (!rule[2].startsWith("?")){
               int startOfRange = Integer.parseInt(rule[2].substring(0,1));
               int endOfRange = Integer.parseInt(rule[2].substring(2));
               return (((weekDayNumber(date) >= startOfRange) &&
                  (weekDayNumber(date) <= endOfRange))&&
                       isAValidOpeningTime(i));

           }
       }
       return false;
    }

    public int weekDayNumberLocalDate(){
        LocalDate localDate = LocalDate.now();
        DayOfWeek dayOfWeek = DayOfWeek.from(localDate);
        System.out.println("Day of the Week on "
                + localDate + " - "
                + dayOfWeek.name());
        int weekDayNumber = dayOfWeek.get(ChronoField.DAY_OF_WEEK); //day number
        System.out.println("Int Value of "
                + dayOfWeek.name()
                + " - " + weekDayNumber);
        return weekDayNumber;
    }

    public int weekDayNumber(String date) {
        String dateInString = formatOpeningDate(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate specifiedDate = LocalDate.parse(dateInString, formatter);
        DayOfWeek dayOfWeek = DayOfWeek.from(specifiedDate);
        System.out.println("Day of the Week on "
                + specifiedDate + " - "
                + dayOfWeek.name());
        int weekDayNumber = dayOfWeek.get(ChronoField.DAY_OF_WEEK); //day number
        System.out.println("Int Value of "
                + dayOfWeek.name()
                + " - " + weekDayNumber);
        return weekDayNumber;
    }

    /**
     * Metode beregner siste arbeidsdag for siste dag i måneden som input
     * @param lastDayOfMonth
     * @return LocalDate-forekomst som inneholder siste arbeidsdag
     */
    public static LocalDate getLastWorkingDayOfMonth(LocalDate lastDayOfMonth) {
        LocalDate lastWorkingDayofMonth = switch (DayOfWeek.of(lastDayOfMonth.get(ChronoField.DAY_OF_WEEK))) {
            case SATURDAY -> lastDayOfMonth.minusDays(1);
            case SUNDAY -> lastDayOfMonth.minusDays(2);
            default -> lastDayOfMonth;
        };
        return lastWorkingDayofMonth;
    }
}






