package nav.portal.core.repositories.OpeningHours;

import nav.portal.core.entities.OpeningHours.OpeningTimes;
import nav.portal.core.repositories.AreaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.util.*;

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
        Assertions.assertThat(example1isFalse).isFalse();         //Ugyldig format: days of week mangler tidsperioder
        Assertions.assertThat(example2isFalse).isFalse();        //Ugyldig format: day in month mangler tidsperioder
        Assertions.assertThat(example3isTrue).isTrue();            //Gyldig
        Assertions.assertThat(example4isTrue).isTrue();                //Gyldig
        Assertions.assertThat(example5isFalse ).isFalse();          //Ugyldig dato utenfor range - nederst
        Assertions.assertThat(example6isTrue).isTrue();                //Glydig -dekker alle ukedager
        Assertions.assertThat(example7isFalse).isFalse();          //Ugyldig dato utenfor range - overst
        Assertions.assertThat(example8isFalse).isFalse();          //Ugyldig dato utenfor range
        Assertions.assertThat(example9isFalse).isFalse();          //Ugyldig format
        Assertions.assertThat(example10isFalse).isFalse();         //Ugldig åpningstider
        Assertions.assertThat(example11isFalse).isFalse();          //Ugyldig start dato > end dato
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
        List<String> retrievedOpeningTimes = openingTimes.getOpeningTimesSet();

        //Assert
        Assertions.assertThat(example1isTrue).isTrue();       //Gyldig times
        Assertions.assertThat(example2isFalse).isFalse();     //Uglyldig dato format
        Assertions.assertThat(example3isFalse).isFalse();     //Uglyldig dato format
        Assertions.assertThat(example4isFalse).isFalse();     //Uglyldig dato format
        Assertions.assertThat(example5isFalse).isFalse();     //Uglyldig dato format
        Assertions.assertThat(example6isFalse).isFalse();    //Ugyldig times
        Assertions.assertThat(retrievedOpeningTimes.size()).isEqualTo(1);
    }

    @Test
    void getOpeningTimesSet() {
        //Arrange
        String example1 = "??.??.???? 07:00-21:00 1:5 ?"; //Gyldig times
        String example2 = "??.??.???? 00:0z-21:00 1:5 ?"; //Uglyldig dato format
        String example3 = "??.??.???? 00:00-21:0z 1:5 ?"; //Uglyldig dato format
        String example4 = "??.??.???? 77:29-21:00 1:5 ?"; //Uglyldig dato format


        //Act
        openingTimes.isAValidRule(example1);
        openingTimes.isAValidRule(example2);
        openingTimes.isAValidRule(example3);
        openingTimes.isAValidRule(example4);
        List<String> retrievedOpeningTimes = openingTimes.getOpeningTimesSet();

        //Assert
        Assertions.assertThat(retrievedOpeningTimes.size()).isEqualTo(1);

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
        Assertions.assertThat(example1isFalse).isFalse();    //Ugyldig format
        Assertions.assertThat(example2isFalse).isFalse();    //Ugyldig Mangler åpningstider
        Assertions.assertThat(example3isTrue).isTrue();      //Gyldig  åpningstider
        Assertions.assertThat(example4isTrue).isTrue();      //Gyldig  åpningstider
        Assertions.assertThat(example5isFalse).isFalse();    //Uglyldig dagsverdi
        Assertions.assertThat(example6isFalse).isFalse();    //Uglyldig månedsverdi
        Assertions.assertThat(example7isFalse).isFalse();    //Ugyldig format
        Assertions.assertThat(example8isFalse).isFalse();    //Ugyldig format
        Assertions.assertThat(example9isTrue).isTrue();      //Gyldig  åpningstider
        Assertions.assertThat(example10isFalse).isFalse();   //Uglyldig skuddår
        Assertions.assertThat(example11isTrue).isTrue();    //Gyldig skuddår dato
        Assertions.assertThat(example12isFalse).isFalse();   //Ugyldig format

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

        List<String> retrievedOpeningTimes = openingTimes.getOpeningTimesSet();

        //Assert
        Assertions.assertThat(example1isTrue).isTrue(); //Gyldig random dato
        Assertions.assertThat(example2isTrue).isTrue();  //Gyldig første dag i måneden
        Assertions.assertThat(example3isTrue).isTrue();  //Gyldig dag 30 i måneden
        Assertions.assertThat(example4isTrue).isTrue();  //Gyldig siste dag i måneden
        Assertions.assertThat(example5isTrue).isTrue();   //Gyldig siste dag av hver måned
        Assertions.assertThat(example6isFalse).isFalse();  //Ugyldig dato utenfor måned
        Assertions.assertThat(example7isFalse).isFalse();  //Ugyldig dato utenfor måned
        Assertions.assertThat(example8isFalse).isFalse();  //Ugyldig dato utenfor måned
        Assertions.assertThat(example9isFalse).isFalse();  //Ugyldig feil rulelengde
        Assertions.assertThat(example10isTrue).isTrue();   //Gyldig annen siste dag av hver måned
        Assertions.assertThat(example11isFalse).isFalse(); //Ugyldig for dato 0
        Assertions.assertThat(example12isTrue).isTrue();   //Gyldig for dato -10

        Assertions.assertThat(retrievedOpeningTimes.size()).isEqualTo(7);
    }


    @Test
    public void isOpeningLocalDate(){
        String example1 = "02.05.2023 09:00-22:00 ? ?"; //Gyldig  åpningstider
        String example2 = "07.02.2023 09:00-22:00 ? ?"; //Gyldig  åpningstider
        String example3 = "04.01.2023 09:00-22:00 ? ?"; //Gyldig  åpningstider

        Boolean example1isTrue = openingTimes.isAValidRule(example1);
        Boolean example2isTrue = openingTimes.isAValidRule(example2);
        Boolean example3isTrue = openingTimes.isAValidRule(example3);

        System.out.println(openingTimes.isAnOpeningLocalDate());
    }

    @Test
    public void isAnOpeningSpecifiedDate(){
        String example1 = "02.05.2023 09:00-22:00 ? ?"; //Gyldig  åpningstider
        String example2 = "07.02.2023 09:00-18:00 ? ?"; //Gyldig  åpningstider
        String example3 = "03.01.2023 09:00-22:00 ? ?"; //Gyldig  åpningstider

        Boolean example1isTrue = openingTimes.isAValidRule(example1);
        Boolean example2isTrue = openingTimes.isAValidRule(example2);
        Boolean example3isTrue = openingTimes.isAValidRule(example3);
        String aDate = "07.02.2023";
        System.out.println(openingTimes.isAnOpeningSpecifiedDate(aDate));
    }

    @Test
    public void isAValidOTForDayInMonthLocalTime(){
        String example1 = "??.??.???? 07:00-21:00 ? -10";
        Boolean example1isTrue = openingTimes.isAValidRule(example1);
        System.out.println(openingTimes.isAValidOTForDayInMonthLocalTime());
    }

    @Test
    public void isAValidOTForDayInMonthSpecifiedDate(){
        String example1 = "??.??.???? 07:00-23:30 ? 14";
        Boolean example1isTrue = openingTimes.isAValidRule(example1);
        System.out.println(openingTimes.isAValidOTForDayInMonthSpecifiedDate("14.01.2023"));
    }

    @Test
    public void weekDayNumberLocalDate(){
        String example1 = "??.??.???? 07:00-21:00 1:5 ?";
        Boolean example1isTrue = openingTimes.isAValidRule(example1);
        int weekdayNumber = openingTimes.weekDayNumberLocalDate();
    }

    @Test
    public void weekDayNumberForSpecifiedDate() throws ParseException {
        String example1 = "??.??.???? 07:00-21:00 1:5 ?";
        Boolean example1isTrue = openingTimes.isAValidRule(example1);
        int weekdayNumber = openingTimes.weekDayNumber("13.03.2023");
    }

    @Test
    public void isAValidOTForWeekday(){
        String example1 = "??.??.???? 07:00-23:00 1:5 ?";
        Boolean example1isTrue = openingTimes.isAValidRule(example1);
        System.out.println(openingTimes.isAValidOTForWeekday("13.03.2023"));

    }

}

