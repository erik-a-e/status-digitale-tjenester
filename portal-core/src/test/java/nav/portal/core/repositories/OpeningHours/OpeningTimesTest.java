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
    void isValidForWeekDays(){
        //Arrange
        String example1 = "??.??.???? ? ? 1:5";              //Ugyldig format: days of week mangler tidsperioder
        String example2 = "??.??.???? ? ? 1";                //Ugyldig format: day in month mangler tidsperioder
        String example3 = "??.??.???? 07:00-21:00 1:5 ?";    //Gyldig
        String example4 = "??.??.???? 07:00-21:00 2:4 ?";    //Gyldig
        String example5 = "??.??.???? 07:00-21:00 0:5 ?";    //Ugyldig dato utenfor range - nederst
        String example6 = "??.??.???? 07:00-21:00 1:7 ?";    //Glydig -dekker alle ukedager
        String example7 = "??.??.???? 07:00-21:00 1:9 ?";    //Ugyldig dato utenfor range - overst
        String example8 = "??.??.???? 07:00-21:00 0:8 ?";    //Ugyldig dato utenfor range
        String example9 = "??.??.???? 07:00-21:00 0:z ?";    //Ugyldig format
        String example10 = "??.??.???? 00:00-00:00 1:3 ?";   //Ugldig åpningstider


        //Act
        Boolean example1isFalse = openingTimes.isAValidRule(example1);
        Boolean example2isFalse = openingTimes.isAValidRule(example2);
        Boolean example3isTrue = openingTimes.isAValidRule(example3);
        Boolean example4isTrue = openingTimes.isAValidRule(example4);
        Boolean example5isFalse = openingTimes.isAValidRule(example5);
        Boolean example6isTrue = openingTimes.isAValidRule(example6);
        Boolean example7isFalse = openingTimes.isAValidRule(example7);
        Boolean example8isFalse = openingTimes.isAValidRule(example8);
        Boolean example9isFalse = openingTimes.isAValidRule(example9);
        Boolean example10isFalse = openingTimes.isAValidRule(example10);


        //Assign
        Assertions.assertFalse((boolean)example1isFalse);
        Assertions.assertFalse((boolean)example2isFalse);
        Assertions.assertTrue((boolean)example3isTrue);
        Assertions.assertTrue((boolean)example4isTrue);
        Assertions.assertFalse((boolean)example5isFalse);
        Assertions.assertTrue((boolean)example6isTrue);
        Assertions.assertFalse((boolean)example7isFalse);
        Assertions.assertFalse((boolean)example8isFalse);
        Assertions.assertFalse((boolean)example9isFalse);
        Assertions.assertFalse((boolean)example10isFalse);
    }

    @Test
    void isASavedRuleTimes(){
        //Arrange
        String example1 = "??.??.???? 07:00-21:00 1:5 ?"; //Gyldig times
        String example2 = "??.??.???? 00:0z-21:00 1:5 ?"; //Uglyldig dato format
        String example3 = "??.??.???? 00:00-21:0z 1:5 ?"; //Uglyldig dato format
        String example4 = "??.??.???? 77:29-21:00 1:5 ?"; //Uglyldig dato format
        String example5 = "??.??.???? 07:63-21:00 1:5 ?"; //Uglyldig dato format
        String example6 = "??.??.???? 07:00-99:00 1:5 ?"; //Ugyldig times
        String example7 = "??.??.???? 07:00-21:60 1:5 ?"; //Ugyldig times
        //Act
        Boolean example1isTrue = openingTimes.isAValidRule(example1);
        Boolean example2isFalse = openingTimes.isAValidRule(example2);
        Boolean example3isFalse  = openingTimes.isAValidRule(example3);
        Boolean example4isFalse  = openingTimes.isAValidRule(example4);
        Boolean example5isFalse  = openingTimes.isAValidRule(example5);
        Boolean example6isFalse  = openingTimes.isAValidRule(example6);
        Boolean example7isFalse  = openingTimes.isAValidRule(example7);
        //Assert
        Assertions.assertTrue((boolean) example1isTrue);
        Assertions.assertFalse((boolean)example2isFalse);
        Assertions.assertFalse((boolean)example3isFalse);
        Assertions.assertFalse((boolean)example4isFalse);
        Assertions.assertFalse((boolean)example5isFalse);
        Assertions.assertFalse((boolean) example6isFalse);
        Assertions.assertFalse((boolean) example7isFalse);
    }

    @Test
    void isAValidRuleForASpecifiedDate(){
        //Arrange
        String example1 = "??.??.???? ? ";              //Ugyldig format
        String example2 = "02.05.2022 ? 1:5 ?";         //Ugyldig Mangler åpningstider
        String example3 = "02.05.2022 09:00-22:00 ? ?"; //Gyldig  åpningstider
        String example4 = "11.12.2022 09:00-22:00 ? ?"; //Gyldig  åpningstider
        String example5 = "51.12.2022 09:00-22:00 ? ?"; //Uglyldig dagsverdi
        String example6 = "01.15.???? 09:00-22:00 ? ?"; //Uglyldig månedsverdi
        String example7 = "11.??.2022 09:00-22:00 ? ?"; //Ugyldig format
        String example8 = "14.12.20zz 09:00-22:00 ? ?"; //Ugyldig format
        String example9 = "14.12.9999 09:00-22:00 ? ?"; //Gyldig  åpningstider
        String example10 = "29.02.2022 10:00-22:00 ? ?"; //Uglyldig skuddår
        String example11 = "29.02.2024 10:00-22:00 ? ?"; //Gyldig skuddår dato
        String example12 = "11/12/2022 09:00-22:00 ? ?"; //Ugyldig format
        //Act
        Boolean example1isFalse = openingTimes.isAValidRule(example1);
        Boolean example2isFalse = openingTimes.isAValidRule(example2);
        Boolean example3isTrue  = openingTimes.isAValidRule(example3);
        Boolean example4isTrue  = openingTimes.isAValidRule(example4);
        Boolean example5isFalse  = openingTimes.isAValidRule(example5);
        Boolean example6isFalse  = openingTimes.isAValidRule(example6);
        Boolean example7isFalse  = openingTimes.isAValidRule(example7);
        Boolean example8isFalse  = openingTimes.isAValidRule(example8);
        Boolean example9isTrue  = openingTimes.isAValidRule(example9);
        Boolean example10isFalse  = openingTimes.isAValidRule(example10);
        Boolean example11isTrue  = openingTimes.isAValidRule(example11);
        Boolean example12isFalse  = openingTimes.isAValidRule(example12);

        //Assert
        Assertions.assertFalse((boolean)example1isFalse);
        Assertions.assertFalse((boolean)example2isFalse);
        Assertions.assertTrue((boolean)example3isTrue);
        Assertions.assertTrue((boolean)example4isTrue);
        Assertions.assertFalse((boolean)example5isFalse);
        Assertions.assertFalse((boolean)example6isFalse);
        Assertions.assertFalse((boolean)example7isFalse);
        Assertions.assertFalse((boolean)example8isFalse);
        Assertions.assertTrue((boolean)example9isTrue);
        Assertions.assertFalse((boolean)example10isFalse);
        Assertions.assertTrue((boolean)example11isTrue);
        Assertions.assertFalse((boolean)example12isFalse);

    }

    @Test
    public void isValidForDaysInTheMonth(){
        //Assign
        String example1 = "??.??.???? 07:00-21:00 ? 5";    //Gyldig random dato
        String example2 = "??.??.???? 07:00-21:00 ? 1";    //Gyldig første dag i måneden
        String example3 = "??.??.???? 07:00-21:00 ? 30";   //Gyldig nest siste dag i måneden
        String example4 = "??.??.???? 07:00-21:00 ? 31";   //Gyldig siste dag i måneden
        String example5 = "??.??.???? 07:00-21:00 ? -1";   //Gyldig siste dag av hver måned
        String example6 = "??.??.???? 07:00-21:00 ? 32";   //Ugyldig dato utenfor måned
        String example7 = "??.??.???? 07:00-21:00 ? 0";    //Ugyldig dato utenfor måned
        String example8 = "??.??.???? 07:00-21:00 ? -3";    //Ugyldig dato utenfor måned
        String example9 = "??.??.???? 07:00-21:00 ? 1 7";   //Ugyldig feil rulelengde

        //Act
        Boolean example1isTrue = openingTimes.isAValidRule(example1);
        Boolean example2isTrue = openingTimes.isAValidRule(example2);
        Boolean example3isTrue = openingTimes.isAValidRule(example3);
        Boolean example4isTrue = openingTimes.isAValidRule(example4);
        Boolean example5isTrue = openingTimes.isAValidRule(example5);
        Boolean example6isFalse  = openingTimes.isAValidRule(example6);
        Boolean example7isFalse  = openingTimes.isAValidRule(example7);
        Boolean example8isFalse  = openingTimes.isAValidRule(example8);
        Boolean example9isFalse  = openingTimes.isAValidRule(example9);

        //Assert
        Assertions.assertTrue((boolean) example1isTrue);
        Assertions.assertTrue((boolean)example2isTrue);
        Assertions.assertTrue((boolean)example3isTrue);
        Assertions.assertTrue((boolean)example4isTrue);
        Assertions.assertTrue((boolean)example5isTrue);
        Assertions.assertFalse((boolean)example6isFalse);
        Assertions.assertFalse((boolean)example7isFalse);
        Assertions.assertFalse((boolean)example8isFalse);
        Assertions.assertFalse((boolean)example9isFalse);
    }
}

