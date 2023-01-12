package nav.portal.core.entities.OpeningHours;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OpeningTimesV2 {


    public static boolean isAValidRule(String rule) {
        //System.out.println("Rule: " + rule);
        String[] ruleParts = rule.split("[\s]");
        if (ruleParts.length != 4) {
           return false;
        }
        //Check if valid date format
       if (!isValidDateFormat(ruleParts[0])) {
            System.out.println("Invalid date format");
            return false;
        }
        //Check if time interval is correct
        if (!isValidTimeFormat(ruleParts[1])) {
            return false;
        }

        //Check if weekday range is correct
        if (!isValidWeekdayFormat(ruleParts[2])) {
            System.out.println("Invalid weekday format");
            return false;
        }

        //Check if weekday range is correct
        if (!isValidDayInMonthFormat(ruleParts[3])) {
            System.out.println("Invalid day in month format");
            return false;
        }

        return true;
    }

    private static boolean isValidTimeFormat(String timeRule) {
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


    private static boolean isValidDateFormat(String dateRule) {
        //Checks for ??.??.????
        if (dateRule.equals("??.??.????")) {
            return true;
        }
        String[] ruleParts = dateRule.split("[.]");
        if (ruleParts.length != 3) {
            return false;
        }
        //Checks for dd.mm.????
        if (ruleParts[2].equals("????")) {
            String ddmmRule = ruleParts[0] + ruleParts[1];
            return ddmmRule.matches("^(0?[1-9]|[12][0-9]|3[01]).(0?[1-9]|1[012])$");
        }
        //Checks for dd.mm.yyyy
        return dateRule.matches("(((0[1-9]|[12][0-9]|3[01])([.])(0[13578]|10|12)([.])(\\d{4}))" +
                "|(([0][1-9]|[12][0-9]|30)([.])(0[469]|11)([.])(\\d{4}))|((0[1-9]|1[0-9]|2[0-8])([.])(02)([.])(\\d{4}))" +
                "|((29)(02)([.])([02468][048]00))|((29)([.])(02)([.])([13579][26]00))|((29)([.])(02)([.])([0-9][0-9][0][48]))" +
                "|((29)([.])(02)([.])([0-9][0-9][2468][048]))|((29)([.])(02)([.])([0-9][0-9][13579][26])))");

    }

    static boolean isValidWeekdayFormat(String weekDayRule) {
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
                //if not a valid weekday number or range values increase consecutively
                if (!ruleParts[i].matches("[1-7]") || !(lowerRange <= upperRange)) {
                    return false;
                }
                lowerRange = upperRange;
            }
            return true;
        }
        return false;
    }




}
