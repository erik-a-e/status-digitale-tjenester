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
        String example11 = "??.??.???? 07:00-21:00 5:1 ?";    //Ugyldig start dato > end dato

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
        Boolean example11isFalse = openingTimes.isAValidRule(example11);


        //Assign
        Assertions.assertFalse(example1isFalse);          //Ugyldig format: days of week mangler tidsperioder
        Assertions.assertFalse(example2isFalse);          //Ugyldig format: day in month mangler tidsperioder
        Assertions.assertTrue(example3isTrue);            //Gyldig
        Assertions.assertTrue(example4isTrue);            //Gyldig
        Assertions.assertFalse(example5isFalse);          //Ugyldig dato utenfor range - nederst
        Assertions.assertTrue(example6isTrue);            //Glydig -dekker alle ukedager
        Assertions.assertFalse(example7isFalse);          //Ugyldig dato utenfor range - overst
        Assertions.assertFalse(example8isFalse);          //Ugyldig dato utenfor range
        Assertions.assertFalse(example9isFalse);          //Ugyldig format
        Assertions.assertFalse(example10isFalse);         //Ugldig åpningstider
        Assertions.assertFalse(example11isFalse);          //Ugyldig start dato > end dato
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


        //Act
        Boolean example1isTrue = openingTimes.isAValidRule(example1);
        Boolean example2isFalse = openingTimes.isAValidRule(example2);
        Boolean example3isFalse  = openingTimes.isAValidRule(example3);
        Boolean example4isFalse  = openingTimes.isAValidRule(example4);
        Boolean example5isFalse  = openingTimes.isAValidRule(example5);
        Boolean example6isFalse  = openingTimes.isAValidRule(example6);

        //Assert
        Assertions.assertTrue(example1isTrue);      //Gyldig times
        Assertions.assertFalse(example2isFalse);     //Uglyldig dato format
        Assertions.assertFalse(example3isFalse);     //Uglyldig dato format
        Assertions.assertFalse(example4isFalse);     //Uglyldig dato format
        Assertions.assertFalse(example5isFalse);     //Uglyldig dato format
        Assertions.assertFalse(example6isFalse);    //Ugyldig times

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
        Assertions.assertFalse(example1isFalse);    //Ugyldig format
        Assertions.assertFalse(example2isFalse);    //Ugyldig Mangler åpningstider
        Assertions.assertTrue(example3isTrue);      //Gyldig  åpningstider
        Assertions.assertTrue(example4isTrue);      //Gyldig  åpningstider
        Assertions.assertFalse(example5isFalse);    //Uglyldig dagsverdi
        Assertions.assertFalse(example6isFalse);    //Uglyldig månedsverdi
        Assertions.assertFalse(example7isFalse);    //Ugyldig format
        Assertions.assertFalse(example8isFalse);    //Ugyldig format
        Assertions.assertTrue(example9isTrue);      //Gyldig  åpningstider
        Assertions.assertFalse(example10isFalse);   //Uglyldig skuddår
        Assertions.assertTrue(example11isTrue);     //Gyldig skuddår dato
        Assertions.assertFalse(example12isFalse);   //Ugyldig format

    }

    @Test
    public void isValidForDaysInTheMonth(){
        //Assign
        String example1 = "??.??.???? 07:00-21:00 ? 5";    //Gyldig random dato
        String example2 = "??.??.???? 07:00-21:00 ? 1";    //Gyldig første dag i måneden
        String example3 = "??.??.???? 07:00-21:00 ? 30";   //Gyldig dag 30 i måneden
        String example4 = "??.??.???? 07:00-21:00 ? 31";   //Gyldig siste dag i måneden
        String example5 = "??.??.???? 07:00-21:00 ? -1";   //Gyldig siste dag av hver måned
        String example6 = "??.??.???? 07:00-21:00 ? 32";   //Ugyldig dato utenfor måned
        String example7 = "??.??.???? 07:00-21:00 ? 0";    //Ugyldig dato utenfor måned
        String example8 = "??.??.???? 07:00-21:00 ? -11";    //Ugyldig dato utenfor måned
        String example9 = "??.??.???? 07:00-21:00 ? 1 7";   //Ugyldig feil rulelengde
        String example10 = "??.??.???? 07:00-21:00 ? -2";   //Gyldig annen siste dag av hver måned
        String example11 = "??.??.???? 07:00-21:00 ? 0";    //Ugyldig for dato 0
        String example12 = "??.??.???? 07:00-21:00 ? -10";    //Gyldig for dato -10
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
        Boolean example10isTrue = openingTimes.isAValidRule(example10);
        Boolean example11isFalse  = openingTimes.isAValidRule(example11);
        Boolean example12isTrue = openingTimes.isAValidRule(example12);

        //Assert
        Assertions.assertTrue(example1isTrue); //Gyldig random dato
        Assertions.assertTrue(example2isTrue);  //Gyldig første dag i måneden
        Assertions.assertTrue(example3isTrue);  //Gyldig dag 30 i måneden
        Assertions.assertTrue(example4isTrue);  //Gyldig siste dag i måneden
        Assertions.assertTrue(example5isTrue);   //Gyldig siste dag av hver måned
        Assertions.assertFalse(example6isFalse);  //Ugyldig dato utenfor måned
        Assertions.assertFalse(example7isFalse);  //Ugyldig dato utenfor måned
        Assertions.assertFalse(example8isFalse);  //Ugyldig dato utenfor måned
        Assertions.assertFalse(example9isFalse);  //Ugyldig feil rulelengde
        Assertions.assertTrue(example10isTrue);   //Gyldig annen siste dag av hver måned
        Assertions.assertFalse(example11isFalse); //Ugyldig for dato 0
        Assertions.assertTrue(example12isTrue);   //Gyldig for dato -10
    }
}

