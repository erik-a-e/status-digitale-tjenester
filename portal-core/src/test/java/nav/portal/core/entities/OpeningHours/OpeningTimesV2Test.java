package nav.portal.core.entities.OpeningHours;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OpeningTimesV2Test {

    @Test
    void validateDateformatPart(){

        String example1 = "??.??.???? ? ";              //Uyldig format
        String example2 = "02.05.2022 ? 1-5 ?";         //Ugyldig Mangler åpningstider
        String example3 = "02.05.2022 ? ? 09:00-22:00"; //Gyldig  åpningstider
        String example4 = "11.12.2022 ? ? 09:00-22:00"; //Gyldig  åpningstider
        String example5 = "51.12.2022 ? ? 09:00-22:00"; //Uglyldig dagsverdi
        String example6 = "01.15.???? ? ? 09:00-22:00"; //Ugyldig månedsverdi
        String example7 = "11.??.2022 ? ? 09:00-22:00"; //Ugyldig format
        String example8 = "14.12.20zz ? ? 09:00-22:00"; //Ugyldig format
        String example9 = "14.12.9999 ? ? 09:00-22:00"; //Gyldig  åpningstider
        String example10 = "29.02.2022 ? ? 10:00-22:00"; //Uglyldig skuddår
        String example11 = "29.02.2024 ? ? 10:00-22:00"; //Gyldig skuddår dato
        String example12 = "11/12/2022 ? ? 09:00-22:00"; //Ugyldig format
        String example13 = "??.01.???? ? ? 09:00-22:00"; //Gyldig format
        String example14 = "??.12.???? ? ? 09:00-22:00"; //Gyldig format
        String example15 = "??.13.???? ? ? 09:00-22:00"; //Ugyldig format
        String example16 = "??.??.???? ? ? 09:00-22:00"; //Gyldig format
        String example17 = "12.??.???? ? ? 09:00-22:00"; //Uyldig format
        String example18 = "??.??.???? ? ? 09:00-22:00"; //Gyldig format

        //Act
        Boolean example1isFalse = OpeningTimesV2.isAValidRule(example1);
        Boolean example2isFalse = OpeningTimesV2.isAValidRule(example2);
        Boolean example3isTrue  = OpeningTimesV2.isAValidRule(example3);
        Boolean example4isTrue  = OpeningTimesV2.isAValidRule(example4);
        Boolean example5isFalse  = OpeningTimesV2.isAValidRule(example5);
        Boolean example6isFalse  = OpeningTimesV2.isAValidRule(example6);
        Boolean example7isFalse  = OpeningTimesV2.isAValidRule(example7);
        Boolean example8isFalse  = OpeningTimesV2.isAValidRule(example8);
        Boolean example9isTrue  = OpeningTimesV2.isAValidRule(example9);
        Boolean example10isFalse  = OpeningTimesV2.isAValidRule(example10);
        Boolean example11isTrue  = OpeningTimesV2.isAValidRule(example11);
        Boolean example12isFalse  = OpeningTimesV2.isAValidRule(example12);
        Boolean example13isTrue  = OpeningTimesV2.isAValidRule(example13);
        Boolean example14isTrue  = OpeningTimesV2.isAValidRule(example14);
        Boolean example15isFalse  = OpeningTimesV2.isAValidRule(example15);
        Boolean example16isTrue  = OpeningTimesV2.isAValidRule(example16);
        Boolean example17isFalse  = OpeningTimesV2.isAValidRule(example17);
        Boolean example18isTrue  = OpeningTimesV2.isAValidRule(example18);

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
        Assertions.assertThat(example12isFalse).isFalse(); //Ugyldig med bindestrek
        Assertions.assertThat(example13isTrue).isTrue();      //Gyldig  åpningstider
        Assertions.assertThat(example14isTrue).isTrue();
        Assertions.assertThat(example15isFalse).isFalse();      //Gyldig  åpningstider
        Assertions.assertThat(example16isTrue).isTrue();
        Assertions.assertThat(example17isFalse).isFalse();
        Assertions.assertThat(example18isTrue).isTrue();
    }

    @Test
    void validateDayInMonthFormat(){
        //Arrange
        String example1 = "??.??.???? 5 ? 07:00-21:00";                //Gyldig
        String example2 = "??.??.???? L ? 07:00-21:00";                //Gyldig
        String example3 = "??.??.???? ? ? 07:00-21:00";                //Gyldig
        String example4 = "??.??.???? 31 ? 07:00-21:00";               //Gyldig
        String example5 = "??.??.???? 32 ? 07:00-21:00";               //Ugldig
        String example6 = "??.??.???? 1,2,L,30 ? 7:00-21:00";          //Ugldig
        String example7 = "??.??.???? 11,17,19,L ? 07:00-21:00";       //Gyldig
        String example8 = "??.??.???? 18,17,19,L ? 07:00-21:00";       //UGyldig
        String example9 = "??.??.???? 1-3,6,11,17,23 ? 07:00-21:00";   //Gyldig
        String example10 = "??.??.???? 5-3,6,15,17,23 ? 07:00-21:00";
        String example11 = "??.??.???? 2,4 ? 07:00-21:00";              //Gyldig
        String example12 = "??.??.???? 5-12 ? 07:00-21:00";             //Gyldig
        String example13 = "??.??.???? 3,5-10,22 ? 07:00-21:00";
        String example14 = "??.??.???? 18,1-2 ? 07:00-21:00";           //Ugyldig format
        String example15 = "??.??.???? 5-4,1 ? 00:00-00:00";

        //Act
        Boolean example1isTrue = OpeningTimesV2.isAValidRule(example1);
        Boolean example2isTrue = OpeningTimesV2.isAValidRule(example2);
        Boolean example3isTrue = OpeningTimesV2.isAValidRule(example3);
        Boolean example4isTrue = OpeningTimesV2.isAValidRule(example4);
        Boolean example5isFalse = OpeningTimesV2.isAValidRule(example5);
        Boolean example6isFalse = OpeningTimesV2.isAValidRule(example6);
        Boolean example7isTrue = OpeningTimesV2.isAValidRule(example7);
        Boolean example8isFalse = OpeningTimesV2.isAValidRule(example8);
        Boolean example9isTrue = OpeningTimesV2.isAValidRule(example9);
        Boolean example10isFalse = OpeningTimesV2.isAValidRule(example10);
        Boolean example11isTrue = OpeningTimesV2.isAValidRule(example11);
        Boolean example12isTrue = OpeningTimesV2.isAValidRule(example12);
        Boolean example13isTrue = OpeningTimesV2.isAValidRule(example13);
        Boolean example14isFalse = OpeningTimesV2.isAValidRule(example14);
        Boolean example15isFalse = OpeningTimesV2.isAValidRule(example15);

        //Assert
        Assertions.assertThat(example1isTrue).isTrue();
        Assertions.assertThat(example2isTrue).isTrue();
        Assertions.assertThat(example3isTrue).isTrue();
        Assertions.assertThat(example4isTrue).isTrue();
        Assertions.assertThat(example5isFalse).isFalse();
        Assertions.assertThat(example6isFalse).isFalse();
        Assertions.assertThat(example7isTrue).isTrue();
        Assertions.assertThat(example8isFalse).isFalse();
        Assertions.assertThat(example9isTrue).isTrue();
        Assertions.assertThat(example10isFalse).isFalse();
        Assertions.assertThat(example11isTrue).isTrue();
        Assertions.assertThat(example12isTrue).isTrue();
        Assertions.assertThat(example13isTrue).isTrue();
        Assertions.assertThat(example14isFalse).isFalse();
        Assertions.assertThat(example15isFalse).isFalse();
    }

    @Test
    void validateWeekdayFormat(){
        //Arrange
        String example1 = "??.??.???? ? 5 07:00-21:00";          //
        String example2 = "??.??.???? ? 1 07:00-21:00";         //Gyldig
        String example3 = "??.??.???? ? 1-3 07:00-21:00";       //Gyldig
        String example4 = "??.??.???? ? 2,4 07:00-21:00";       //Gyldig
        String example5 = "??.??.???? ? 2-4 07:00-21:00";       //Ggyldig
        String example6 = "??.??.???? ? 3,5-6 07:00-21:00";     //Glydig -dekker alle ukedager
        String example7 = "??.??.???? ? 1,2,3,4,5,6,7 07:00-21:00";    //Gyldig dato utenfor range - overst
        String example8 = "??.??.???? ? 5,4,3 07:00-21:00";    //Ugyldig dato utenfor range
        String example9 = "??.??.???? ? 4,1-2 07:00-21:00";    //Ugyldig format
        String example10 = "??.??.???? ? 5-4,1 07:00-21:00";   //Ugldig åpningstider
        String example11 = "??.??.???? ? 1,2,3,5-6 07:00-21:00";  //Gyldig
        String example12 = "??.??.???? ? ? 07:00-21:00";  //Gyldig

        //Act
        Boolean example1isTrue = OpeningTimesV2.isAValidRule(example1);
        Boolean example2isTrue = OpeningTimesV2.isAValidRule(example2);
        Boolean example3isTrue = OpeningTimesV2.isAValidRule(example3);
        Boolean example4isTrue = OpeningTimesV2.isAValidRule(example4);
        Boolean example5isTrue = OpeningTimesV2.isAValidRule(example5);
        Boolean example6isTrue = OpeningTimesV2.isAValidRule(example6);
        Boolean example7isTrue = OpeningTimesV2.isAValidRule(example7);
        Boolean example8isFalse = OpeningTimesV2.isAValidRule(example8);
        Boolean example9isFalse = OpeningTimesV2.isAValidRule(example9);
        Boolean example10isFalse = OpeningTimesV2.isAValidRule(example10);
        Boolean example11isTrue = OpeningTimesV2.isAValidRule(example11);
        Boolean example12isTrue = OpeningTimesV2.isAValidRule(example12);

        //Assert
        Assertions.assertThat(example1isTrue).isTrue();
        Assertions.assertThat(example2isTrue).isTrue();
        Assertions.assertThat(example3isTrue).isTrue();
        Assertions.assertThat(example4isTrue).isTrue();
        Assertions.assertThat(example5isTrue).isTrue();
        Assertions.assertThat(example6isTrue).isTrue();
        Assertions.assertThat(example7isTrue).isTrue();
        Assertions.assertThat(example8isFalse).isFalse();
        Assertions.assertThat(example9isFalse).isFalse();
        Assertions.assertThat(example10isFalse).isFalse();
        Assertions.assertThat(example11isTrue).isTrue();
        Assertions.assertThat(example12isTrue).isTrue();
    }

    @Test
    void validateTimeformatPart(){
        //Arrange
        String example1 = "??.??.???? ? ? 07:00-21:00"; //Gyldig times
        String example2 = "??.??.???? ? ? 00:00-21:00"; //Glyldig tid format
        String example3 = "??.??.???? ? ? 17:00-17:00"; //Uglyldig tid format
        String example4 = "??.??.???? ? ? 77:29-21:00"; //Uglyldig tid format
        String example5 = "??.??.???? ? ? 12:30-0b:00"; //Uglyldig tid åpning skal være før slutt format
        String example6 = "??.??.???? ? ? 00:00-00:00"; //Gyldig tid angir stengt
        String example7 = "??.??.???? ? ? 12:34-23:45"; //Uglyldig tid format
        String example8 = "??.??.???? ? ? 16:00-13:00"; //Uglyldig tid format
        String example9 = "??.??.???? ? ? 1a:34-16:18"; //Uglyldig tid format
        String example10 = "??.??.???? ? ? d2:b4-16:18"; //Uglyldig tid format
        String example11 = "??.??.???? ? ? 17:b1-16:18"; //Uglyldig tid format
        String example12 = "??.??.???? ? ? 10:44-a1:18"; //Uglyldig tid format
        String example13 = "??.??.???? ? ? 10:44- :1b"; //Uglyldig tid format
        String example14 = "??.??.???? ? ? 10:44-12:c1"; //Uglyldig tid format

        //Act
        Boolean example1isTrue = OpeningTimesV2.isAValidRule(example1);
        Boolean example2isTrue = OpeningTimesV2.isAValidRule(example2);
        Boolean example3isTrue  = OpeningTimesV2.isAValidRule(example3);
        Boolean example4isFalse  = OpeningTimesV2.isAValidRule(example4);
        Boolean example5isFalse  = OpeningTimesV2.isAValidRule(example5);
        Boolean example6isTrue  = OpeningTimesV2.isAValidRule(example6);
        Boolean example7isTrue  = OpeningTimesV2.isAValidRule(example7);
        Boolean example8isFalse  = OpeningTimesV2.isAValidRule(example8);
        Boolean example9isFalse  = OpeningTimesV2.isAValidRule(example9);
        Boolean example10isFalse  = OpeningTimesV2.isAValidRule(example10);
        Boolean example11isFalse  = OpeningTimesV2.isAValidRule(example11);
        Boolean example12isFalse  = OpeningTimesV2.isAValidRule(example12);
        Boolean example13isFalse  = OpeningTimesV2.isAValidRule(example13);
        Boolean example14isFalse  = OpeningTimesV2.isAValidRule(example14);

        //Assert
        Assertions.assertThat(example1isTrue).isTrue();
        Assertions.assertThat(example2isTrue).isTrue();
        Assertions.assertThat(example3isTrue).isTrue();
        Assertions.assertThat(example4isFalse).isFalse();
        Assertions.assertThat(example5isFalse).isFalse();
        Assertions.assertThat(example6isTrue).isTrue();
        Assertions.assertThat(example7isTrue).isTrue();
    }

    @Test
    void validateRules(){
        //assign
        String example1 = "06.04.2023 ? ? 00:00-00:00";
        String example2 = "07.04.2023 ? ? 00:00-00:00";
        String example3 = "10.04.2023 ? ? 00:00-00:00";
        String example4 = "24.12.2023 ? 1-5 09:00-14:00";
        String example5 = "17.05.2023 ? ? 00:00-00:00";
        String example6 = "??.??.???? L ? 07:00-18:00";
        String example7 = "??.??.???? ? 6-7 00:00-00:00";
        String example8 = "??.??.???? ? 1-5 07:00-21:00";


        //Act
        Boolean example1isTrue = OpeningTimesV2.isAValidRule(example1);
        Boolean example2isTrue = OpeningTimesV2.isAValidRule(example2);
        Boolean example3isTrue  = OpeningTimesV2.isAValidRule(example3);
        Boolean example4isTrue = OpeningTimesV2.isAValidRule(example4);
        Boolean example5isTrue = OpeningTimesV2.isAValidRule(example5);
        Boolean example6isTrue  = OpeningTimesV2.isAValidRule(example6);
        Boolean example7isTrue = OpeningTimesV2.isAValidRule(example7);
        Boolean example8isTrue = OpeningTimesV2.isAValidRule(example8);

        //Assert
        Assertions.assertThat(example1isTrue).isTrue();
        Assertions.assertThat(example2isTrue).isTrue();
        Assertions.assertThat(example3isTrue).isTrue();
        Assertions.assertThat(example4isTrue).isTrue();
        Assertions.assertThat(example5isTrue).isTrue();
        Assertions.assertThat(example6isTrue).isTrue();
        Assertions.assertThat(example7isTrue).isTrue();
        Assertions.assertThat(example8isTrue).isTrue();


    }

}