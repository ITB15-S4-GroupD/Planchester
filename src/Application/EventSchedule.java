package Application;

import Domain.Models.EventDutyModel;
import Persistence.EventDuty;
import Utils.DateHelper;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by timorzipa on 06/04/2017.
 */
public class EventSchedule {
    private static Calendar loadedEventsStartdate; //start of the already loaded calendar
    private static Calendar loadedEventsEnddate; //end of the already loaded calendar

    public static void publish(Month month) {
    }

    public static void insertNewEventDutyModel(EventDutyModel eventDutyModel) {
        //eventDutyModel.validate();
        EventDuty.insertNewEventDuty(eventDutyModel.getEventDutyEntity());
    }

    public static void store(EventDutyModel eventDuty) {

    }

    public static void delete(EventDutyModel eventDuty) {

    }

    public static List<EventDutyModel> getEventDutyForCurrentWeek() {
        Calendar today = Calendar.getInstance();
        return getEventDutyInRange(DateHelper.getStartOfWeek(today), DateHelper.getEndOfWeek(today));
    }

    public static List<EventDutyModel> getEventDutyForWeek(Calendar cal) {
        return getEventDutyInRange(DateHelper.getStartOfWeek(cal), DateHelper.getEndOfWeek(cal));
    }

    private static List<EventDutyModel> getEventDutyInRange(Calendar startdayOfWeek, Calendar enddayOfWeek) {
        if(loadedEventsStartdate != null && loadedEventsEnddate != null &&
                loadedEventsStartdate.compareTo(startdayOfWeek) <= 0 && loadedEventsEnddate.compareTo(enddayOfWeek) >= 0) {
            return new ArrayList<EventDutyModel>();
        }
        List<EventDutyModel> eventDuties = EventDuty.getEventDutyInRange(startdayOfWeek, enddayOfWeek);
        setLoadedEventsStartAndEnddate(startdayOfWeek, enddayOfWeek);
        return eventDuties;
    }

    private static void setLoadedEventsStartAndEnddate(Calendar start, Calendar end) {
        if(loadedEventsStartdate == null) {
            loadedEventsStartdate = start;
        } else if(loadedEventsStartdate.after(start)) {
            loadedEventsStartdate = start;
        }

        if(loadedEventsEnddate == null) {
            loadedEventsEnddate = end;
        } else if (loadedEventsEnddate.before((end))) {
            loadedEventsEnddate = end;
        }
    }
}