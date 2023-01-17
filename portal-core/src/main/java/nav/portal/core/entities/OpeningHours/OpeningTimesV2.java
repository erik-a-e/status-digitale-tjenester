package nav.portal.core.entities.OpeningHours;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;

public class OpeningTimesV2 {


    public static boolean isAValidRule(String rule) {
        //System.out.println("Rule: " + rule);
        String[] ruleParts = rule.split("[\s]");
        if (ruleParts.length != 4) {
           System.out.println("Invalid rule parts length : " + ruleParts.length);
           return false;
        }
        //Check if valid date format
       if (!isValidDateFormat(ruleParts[0])) {
            System.out.println("Invalid date format: " + ruleParts[0]);
            return false;
        }
        //Check if weekday range is correct
        if (!isValidDayInMonthFormat(ruleParts[1])) {
            System.out.println("Invalid day in month format: " + ruleParts[1]);
            return false;
        }

        //Check if weekday range is correct
        if (!isValidWeekdayFormat(ruleParts[2])) {
            System.out.println("Invalid weekday format: " + ruleParts[2]);
            return false;
        }

        //Check if time interval is correct
           //return isValidTimeFormat(ruleParts[3]);
        if (isValidTimeFormat(ruleParts[3])){
            System.out.println("Valid rule: " + rule);
            return true;
        }else{
            System.out.println("Invalid time rule: " + ruleParts[3]);
            return false;
        }
    }

    private static boolean isValidDateFormat(String dateRule) {
        //Checks for ??.??.????
        if (dateRule.equals("??.??.????")) {
            return true;
        }
        String[] ruleParts = dateRule.split("[.]");
        if (ruleParts.length != 3) {
            return false;
        }
        //Checks formats ??.mm.???? and dd.mm.????
        if (ruleParts[2].equals("????")) {
            if (ruleParts[0].equals("??")){
                return ruleParts[1].matches("^(0?[1-9]|[1][1-2])$");
            }
            //Checks for dd.mm.????
            String ddmmRule = dateRule.substring(0,5);
            return ddmmRule.matches("^(0?[1-9]|[12][0-9]|3[01]).(0?[1-9]|1[012])$");
        }

        //Checks for dd.mm.yyyy
        return dateRule.matches("(((0[1-9]|[12][0-9]|3[01])([.])(0[13578]|10|12)([.])(\\d{4}))" +
                "|(([0][1-9]|[12][0-9]|30)([.])(0[469]|11)([.])(\\d{4}))|((0[1-9]|1[0-9]|2[0-8])([.])(02)([.])(\\d{4}))" +
                "|((29)(02)([.])([02468][048]00))|((29)([.])(02)([.])([13579][26]00))|((29)([.])(02)([.])([0-9][0-9][0][48]))" +
                "|((29)([.])(02)([.])([0-9][0-9][2468][048]))|((29)([.])(02)([.])([0-9][0-9][13579][26])))");
    }

    private static boolean isValidDayInMonthFormat(String dayInMonthRule) {
        //Checks for ?
        if (dayInMonthRule.equals("?")) {
            return true;
        }

        //Checks for a singular day in the month or for last day in the month(L) entry
        String[] ruleParts = dayInMonthRule.split("[,-]");
        if (ruleParts.length == 1){
            return (dayInMonthRule.substring(0).matches("^([0-2]?[0-9]$|[3]?[0-1])$|[L]$"));
        }

        boolean ruleContainsL = false;                 //checks for the value L in rule initialise to false
        int LPosition = -1;                            //Position of L in the range of numbers initialise to 1
        if (dayInMonthRule.contains("L")) {           //checks for the value L in rule
            ruleContainsL = true;                     //true when found
            LPosition = dayInMonthRule.indexOf("L");  //Position of L in the range of numbers
        }

        //String[] ruleParts = dayInMonthRule.split("[,-]");
        //Checks for the letter L indicating the last day in a month is at end of
        // the range of days by matching the position and adding 1 in order to match the length;
        //returning false if it is not the case
        if (ruleContainsL && (dayInMonthRule.length() != (LPosition + 1))) {
            return false;
        }

        /*Checks for - indicating a range of values, where 2-5, which is equivalent to 2,3,4,5
        and or "," specifying a list of values **/
        ruleParts = dayInMonthRule.split("[,-]");
        int length;
        if (ruleParts.length > 0) {
            if(ruleContainsL){//if L found, remove exclude from the length
                length = ruleParts.length - 1;
            }else{       //use of the original length
                length = ruleParts.length;
            }
            //Checks for a range of days in the month*/
            int lowerRange = Integer.parseInt(ruleParts[0]);
            for (int i = 1; i < length; i++) {
                int upperRange = Integer.parseInt(ruleParts[i]);
                //Checks a a range of values consecutively increasing
                if (!ruleParts[i].matches("^([0-2]?[0-9]$|[3]?[0-1])$") || !(lowerRange <= upperRange)) {
                    return false;
                }
                lowerRange = upperRange;
            }
            return true;
        }
        System.out.println("dayInMonthRule : " + dayInMonthRule);
        return true;
    }

