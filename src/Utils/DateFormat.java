package Utils;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by julia on 05.04.2017.
 */
public class DateFormat {
    public static Calendar convertTimestampToCalendar(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        return calendar;
    }
}