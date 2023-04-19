package nav.portal.core.openingHours;

import nav.portal.core.entities.OpeningHoursGroup;
import nav.portal.core.entities.OpeningHoursRuleEntity;
import nav.portal.core.repositories.SampleData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OpeningHoursParserTest {

    @Test
    void temp(){
        //Assign
        //BevHelligdager2023
        String helligDagRule1 = "06.04.2023 ? ? 00:00-00:00";
        String helligDagRule2 = "07.04.2023 ? ? 00:00-00:00";
        String helligDagRule3 = "10.04.2023 ? ? 00:00-00:00";
        //Basis
        String basisRule1 = "24.12.???? ? 1-5 09:00-14:00";
        String basisRule2 = "17.05.???? ? ? 00:00-00:00";
        String basisRule3 = "??.??.???? L ? 07:00-18:00";
        String basisRule4 = "??.??.???? ? 6-7 00:00-00:00";
        String basisRule5 = "??.??.???? ? 1-5 07:00-21:00";
        String basisRule6 = "??.??.???? 1-5,10-L ? 07:00-21:00";


        //Services
        LocalDate normalWednesday = LocalDate.of(2023,1,18);
        LocalDate lastOfJan = LocalDate.of(2023,1,31);
        LocalDate lastOfFeb = LocalDate.of(2023,2,28);
        LocalDate normalTuesday = LocalDate.of(2023,3,7);
        LocalDate easterFriday = LocalDate.of(2023,4,7);
        LocalTime midday = LocalTime.of(12,00);
        LocalTime at21 = LocalTime.of(21,00);


        LocalDateTime normalDay = LocalDateTime.of(normalWednesday,midday);
        LocalDateTime normalDayAt21 = LocalDateTime.of(normalWednesday,at21);
        LocalDateTime lastDayOfJanuary = LocalDateTime.of(lastOfJan,midday);
        LocalDateTime lastDayOfFebruary = LocalDateTime.of(lastOfFeb,midday);
        LocalDateTime normTuesday = LocalDateTime.of(normalTuesday,midday);
        LocalDateTime easterFridayMidday = LocalDateTime.of(easterFriday,midday);


        //Act

        //test for bidrag1
//        boolean normalDayIsNotHelligdag = OpeningTimesV2.isOpen(normalDay, helligDagRule1);
//        boolean normalDayIsNotLastDayOfMonth = OpeningTimesV2.isOpen(normalDay, basisRule3);
//        boolean normalDayIsNormalDay = OpeningTimesV2.isOpen(normalDay,basisRule5);
//        boolean normalDayIsNormalDayAt21 = OpeningTimesV2.isOpen(normalDayAt21,basisRule5);
//        boolean lastOfJanuaryIsOpenMidday = OpeningTimesV2.isOpen(lastDayOfJanuary,basisRule3);
//        boolean lastOfFebruaryIsOpenMidday = OpeningTimesV2.isOpen(lastDayOfFebruary,basisRule3);
        boolean outsideOfRangeTuesday = OpeningHoursParser.isOpen(normTuesday,basisRule6);

        boolean easterFridayMiddayHelligDagRule1  = OpeningHoursParser.isOpen(easterFridayMidday,helligDagRule1);
        boolean easterFridayMiddayHelligDagRule2  = OpeningHoursParser.isOpen(easterFridayMidday,helligDagRule2);
        boolean easterFridayMiddayHelligDagRule3 = OpeningHoursParser.isOpen(easterFridayMidday,helligDagRule3);
        boolean easterFridayMiddayHelliBasisRule1 = OpeningHoursParser.isOpen(easterFridayMidday,basisRule1);
        boolean easterFridayMiddayHelliBasisRule2 = OpeningHoursParser.isOpen(easterFridayMidday,basisRule2);
        boolean easterFridayMiddayHelliBasisRule3 = OpeningHoursParser.isOpen(easterFridayMidday,basisRule3);
        boolean easterFridayMiddayHelliBasisRule4 = OpeningHoursParser.isOpen(easterFridayMidday,basisRule4);
        boolean easterFridayMiddayHelliBasisRule5 = OpeningHoursParser.isOpen(easterFridayMidday,basisRule5);
        boolean easterFridayMiddayHelliBasisRule6 = OpeningHoursParser.isOpen(easterFridayMidday,basisRule6);

        //Assert
//        Assertions.assertThat(normalDayIsNotHelligdag).isFalse();
//        Assertions.assertThat(normalDayIsNotLastDayOfMonth).isFalse();
//        Assertions.assertThat(normalDayIsNormalDay).isTrue();
//        Assertions.assertThat(normalDayIsNormalDayAt21).isTrue();
//        Assertions.assertThat(lastOfJanuaryIsOpenMidday).isTrue();
//        Assertions.assertThat(lastOfFebruaryIsOpenMidday).isTrue();
        Assertions.assertThat(outsideOfRangeTuesday).isFalse();

        Assertions.assertThat(easterFridayMiddayHelligDagRule1).isFalse();
        Assertions.assertThat(easterFridayMiddayHelligDagRule2).isFalse();
        Assertions.assertThat(easterFridayMiddayHelligDagRule3).isFalse();
        Assertions.assertThat(easterFridayMiddayHelliBasisRule1).isFalse();
        Assertions.assertThat(easterFridayMiddayHelliBasisRule2).isFalse();
        Assertions.assertThat(easterFridayMiddayHelliBasisRule3).isFalse();
        Assertions.assertThat(easterFridayMiddayHelliBasisRule4).isFalse();
        Assertions.assertThat(easterFridayMiddayHelliBasisRule5).isTrue();
        Assertions.assertThat(easterFridayMiddayHelliBasisRule6).isFalse();
    }


    @Test
    void getOpeninghours(){
        //Assign
        String outsideOfOH = "00:00-00:00";
        String example1 = "??.??.???? ? 1-5 07:00-21:00";                //Valid weekday mellom kl.07-21
        LocalDate normalWeekDay = LocalDate.of(2023,4,20);
        LocalTime midday = LocalTime.of(12,00);

        LocalDate normalWeekend = LocalDate.of(2023,4,22); //Ugyldig Weekend

        String basisRule1 = "24.12.???? ? 1-5 09:00-14:00"; //Christmas eve
        String basisRule2 = "17.05.???? ? ? 00:00-00:00"; //Norway's National Holiday
        String basisRule3 = "??.??.???? L ? 07:00-18:00"; //Valid for only the Last day of Month
        String basisRule4 = "??.??.???? 1-5,25-30 ? 07:00-21:00"; // Valid for days 1 to 5 or 25 to 30;
        String basisRule5 = "18.04.2023 ? 1-5 15:00-22:00"; //Today's date invalid Time

        LocalDate christmasEve = LocalDate.of(2024,12,24);
        LocalDate christmasEveWeekend = LocalDate.of(2023,12,24);
        LocalDate nationalDay = LocalDate.of(2023,05,17);
        LocalDate validSaturdayDay29 = LocalDate.of(2023,04,29); //Valid test of basisRule4
        LocalDate lastDayOfMonth = LocalDate.of(2023,8,31); //Valid test of basisRule3
        LocalDate todaysDate = LocalDate.of(2023,04,18);


        //Act
        String validWeekdayOH = OpeningHoursParser.getOpeninghours(normalWeekDay, example1);
        //String invalidWeekendOH = OpeningHoursParser.getOpeninghours(normalWeekend, example1);String validChristmasEve = OpeningHoursParser.getOpeninghours(christmasEve, basisRule1);
        //String inValidChristmasEve = OpeningHoursParser.getOpeninghours(christmasEveWeekend, basisRule1);

        //String validNationalDay = OpeningHoursParser.getOpeninghours(nationalDay, basisRule2);
        //String validDay29 = OpeningHoursParser.getOpeninghours(validSaturdayDay29, basisRule4);
        //String validLastDayOfMonth = OpeningHoursParser.getOpeninghours(lastDayOfMonth, basisRule3);
        //String invalidLastDayOfMonth = OpeningHoursParser.getOpeninghours(lastDayOfMonth, basisRule4); //invalid date
        String invalidTimeTodaysDate = OpeningHoursParser.getOpeninghours(todaysDate, basisRule5); //invalid date
        //Assert


        Assertions.assertThat(validWeekdayOH).isEqualTo("07:00-21:00");
        //Assertions.assertThat(invalidWeekendOH).isNotEqualTo(example1.substring(17)); //weekend - non valid opening hours

        //Non valid weekday
        //Assertions.assertThat(invalidWeekendOH).isEqualTo(outsideOfOH);

        //Christmas Eve
       //Assertions.assertThat(validChristmasEve).isEqualTo("09:00-14:00");
        //Assertions.assertThat(inValidChristmasEve).isEqualTo("00:00-00:00");
        //Assertions.assertThat(validNationalDay).isEqualTo("00:00-00:00");
        //Assertions.assertThat(validDay29).isEqualTo("07:00-21:00");
        //Assertions.assertThat(validLastDayOfMonth).isEqualTo("07:00-18:00");
        //Assertions.assertThat(invalidLastDayOfMonth).isEqualTo("00:00-00:00");
        Assertions.assertThat(invalidTimeTodaysDate).isEqualTo("15:00-22:00");

    }

    @Test
    void getOpeninghoursGroup(){
            //Arrange
            OpeningHoursRuleEntity rule1 = new OpeningHoursRuleEntity().setRule("24.12.1999 ? 1-5 09:00-14:00");
            OpeningHoursRuleEntity rule2 = new OpeningHoursRuleEntity().setRule("24.12.1999 ? 1-5 09:00-14:00");
            OpeningHoursRuleEntity rule3 = new OpeningHoursRuleEntity().setRule("24.12.1999 ? 1-5 09:00-14:00");
            OpeningHoursRuleEntity rule4 = new OpeningHoursRuleEntity().setRule("24.12.1999 ? 1-5 09:00-14:00");
            OpeningHoursRuleEntity rule5 = new OpeningHoursRuleEntity().setRule("24.12.1999 ? 1-5 09:00-14:00");
            OpeningHoursRuleEntity rule6 = new OpeningHoursRuleEntity().setRule("24.12.???? ? 1-5 09:00-15:00");

            //g4
            OpeningHoursGroup group4 = new OpeningHoursGroup().setName("Gruppe4").setRules(List.of(rule4,rule5));

            //g3
            OpeningHoursGroup group3 = new OpeningHoursGroup().setName("Gruppe3").setRules(List.of(rule2,rule3));
            //g2
            OpeningHoursGroup group2 = new OpeningHoursGroup().setName("Gruppe2").setRules(List.of(group3,group4));

            //g1
            OpeningHoursGroup group1 = new OpeningHoursGroup().setName("Gruppe1").setRules(List.of(rule1,group2,rule6));

            //Act
            //Assert
            String openingHoursResult = OpeningHoursParser.getOpeninghours(LocalDate.of(2023,12,24), group1);

            Assertions.assertThat(openingHoursResult).isEqualTo("09:00-15:00");
    }

}