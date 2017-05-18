package TeamF.Domain.helper;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class DateTimeHelper {
    public static boolean takesPlaceInFuture(LocalDateTime time) {
        LocalDateTime now = LocalDateTime.now();

        if (!time.isBefore(now)) {
            return true;
        }

        return false;
    }

    public static boolean compareDates(LocalDateTime starttime, LocalDateTime endtime) {
        if (!endtime.isBefore(starttime)) {
            return true;
        }

        return false;
    }

    public static boolean compareRehearsalDate(LocalDateTime starttimeEvent, LocalDateTime endtimeRehearsal) {
        if ((!endtimeRehearsal.isAfter(starttimeEvent))) {
            return true;
        }

        return false;
    }

   public static Boolean periodExpired(LocalDateTime starttime) {
        LocalDateTime fromDateTime = starttime;
        LocalDateTime nowTime = LocalDateTime.now();

       Calendar month1 = new GregorianCalendar(nowTime.getYear(),nowTime.getMonthValue(),nowTime.getDayOfMonth());
       Calendar month2 = new GregorianCalendar(nowTime.getYear(),nowTime.getMonthValue()+1,nowTime.getDayOfMonth());

       int daysMont1 = month1.getActualMaximum(Calendar.DAY_OF_MONTH);
       int daysMont2 = month2.getActualMaximum(Calendar.DAY_OF_MONTH);
       int limit=daysMont1+daysMont2;

       Calendar cal = Calendar.getInstance();
       int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
       int restOfMonth=days-nowTime.getDayOfMonth();

       LocalDateTime toDatetime=LocalDateTime.of(nowTime.getYear(),nowTime.getMonthValue(),nowTime.getDayOfMonth()+restOfMonth,nowTime.getHour(),nowTime.getMinute(),nowTime.getSecond());

       Instant instant1 = toDatetime.atZone(ZoneId.systemDefault()).toInstant();
       Date dateTo = Date.from(instant1);


       Instant instant2 = fromDateTime.atZone(ZoneId.systemDefault()).toInstant();
       Date dateFrom = Date.from(instant2);

       long diff = dateFrom.getTime() - dateTo.getTime();

       if((TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)>limit)){
            return true;
        } else {
            return false;
        }

    }
}