    private static boolean isValidWeekdayFormat(String weekDayRule) {
        //Checks for ?
        if (weekDayRule.equals("?")) {
            return true;
        }
        //Checks for a singular weekday from 1 - Monday  7 - Sunday*/
        if (weekDayRule.matches("[1-7]")) {
            return true;
        }

         /*Checks for - indicating a range of values, where 2-5, which is equivalent to 2,3,4,5
        and or "," specifying a list of values **/
        String[] ruleParts = weekDayRule.split("[,-]");
        if (ruleParts.length > 0) {
            //Checks for a range of weekdays from 1 - Monday  7 - Sunday*/
            int lowerRange = Integer.parseInt(ruleParts[0]);
            for (int i = 1; i <  ruleParts.length; i++) {
                int upperRange = Integer.parseInt(ruleParts[i]);
                //if not a valid weekday number or range of values consecutively increasing
                if (!ruleParts[i].matches("[1-7]") || !(lowerRange <= upperRange)) {
                    return false;
                }
                lowerRange = upperRange;
            }
            return true;
        }
        return false;
    }

    private static boolean isValidTimeFormat(String timeRule) {
        //checks for hh:mm-hh:mm
        if (!timeRule.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):([0-9]|[0-5][0-9])-([0-9]|0[0-9]|1[0-9]|2[0-3]):([0-9]|[0-5][0-9])$")){
            System.out.println("Illegal time format, should be hh:mm-hh:mm");
            return false;
        }

