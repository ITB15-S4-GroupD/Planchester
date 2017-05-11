package Utils;

import java.sql.Timestamp;
import java.time.*;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by julia on 05.04.2017.
 */
public class DateHelper {
    public static Calendar convertTimestampToCalendar(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        return calendar;
    }

    public static Timestamp convertCalendarToTimestamp(Calendar calendar) {
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static Timestamp mergeDateAndTime(LocalDate date, LocalTime time) {
        if(date == null || time == null) {
            return null;
        }
        return Timestamp.valueOf(LocalDateTime.of(date, time));
    }

    public static Calendar getStartOfWeek(Calendar dayOfWeek) {
        return getStartOfWeek(dayOfWeek, Calendar.MONDAY);
    }

    /**
     * @param  dayOfWeek            date in the week you would like to get the start date from
     * @param  weekStartDayIndex    index of the day the weeks starts at. ATTENTION: sunday = 1, monday = 2, ...
     * @return                      date of the first day of week
     */
    public static Calendar getStartOfWeek(Calendar dayOfWeek, int weekStartDayIndex) {
        Calendar startDay = (Calendar) dayOfWeek.clone();
        while (startDay.get(Calendar.DAY_OF_WEEK) != weekStartDayIndex) {
            startDay.add(Calendar.DATE, -1);
        }
        startDay.set(Calendar.HOUR_OF_DAY, 0);
        startDay.set(Calendar.MINUTE, 00);
        startDay.set(Calendar.SECOND, 00);
        startDay.set(Calendar.MILLISECOND, 000);
        return startDay;
    }

    public static Calendar getEndOfWeek(Calendar dayOfWeek) {
        return getEndOfWeek(dayOfWeek, Calendar.SUNDAY);
    }

    /**
     * @param  dayOfWeek            date in the week you would like to get the end date from
     * @param  weekStartDayIndex    index of the day the weeks ends at. ATTENTION: sunday = 1, monday = 2, ...
     * @return                      date of the last day of week
     */
    public static Calendar getEndOfWeek(Calendar dayOfWeek, int weekStartDayIndex) {
        Calendar endDay = (Calendar) dayOfWeek.clone();
        while (endDay.get(Calendar.DAY_OF_WEEK) != weekStartDayIndex) {
            endDay.add(Calendar.DATE, +1);
        }
        endDay.set(Calendar.HOUR_OF_DAY, 23);
        endDay.set(Calendar.MINUTE, 59);
        endDay.set(Calendar.SECOND, 59);
        endDay.set(Calendar.MILLISECOND, 999);
        return endDay;
    }
}