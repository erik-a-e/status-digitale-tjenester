package nav.portal.core.entities.OpeningHours;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OpeningTimesV2 {


    public static boolean isAValidRule(String rule){

        String[] ruleParts = rule.split("[\s]");
        if(ruleParts.length != 4){
            return false;
        }
        //Check if valid date format
        if(!isValidDateFormat(ruleParts[0])){
            System.out.println("Invalid date format");
            return false;
        }
        //Check if time interval is correct
        if(!isValidTimeFormat(ruleParts[1])){
            return false;
        }
        return true;
    }

    private static boolean isValidTimeFormat(String timeRule) {
        String[] ruleParts = timeRule.split("[-]");
        if(ruleParts.length != 2){
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try{
            Date midnight = sdf.parse("00:00");
            Date opening = sdf.parse(ruleParts[0]);
            Date closing = sdf.parse(ruleParts[1]);
            if(opening.equals(midnight) && closing.equals(midnight)){
                return true;
            }
            if(closing.before(opening)){
                return false;
            }
        }
        catch (ParseException e){
            System.out.println("Illegal time format, should be hh:mm-hh:mm");
            return false;
        }
        return true;

    }


    private static boolean isValidDateFormat(String dateRule){
        //Checks for ??.??.????
        if(dateRule.equals("??.??.????")){
            return true;
        }
        String[] ruleParts = dateRule.split("[.]");
        if(ruleParts.length!=3){
            return false;
        }
        //Checks for dd.mm.????
        if(ruleParts[2].equals("????")){
            String ddmmRule = ruleParts[0]+ruleParts[1];
            return ddmmRule.matches("^(0?[1-9]|[12][0-9]|3[01]).(0?[1-9]|1[012])$");
        }
        //Checks for dd.mm.yyyy
        return dateRule.matches("(((0[1-9]|[12][0-9]|3[01])([.])(0[13578]|10|12)([.])(\\d{4}))" +
                "|(([0][1-9]|[12][0-9]|30)([.])(0[469]|11)([.])(\\d{4}))|((0[1-9]|1[0-9]|2[0-8])([.])(02)([.])(\\d{4}))" +
                "|((29)(02)([.])([02468][048]00))|((29)([.])(02)([.])([13579][26]00))|((29)([.])(02)([.])([0-9][0-9][0][48]))" +
                "|((29)([.])(02)([.])([0-9][0-9][2468][048]))|((29)([.])(02)([.])([0-9][0-9][13579][26])))");

    }



}
