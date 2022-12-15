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
        String example1 = "??.??.???? ? ";                   //Ugyldig format:
        String example2 = "??.??.???? ? ? 1:5";              //Ugyldig format: days of week mangler tidsperioder
        String example3 = "??.??.???? ? ? 1";                //Ugyldig format: day in month mangler tidsperioder
        String example4 = "02.05.2022 ? 1:5 ?";
        String example5 = "??.??.???? 07:00-21:00 1:5 ?";
        String example6 = "??.??.???? 07:00-21:00 2:4 ?";
        String example7 = "??.??.???? 07:00-21:00 0:5 ?";
        String example8 = "??.??.???? 07:00-21:00 1:7 ?";
        String example9 = "??.??.???? 07:00-21:00 1:9 ?";
        String example10 = "??.??.???? 07:00-21:00 0:8 ?";
        String example11 = "??.??.???? 07:00-21:00 0:z ?";
        String example12 = "??.??.???? 00:00-00:00 1:3 ?";
        String example13 = "02.05.2022 09:00-22:00 ? ?";
        String example14 = "11.12.2022 09:00-22:00 ? ?";
        String example15 = "51.12.2022 09:00-22:00 ? ?";
        String example16 = "01.15.???? 09:00-22:00 ? ?";
        String example17 = "11.??.2022 09:00-22:00 ? ?";
        String example18 = "14.12.20zz 09:00-22:00 ? ?";
        String example19 = "14.12.9999 09:00-22:00 ? ?";
        String example20 = "29.02.2022 10:00-22:00 ? ?"; //Uglyldig skuddår
        String example21 = "29.02.2024 10:00-22:00 ? ?"; //Gyldig skuddår dato
        String example22 = "11/12/2022 09:00-22:00 ? ?";


        //Act
        Boolean isTrueIfAnswerValidForExample1 = openingTimes.isASavedRule(example1);
        Boolean isTrueIfAnswerValidForExample2 = openingTimes.isASavedRule(example2);
        Boolean isTrueIfAnswerValidForExample3 = openingTimes.isASavedRule(example3);
        Boolean isTrueIfAnswerValidForExample4 = openingTimes.isASavedRule(example4);
        Boolean isTrueIfAnswerValidForExample5 = openingTimes.isASavedRule(example5);
        Boolean isTrueIfAnswerValidForExample6 = openingTimes.isASavedRule(example6);
        Boolean isTrueIfAnswerValidForExample7 = openingTimes.isASavedRule(example7);
        Boolean isTrueIfAnswerValidForExample8 = openingTimes.isASavedRule(example8);
        Boolean isTrueIfAnswerValidForExample9 = openingTimes.isASavedRule(example9);
        Boolean isTrueIfAnswerValidForExample10 = openingTimes.isASavedRule(example10);
        Boolean isTrueIfAnswerValidForExample11 = openingTimes.isASavedRule(example11);
        Boolean isTrueIfAnswerValidForExample12 = openingTimes.isASavedRule(example12);
        Boolean isTrueIfAnswerValidForExample13 = openingTimes.isASavedRule(example13);
        Boolean isTrueIfAnswerValidForExample14 = openingTimes.isASavedRule(example14);
        Boolean isTrueIfAnswerValidForExample15 = openingTimes.isASavedRule(example15);
        Boolean isTrueIfAnswerValidForExample16 = openingTimes.isASavedRule(example16);
        Boolean isTrueIfAnswerValidForExample17 = openingTimes.isASavedRule(example17);
        Boolean isTrueIfAnswerValidForExample18 = openingTimes.isASavedRule(example18);
        Boolean isTrueIfAnswerValidForExample19 = openingTimes.isASavedRule(example19);
        Boolean isTrueIfAnswerValidForExample20 = openingTimes.isASavedRule(example20);
        Boolean isTrueIfAnswerValidForExample21 = openingTimes.isASavedRule(example21);
        Boolean isTrueIfAnswerValidForExample22 = openingTimes.isASavedRule(example22);

        //Assign

    }

    @Test
    void isASavedRuleTimes(){
        String example1 = "??.??.???? 07:00-21:00 1:5 ?"; //Gyldig times
        //Act
        Boolean isTrueIfAnswerValidForExample1 = openingTimes.isASavedRule(example1);
        //Assert
        Assertions.assertEquals(true, (boolean) isTrueIfAnswerValidForExample1);
    }






}

