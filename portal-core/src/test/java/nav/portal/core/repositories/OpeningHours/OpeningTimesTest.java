package nav.portal.core.repositories.OpeningHours;

import nav.portal.core.entities.OpeningHours.OpeningTimes;
import nav.portal.core.entities.OpeningHours.OpeningTimesV2;
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
    public void isASpecificDate(){
        //Assign
        String example1 = "29.02.2023 07:00-22:00 ? ?"; //Ugyldig  dato
        String example2 = "10.04.2023 07:00-22:00 ? ?"; //Gyldig  åpningstider
        String example3 = "07.02.2023 07:00-22:00 ? ?"; //Gyldig  åpningstider
        String example4 = "04.01.2023 07:00-22:00 ? ?"; //Gyldig  åpningstider
        String example5 = "17.05.2023 07:00-22:00 ? ?"; //Gyldig  åpningstider
        String example6 = "11.01.2023 07:00-22:00 ? ?"; //Gyldig  åpningstider

        Boolean isValid1  = openingTimes.isAValidRule(example1);
        Boolean isValid2 = openingTimes.isAValidRule(example2);
        Boolean isValid3 = openingTimes.isAValidRule(example3);
        Boolean isValid4  = openingTimes.isAValidRule(example4);
        Boolean isValid5  = openingTimes.isAValidRule(example5);
        Boolean isValid6  = openingTimes.isAValidRule(example6);

        //Act
        boolean date04012023 = openingTimes.isASpecificDate("04.01.2023");  //Gyldig  dato
        boolean date06022023 = openingTimes.isASpecificDate("06.02.2023");  //Ugyldig dato
        boolean date10042023 = openingTimes.isASpecificDate("10.04.2023");  //Gyldig  dato
        boolean date17052023 = openingTimes.isASpecificDate("17.05.2023");  //Gyldig  dato
        boolean date18052023 = openingTimes.isASpecificDate("18.05.2023");  //Ugyldig  dato
        boolean date29022023 = openingTimes.isASpecificDate("29.02.2023");  //Ugyldig  dato
        boolean dateLocalTime = openingTimes.isASpecificDate("");

        //Assert
        Assertions.assertThat(date04012023).isTrue();       //Gyldig  dato
        Assertions.assertThat(date06022023).isFalse();      //Ugyldig dato
        Assertions.assertThat(date10042023).isTrue();       //Gyldig  dato
        Assertions.assertThat(date17052023).isTrue();       //Gyldig  dato
        Assertions.assertThat(date18052023).isFalse();      //Ugyldig dato
        Assertions.assertThat(date29022023).isFalse();      //Ugyldig dato
        Assertions.assertThat(dateLocalTime).isTrue();      //Gyldig  nåtid
    }

    @Test
    public void isAValidOTForDayInMonth(){
        //Assign
        String example1 = "??.??.???? 07:00-19:00 ? 14";   //Gyldig  dag
        String example2 = "??.??.???? 07:00-19:00 ? -10";  //Gyldig  dag
        String example3 = "??.??.???? 07:00-19:00 ? 17";   //Gyldig  dag
        String example4 = "??.??.???? 07:00-19:00 ? 0";    //Ugyldig dag
        String example5 = "??.??.???? 07:00-19:00 ? -1";   //Gyldig  dag
        String example6 = "??.??.???? 07:00-19:00 ? -4";   //Gyldig  dag
        String example7 = "??.??.???? 07:00-19:00 ? 29";   //Gyldig  dag
        String example8  = "??.??.???? 07:00-19:00 ? 28";  //Gyldig  dag
        String example9 = "??.??.???? 07:00-19:00 ? 1";   //Gyldig  dag


        Boolean isValid1 = openingTimes.isAValidRule(example1);
        Boolean isValid2 = openingTimes.isAValidRule(example2);
        Boolean isValid3 = openingTimes.isAValidRule(example3);
        Boolean isValid4 = openingTimes.isAValidRule(example4);
        Boolean isValid5 = openingTimes.isAValidRule(example5);
        Boolean isValid6 = openingTimes.isAValidRule(example6);
        Boolean isValid7 = openingTimes.isAValidRule(example7);
        Boolean isValid8 = openingTimes.isAValidRule(example8);
        Boolean isValid9 = openingTimes.isAValidRule(example9);

        //Act
        boolean date14012023 = openingTimes.isAValidOTForDayInMonth("14.01.2023");
        boolean date22012023 = openingTimes.isAValidOTForDayInMonth("22.01.2023");
        boolean date17012023 = openingTimes.isAValidOTForDayInMonth("17.01.2023");
        boolean date03012023 = openingTimes.isAValidOTForDayInMonth("03.01.2023");
        boolean date28012023 = openingTimes.isAValidOTForDayInMonth("28.01.2023");
        boolean date29012023 = openingTimes.isAValidOTForDayInMonth("29.01.2023");
        boolean date30012023 = openingTimes.isAValidOTForDayInMonth("30.01.2023");
        boolean date31012023 = openingTimes.isAValidOTForDayInMonth("31.01.2023");

        //Assert
        Assertions.assertThat(date28012023).isTrue();        //Gyldig  dato
        Assertions.assertThat(date29012023).isTrue();        //Gyldig  dato
        Assertions.assertThat(date03012023).isFalse();       //Gyldig  dato
        Assertions.assertThat(date31012023).isTrue();        //Gyldig  dato
        Assertions.assertThat(date03012023).isFalse();       //Ugyldig  dato
        Assertions.assertThat(date17012023).isTrue();        //Gyldig  dato
        Assertions.assertThat(date14012023).isTrue();        //Gyldig  dato
        Assertions.assertThat(date22012023).isTrue();        //Gyldig  dato
    }

    @Test
    public void isAValidOTForWeekday(){

        //Gyldig fra måndag til fredag fra kl.07:00 - kl.19:00
        /*Assign
        String example1 = "??.??.???? 07:00-19:00 1:5 ?";                                //Gyldig fra mandag til fredag
        Boolean validRuleForWeekDay = openingTimes.isAValidRule(example1);
        //Act
        boolean dateForSunday = openingTimes.isAValidOTForWeekday("12.03.2023");         //søndag
        boolean dateForMonday = openingTimes.isAValidOTForWeekday("13.03.2023");         //måndag
        boolean dateForTuesday = openingTimes.isAValidOTForWeekday("14.03.2023");        //tirsdag
        boolean dateForWednesday = openingTimes.isAValidOTForWeekday("15.03.2023");      //ondsag
        boolean dateForThursday = openingTimes.isAValidOTForWeekday("16.03.2023");       //torsdag
        boolean dateForFriday = openingTimes.isAValidOTForWeekday("17.03.2023");         //fredag
        boolean dateForSaturday = openingTimes.isAValidOTForWeekday("18.03.2023");       //lørdag
        System.out.println(openingTimes.isAValidOTForWeekday(""));                       // Nåtid
        //Assert
        Assertions.assertThat(validRuleForWeekDay).isTrue();                              //gyldig regler mandag-fredag
        Assertions.assertThat(dateForSunday).isFalse();                                   //søndag
        Assertions.assertThat(dateForMonday).isTrue();                                    //månddag
        Assertions.assertThat(dateForTuesday).isTrue();                                   //tirsdag
        Assertions.assertThat(dateForWednesday).isTrue();                                 //onsdag
        Assertions.assertThat(dateForThursday).isTrue();                                  //torsdag
        Assertions.assertThat(dateForFriday).isTrue();                                    //fredag
        Assertions.assertThat(dateForSaturday).isFalse();                                 //lørdag*/


        //Gyldig fra onsdag til torsdag fra kl.07:00 - kl.19:00
        //Assign
        /*String example1 = "??.??.???? 07:00-19:00 3:4 ?";                                //Gyldig fra onsdag til torsdag
        Boolean validRuleForWeekDay = openingTimes.isAValidRule(example1);
        //Act
        boolean dateForSunday = openingTimes.isAValidOTForWeekday("12.03.2023");           //søndag
        boolean dateForMonday = openingTimes.isAValidOTForWeekday("13.03.2023");           //måndag
        boolean dateForTuesday = openingTimes.isAValidOTForWeekday("14.03.2023");          //tirsdag
        boolean dateForWednesday = openingTimes.isAValidOTForWeekday("15.03.2023");        //ondsag
        boolean dateForThursday = openingTimes.isAValidOTForWeekday("16.03.2023");         //torsdag
        boolean dateForFriday = openingTimes.isAValidOTForWeekday("17.03.2023");           //fredag
        boolean dateForSaturday = openingTimes.isAValidOTForWeekday("18.03.2023");         //lørdag
        System.out.println(openingTimes.isAValidOTForWeekday(""));                         // Nåtid
        //Assert
        Assertions.assertThat(validRuleForWeekDay).isTrue();                              //gyldig regler onsdag-tordag
        Assertions.assertThat(dateForSunday).isFalse();                                   //søndag
        Assertions.assertThat(dateForMonday).isFalse();                                   //monddag
        Assertions.assertThat(dateForTuesday).isFalse();                                  //tirsdag
        Assertions.assertThat(dateForWednesday).isTrue();                                 //onsdag
        Assertions.assertThat(dateForThursday).isTrue();                                  //torsdag
        Assertions.assertThat(dateForFriday).isFalse();                                   //fredag
        Assertions.assertThat(dateForSaturday).isFalse();                                 //lørdag*/


        //Gyldig fra måndag til søndag fra kl.01:00 - kl.06:00
        //Assign
        /*String example1 = "??.??.???? 01:00-06:00 1:7 ?";                                //Gyldig fra måndag til sondag åpeningstid utendor dagtid
        Boolean validRuleForWeekDay = openingTimes.isAValidRule(example1);
        //Act
        boolean dateForSunday = openingTimes.isAValidOTForWeekday("12.03.2023");    //søndag
        boolean dateForMonday = openingTimes.isAValidOTForWeekday("13.03.2023");    //måndag
        boolean dateForTuesday = openingTimes.isAValidOTForWeekday("14.03.2023");   //tirsdag
        boolean dateForWednesday = openingTimes.isAValidOTForWeekday("15.03.2023"); //ondsag
        boolean dateForThursday = openingTimes.isAValidOTForWeekday("16.03.2023");  //torsdag
        boolean dateForFriday = openingTimes.isAValidOTForWeekday("17.03.2023");    //fredag
        boolean dateForSaturday = openingTimes.isAValidOTForWeekday("18.03.2023");  //lørdag
        System.out.println(openingTimes.isAValidOTForWeekday(""));                  // Nåtid
        //Assert
        Assertions.assertThat(validRuleForWeekDay).isTrue();                              //gyldig regler måndag-tordag
        Assertions.assertThat(dateForSunday).isFalse();                                   //søndag
        Assertions.assertThat(dateForMonday).isFalse();                                    //monddag
        Assertions.assertThat(dateForTuesday).isFalse();                                   //tirsdag
        Assertions.assertThat(dateForWednesday).isFalse();                                 //onsdag
        Assertions.assertThat(dateForThursday).isFalse();                                  //torsdag
        Assertions.assertThat(dateForFriday).isFalse();                                    //fredag
        Assertions.assertThat(dateForSaturday).isFalse();                                 //lørdag*/

        //Gyldig fra søndag til lørdag fra kl.00:00 - kl.23:59
        //Assign
        String example1 = "??.??.???? 00:00-23:59 1:7 ?";                                //Gyldig fra mandag til fredag
        Boolean validRuleForWeekDay = openingTimes.isAValidRule(example1);
        //Act
        boolean dateForSunday = openingTimes.isAValidOTForWeekday("12.03.2023");    //søndag
        boolean dateForMonday = openingTimes.isAValidOTForWeekday("13.03.2023");    //måndag
        boolean dateForTuesday = openingTimes.isAValidOTForWeekday("14.03.2023");   //tirsdag
        boolean dateForWednesday = openingTimes.isAValidOTForWeekday("15.03.2023"); //ondsag
        boolean dateForThursday = openingTimes.isAValidOTForWeekday("16.03.2023");  //torsdag
        boolean dateForFriday = openingTimes.isAValidOTForWeekday("17.03.2023");    //fredag
        boolean dateForSaturday = openingTimes.isAValidOTForWeekday("18.03.2023");  //lørdag
        System.out.println(openingTimes.isAValidOTForWeekday(""));                  // Nåtid
        //Assert
        Assertions.assertThat(validRuleForWeekDay).isTrue();                              //gyldig regler mandag-fredag
        Assertions.assertThat(dateForSunday).isTrue();                                    //søndag
        Assertions.assertThat(dateForMonday).isTrue();                                    //månddag
        Assertions.assertThat(dateForTuesday).isTrue();                                   //tirsdag
        Assertions.assertThat(dateForWednesday).isTrue();                                 //onsdag
        Assertions.assertThat(dateForThursday).isTrue();                                  //torsdag
        Assertions.assertThat(dateForFriday).isTrue();                                    //fredag
        Assertions.assertThat(dateForSaturday).isTrue();                                  //lørdag
    }

}

