package nav.portal.core.entities.OpeningHours;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OpeningTimesV2Test {

    @Test
    void validateDateformatPart(){

        String example1 = "??.??.???? ? ";              //Ugyldig format
        String example2 = "02.05.2022 ? 1:5 ?";         //Ugyldig Mangler åpningstider
        String example3 = "02.05.2022 09:00-22:00 ? ?"; //Gyldig  åpningstider
        String example4 = "11.12.2022 09:00-22:00 ? ?"; //Gyldig  åpningstider
        String example5 = "51.12.2022 09:00-22:00 ? ?"; //Uglyldig dagsverdi
        String example6 = "01.15.???? 09:00-22:00 ? ?"; //Gyldig månedsverdi
        String example7 = "11.??.2022 09:00-22:00 ? ?"; //Ugyldig format
        String example8 = "14.12.20zz 09:00-22:00 ? ?"; //Ugyldig format
        String example9 = "14.12.9999 09:00-22:00 ? ?"; //Gyldig  åpningstider
        String example10 = "29.02.2022 10:00-22:00 ? ?"; //Uglyldig skuddår
        String example11 = "29.02.2024 10:00-22:00 ? ?"; //Gyldig skuddår dato
        String example12 = "11/12/2022 09:00-22:00 ? ?"; //Ugyldig format

        //Act
        Boolean example1isFalse = OpeningTimesV2.isAValidRule(example1);
        Boolean example2isTrue = OpeningTimesV2.isAValidRule(example2);
        Boolean example3isTrue  = OpeningTimesV2.isAValidRule(example3);
        Boolean example4isTrue  = OpeningTimesV2.isAValidRule(example4);
        Boolean example5isFalse  = OpeningTimesV2.isAValidRule(example5);
        Boolean example6isTrue  = OpeningTimesV2.isAValidRule(example6);
        Boolean example7isFalse  = OpeningTimesV2.isAValidRule(example7);
        Boolean example8isFalse  = OpeningTimesV2.isAValidRule(example8);
        Boolean example9isTrue  = OpeningTimesV2.isAValidRule(example9);
        Boolean example10isFalse  = OpeningTimesV2.isAValidRule(example10);
        Boolean example11isTrue  = OpeningTimesV2.isAValidRule(example11);
        Boolean example12isFalse  = OpeningTimesV2.isAValidRule(example12);

        //Assert
        Assertions.assertThat(example1isFalse).isFalse();    //Ugyldig format
        Assertions.assertThat(example2isTrue).isTrue();    //Ugyldig Mangler åpningstider
        Assertions.assertThat(example3isTrue).isTrue();      //Gyldig  åpningstider
        Assertions.assertThat(example4isTrue).isTrue();      //Gyldig  åpningstider
        Assertions.assertThat(example5isFalse).isFalse();    //Uglyldig dagsverdi
        Assertions.assertThat(example6isTrue).isTrue();    //Uglyldig månedsverdi
        Assertions.assertThat(example7isFalse).isFalse();    //Ugyldig format
        Assertions.assertThat(example8isFalse).isFalse();    //Ugyldig format
        Assertions.assertThat(example9isTrue).isTrue();      //Gyldig  åpningstider
        Assertions.assertThat(example10isFalse).isFalse();   //Uglyldig skuddår
        Assertions.assertThat(example11isTrue).isTrue();    //Gyldig skuddår dato
        Assertions.assertThat(example12isFalse).isFalse(); //Ugyldig med bindestrek
    }

    @Test
    void validateTimeformatPart(){
        //Arrange
        String example1 = "??.??.???? 07:00-21:00 1:5 ?"; //Gyldig times
        String example2 = "??.??.???? 00:0z-21:00 1:5 ?"; //Uglyldig tid format
        String example3 = "??.??.???? 00:00-21:0z ? ?"; //Uglyldig tid format
        String example4 = "??.??.???? 77:29-21:00 ? ?"; //Uglyldig tid format
        String example5 = "??.??.???? 12:30-09:00 ? ?"; //Uglyldig tid åpning skal være før slutt format
        String example6 = "??.??.???? 00:00-00:00 ? ?"; //Gyldig tid angir stengt



        //Act
        Boolean example1isTrue = OpeningTimesV2.isAValidRule(example1);
        Boolean example2isFalse = OpeningTimesV2.isAValidRule(example2);
        Boolean example3isFalse  = OpeningTimesV2.isAValidRule(example3);
        Boolean example4isFalse  = OpeningTimesV2.isAValidRule(example4);
        Boolean example5isFalse  = OpeningTimesV2.isAValidRule(example5);
        Boolean example6isTrue  = OpeningTimesV2.isAValidRule(example6);

        //Assert
        Assertions.assertThat(example1isTrue).isTrue();
        Assertions.assertThat(example2isFalse).isFalse();
        Assertions.assertThat(example3isFalse).isFalse();
        Assertions.assertThat(example4isFalse).isFalse();
        Assertions.assertThat(example5isFalse).isFalse();
        Assertions.assertThat(example6isTrue).isTrue();

    }


}