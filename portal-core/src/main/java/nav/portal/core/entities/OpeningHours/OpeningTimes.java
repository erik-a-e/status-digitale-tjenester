package nav.portal.core.entities.OpeningHours;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.text.SimpleDateFormat;
import java.util.Date;

//Import of Matcher and Pattern class


public class OpeningTimes {
    private String[] rules;
    private String openingTimeRule;
    private ArrayList<String>openingTimesRules;


    public OpeningTimes ( ){
        this.openingTimesRules = new ArrayList<>();
    }

    public OpeningTimes (String openingTimeRule){
        this.openingTimeRule = openingTimeRule;
        this.openingTimesRules = new ArrayList<>();
    }

    public String getOpeningTimeRule(){
        return this.openingTimeRule;
    }

    public ArrayList<String> getOpeningTimeRules(){
        ArrayList<String> openingTimesRulesList = new ArrayList<>(openingTimesRules);
        return openingTimesRulesList;
    }

    public void setOpeningTimeRule(String openingTimeRule){
        this.openingTimeRule = openingTimeRule;
    }

    public Boolean isSaved(String openingTimeRule){
        if( isAValidRule(openingTimeRule)) {
            openingTimesRules.add(openingTimeRule);
            return true;
        }else{
            return false;
        }
    }

    private Boolean isAValidRule(String openingTimeRule){
        if (isLengthValid(openingTimeRule) && isValidFormatTimes(rules)){
            System.out.println("perform isAValidDayDateOrPeriod");
            return isAValidDayDateOrPeriod(rules);
        }else{
            return false;
        }
    }

    private Boolean isAValidDayDateOrPeriod(String[] rules){
        if (!rules[2].matches("\s*[?*]\s*")){
            System.out.println("rule 2 " + rules[2]);
            System.out.println("perform isValidFormatForWeekday");
            return isValidFormatForWeekday(rules);
        }else{
            return false;
        }
    }

    private Boolean isLengthValid(String openingTimeRule){
        String[] ruleParts = openingTimeRule.split("[\s]");
        if (ruleParts.length == 4){
            rules = new String [ruleParts.length];
            rules[0] = ruleParts[0];
            rules[1] = ruleParts[1];
            rules[2] = ruleParts[2];
            rules[3] = ruleParts[3];
            return true;
          }else{
            return false;
        }
    }

    private Boolean isValidFormatForWeekday (String[] rules) {
        //if (rules[2].substring(0,1).matches("[1-7]")
        //     && rules[2].substring(2).matches("[1-7]"))
        if (rules[2].substring(0).matches("[1-7]\s*-\s*[1-7]")) {
            int range1 = Integer.parseInt(rules[2].substring(0,1));
            int range2 = Integer.parseInt(rules[2].substring(2));
            System.out.println("isAValidWeekday");
            return range1 <= range2;
        }else{
            return false;
        }
    }




    private Boolean isValidFormatTimes(String[] rules){ //
        if ((rules[1].substring(0,5).matches("([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]"))
                && (rules[1].substring(6,11).matches("([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]"))){
            int from = toMins(rules[1].substring(0, 5));
            int to = toMins(rules[1].substring(6, 11));
            return from < to;
        }else{
            return false;
        }
    }

    private static int toMins(String s) {
        String[] hourMin = s.split(":");
        int hour = Integer.parseInt(hourMin[0]);
        int mins = Integer.parseInt(hourMin[1]);
        int hoursInMins = hour * 60;
        return hoursInMins + mins;
    }

    public String openingDateAndTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        System.out.println(formatter.format(date));
        return formatter.format(date);
    }


}




