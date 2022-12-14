package nav.portal.core.repositories.OpeningHours;

import nav.portal.core.entities.OpeningHours.OpeningTimes;
import nav.portal.core.repositories.AreaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

//Import of Matcher and Pattern class
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpeningTimesTest {

    private final OpeningTimes openingTimes = new OpeningTimes();

    @Test
    void isASavedRule(){
        //Arrange
        String example1 = "??.??.???? 07:00-21:00 1:5 ?"; //glydig period
        String example2 = "02/05/2022 09:00-22:00 ? ?";
        String example3 = "??.??.???? 07:00-21:00 0:5 ?";
        String example4 = "??.??.???? 09:00-22:00 ? 1";
        String example5 = "11/12/2022 09:00-22:00 ? ?";


        //Act
        Boolean isTrueIfAnswerValidForExample1 = openingTimes.isASavedRule(example1);
        Boolean isTrueIfAnswerValidForExample2 = openingTimes.isASavedRule(example2);
        Boolean isTrueIfAnswerValidForExample3 = openingTimes.isASavedRule(example3);
        Boolean isTrueIfAnswerValidForExample4 = openingTimes.isASavedRule(example4);
        Boolean isTrueIfAnswerValidForExample5 = openingTimes.isASavedRule(example5);
        ArrayList<String>retrievedRule = openingTimes.getOpeningTimeRules();

        //Assign

    }

    /*@Test
    void isASavedRule(){
        //Arrange
        String validExample = "??.??.???? 07:00-21:00 1:5 ?";
        String invalidExample = "??.??.???? 07:00-21:00 0:5 ?";

        boolean validAnswer = true;
        boolean invalidAnswer = false;
        //Act
        Boolean isTrueIfAnswerValid = openingTimes.isASavedRule(validExample);
        ArrayList<String>retrievedRule = openingTimes.getOpeningTimeRules();

        //Assign
        Assertions.assertEquals(isTrueIfAnswerValid, validAnswer);
        Assertions.assertEquals(retrievedRule.size(), 1);

        Boolean isFalseWhenAnswerInValid = openingTimes.isASavedRule(invalidExample);


        //Assign
        Assertions.assertEquals(isFalseWhenAnswerInValid, invalidAnswer);
    }*/

    @Test
    void openingDateAndTime(){
        //Arrange
        //Act
        //Assign
        System.out.println(new StringBuilder().append("local date and time: ").append(openingTimes.openingDateAndTime()).toString());

    }

    @Test
    void returnOpeningDateAndTime(){
        //Arrange
        //Act
        //Assign
        String aDate = "15/12/2022";
        System.out.println(openingTimes.formatOpeningDateAndTime(aDate));

    }

    @Test
    void  getDayNumber(){
        //Arrange
        //Act
        //Assign
        String aDate = "19/12/2022";
        System.out.println(openingTimes.getDayNumber(aDate));

    }

    @Test
    void displayValidPeriod(){
        //Arrange
        String example = "??.??.???? 07:00-21:00 1:5 ?";
        String testDate1 = "15/12/2022";
        String testDate2 = "16/12/2022";
        String testDate3 = "17/12/2022";
        String testDate4 = "18/12/2022";
        String testDate5 = "19/12/2022";
        String testDate6 = "20/12/2022";
        String testDate7 = "21/12/2022";
        //Assign
        Boolean isTrueIfAnswerValidForExample = openingTimes.isASavedRule(example);
        String retrievedRule = openingTimes.retrieveOpeningTimeDate();
        //Assign
        openingTimes.displayPeriod(testDate1, retrievedRule);
        openingTimes.displayPeriod(testDate2, retrievedRule);
        openingTimes.displayPeriod(testDate3, retrievedRule);
        openingTimes.displayPeriod(testDate4, retrievedRule); //Ugyldig dato
        openingTimes.displayPeriod(testDate5, retrievedRule);
        openingTimes.displayPeriod(testDate6, retrievedRule);
        openingTimes.displayPeriod(testDate7, retrievedRule);
    }

    @Test
    void displayValidDate(){
        //Arrange
        String example = "16/12/2022 07:00-21:00 ? ?";
        String testDate1 = "15/12/2022";
        String testDate2 = "16/12/2022";
        String testDate3 = "17/12/2022";
        //Assign
        Boolean isTrueIfAnswerValidForExample = openingTimes.isASavedRule(example);
        String retrievedRule = openingTimes.retrieveOpeningTimeDate();
        //Assign
        openingTimes.displayDate(testDate1);
        openingTimes.displayDate(testDate2);
        openingTimes.displayDate(testDate3);
    }
}
