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
    void isSaved(){
        //Arrange
        String example = "??.??.???? 07:00-21:00 1-5 ?";
        //Act

        Boolean isSaved = openingTimes.isSaved(example);
        //Assign
        ArrayList<String> retrievedRules = openingTimes.getOpeningTimeRules();

        System.out.println(isSaved);

        System.out.println("Retrieved rules size: " + retrievedRules.size());
        //System.out.println("Retrieved string: " + retrievedRules.get(0));
    }

    @Test
    void openingDateAndTime(){
        //Arrange
        //Act
        //Assign
        System.out.println(new StringBuilder().append("local date and time: ").append(openingTimes.openingDateAndTime()).toString());

    }
}