        String[] ruleParts = timeRule.split("[-]");
        if (ruleParts.length != 2) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date midnight = sdf.parse("00:00");
            Date opening = sdf.parse(ruleParts[0]);
            Date closing = sdf.parse(ruleParts[1]);
            if (opening.equals(midnight) && closing.equals(midnight)) {
                return true;
            }
            if (closing.before(opening)) {
                return false;
            }
        } catch (ParseException e) {
            System.out.println("Illegal time format, should be hh:mm-hh:mm");
            return false;
        }
        return true;
    }

    public static boolean isOpen(String dateEntry, String rule) {
        if(!isValidEntryDateFormat(dateEntry)){
            return false;
        }
        String date;
        if (dateEntry.isEmpty()){
            LocalDate localDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            date =  localDate.format(formatter);
        }else{
            date = dateEntry;
        }
        String[] ruleParts = rule.split("[\s]");

        if (!isValidForDate(date, ruleParts[0])){
            return false;
        }

        if(!isValidForDaysInMonth(date, ruleParts[1])){
            return false;
        }

        if(!isValidForWeekDays(date, ruleParts[2])){
            return false;
        }


        return true;

    }

    public static boolean isValidEntryDateFormat(String dateEntry) {
        //The current date is taken into use if the  date entry is spaces
        if (dateEntry.isEmpty()){
            return true;
        }

        //Checks the date entry has the format dd.mm.yyyy
        //returning false if not
        return dateEntry.matches("(((0[1-9]|[12][0-9]|3[01])([.])(0[13578]|10|12)([.])(\\d{4}))" +
                "|(([0][1-9]|[12][0-9]|30)([.])(0[469]|11)([.])(\\d{4}))|((0[1-9]|1[0-9]|2[0-8])([.])(02)([.])(\\d{4}))" +
                "|((29)(02)([.])([02468][048]00))|((29)([.])(02)([.])([13579][26]00))|((29)([.])(02)([.])([0-9][0-9][0][48]))" +
                "|((29)([.])(02)([.])([0-9][0-9][2468][048]))|((29)([.])(02)([.])([0-9][0-9][13579][26])))");
    }

    private static boolean isValidForDate(String dateEntry, String dateRule) {
        //Checks for ??.??.????
        if (dateRule.equals("??.??.????")) {
            return true;
        }

        String[] ruleParts = dateRule.split("[.]"); //Rule's date
        String[] dateEntryParts = dateEntry.split("[.]"); //entry date
        //Checks formats ??.mm.???? and dd.mm.????
        if (ruleParts[2].equals("????")) {
            if (ruleParts[0].equals("??")) {
                //Checks the date entry and rule date for a match of the month
                return ruleParts[1].equals(dateEntryParts[0]);
            }
            //Checks the date entry and rule date for a dd.mm match
            String ddmmRule = dateRule.substring(0,5);
            String ddmmdateEntry = dateEntry.substring(0,5);
            return ddmmRule.equals(ddmmdateEntry);
        }

        //Checks the date entry and rule date for a match of the dd.mm.yyyy
        return dateEntry.equals(dateRule);
    }

    private static boolean isValidForDaysInMonth(String dateEntry, String dayInMonthRule) {
        //Checks for ?
        if (dayInMonthRule.equals("?")) {
            return true;
        }

        //Checks for a singular day in the month or for last day in the month(L) entry
        String[] ruleParts = dayInMonthRule.split("[,-]");
        String[] dateEntryParts = dateEntry.split("[.]"); //entry date

        if (dayInMonthRule.contains("L")){
            int dayNumbersBack = 1; //number of days back from the first day of the next month
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, 1);
            cal.set(Calendar.DATE, 1);
            cal.add(Calendar.DATE, dayNumbersBack);
            int daysFromEndOfMonth = cal.get(Calendar.DAY_OF_MONTH);//day of last day of month
            String daysNumberFromEndOfMonthString = String.valueOf(daysFromEndOfMonth);
            if (ruleParts.length == 1){
                //Check for a match of the entry date days for a match of the end of the month day
                return daysNumberFromEndOfMonthString.equals(dateEntryParts[0]);
            }
            //Match for last day of month that is within a range of numbers
            if (ruleParts.length > 1 && (daysNumberFromEndOfMonthString.equals(dateEntryParts[0]))){
                return true;
            }
        }

        //checks the day in entry date matches non L day values in the day month rule
        if (ruleParts.length == 1) {
            return (dateEntryParts[0].equals(ruleParts[0]));
        }

        int length;
        if (ruleParts.length > 0) {
            if(dayInMonthRule.contains("L")){//if L found, remove exclude from the length
                length = ruleParts.length - 1;
            }else{       //use of the original length
                length = ruleParts.length;
            }
            //Checks for a range of days in the month*/
            int lowerRange = Integer.parseInt(ruleParts[0]);
            for (int i = 1; i < length; i++) {
                int upperRange = Integer.parseInt(ruleParts[i]);
                //checks a range of values consecutively increasing for a match
                //returns true for a match
                if (ruleParts[i].equals(ruleParts[0])) {
                    return true;
                }
                lowerRange = upperRange;
            }
            return true;
        }
        System.out.println("dayInMonthRule : " + dayInMonthRule);
        return true;
    }

    private static boolean isValidForWeekDays(String dateEntry, String weekDayRule){
        //Checks for ?
        if (weekDayRule.equals("?")) {
            return true;
        }

        //To derive the weekday number format the entryDate for use in the class
        // DayOfWeek
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dataEntryYYYYMMDD = LocalDate.parse(dateEntry, formatter).format(formatter2);

        /*utilises the dateEntry parameter to return a numeric value for the day in the week*/
        DayOfWeek dayOfWeek = DayOfWeek.from(LocalDate.parse( dataEntryYYYYMMDD));
        int dayOfWeekNumber = dayOfWeek.get(ChronoField.DAY_OF_WEEK);

        //Checks for a singular day in the month or for last day in the month(L) entry
        String[] ruleParts = weekDayRule.split("[,]");

        int lowerRange;
        int upperRange;
        //check the range/s for a weekday number match
        for (String rulePart : ruleParts) {
            if (rulePart.contains("-")) {
                //checks weekday falls within a range
                lowerRange = Integer.parseInt(rulePart.substring(0, 1));
                upperRange = Integer.parseInt(rulePart.substring(2));
                if (dayOfWeekNumber >= lowerRange && dayOfWeekNumber <= upperRange)
                    return true;
            } else {
                //checks weekday value matches a single weekday value
                if (dayOfWeekNumber == Integer.parseInt(rulePart)) {
                    return true;
                }
            }
        }
        return false;
    }

}
