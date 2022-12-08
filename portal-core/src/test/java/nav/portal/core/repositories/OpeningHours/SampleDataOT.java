package nav.portal.core.repositories.OpeningHours;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SampleDataOT {
    static final ArrayList<String> weekdayOpeningTimes = new ArrayList<>(Arrays.asList("??.??.???? 09:00-23:00 1-5 ?", "??.??.???? 06:00-18:00 2-3 ?", "??.??.???? 12:55-21:30 0-0 ?",
            "??.??.???? 05:30-21:30 1-9 ?", "??.??.???? 99:77-21:00 1-4 ?", "??.??.???? 03:00-21:79 1-3 ?", "??.??.???? 03:00-21:79 1-3 ?", "??.??.???? 08:00-22:00 3-5 ?"));

    public static String getRandomizedWeekdayOpeningTimes() {
        return getRandomFromArray(weekdayOpeningTimes);
    }

    private static String getRandomFromArray(ArrayList<String> array) {
        if (array.size() == 0) {
            //Hit skal man ikke komme
            return null;
        }
        Random random = new Random();
        return array.get(random.nextInt(array.size()));
    }
}
