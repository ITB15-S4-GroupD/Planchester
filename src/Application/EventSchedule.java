package Application;

import Domain.Enum.EventStatus;
import Domain.Enum.EventType;
import Domain.Models.EventDutyModel;
import Persistence.EventDuty;
import Presentation.EventSchedule.EventScheduleController;
import Utils.DateHelper;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
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
  
    public static void insertNewOperaPerformance(EventDutyModel eventDutyModel) {
//        eventDuty.prevalidate();
        EventDuty.insertNewEventDuty(eventDutyModel.getEventDutyEntity());
    }

    public static void insertTourEventDuty(EventDutyModel eventDutyModel) {

        EventDutyModel nextTourDay = null;
        LocalDate dateStart = eventDutyModel.getStarttime().toLocalDateTime().toLocalDate();
        LocalDate dateEnd = eventDutyModel.getEndtime().toLocalDateTime().toLocalDate();
        LocalTime timeStart = LocalTime.of(0,0);
        LocalTime timeEnd = LocalTime.of(23, 59);
        LocalDate current = dateStart;

        while( !current.isAfter(dateEnd) ){
            eventDutyModel.setStarttime(DateHelper.mergeDateAndTime(current, timeStart));
            eventDutyModel.setEndtime(DateHelper.mergeDateAndTime(current, timeEnd));
            EventDuty.insertNewEventDuty(eventDutyModel.getEventDutyEntity());
            EventScheduleController.addEventDutyToGUI(eventDutyModel); // add event to agenda
            current = LocalDate.parse(current.toString()).plusDays(1);
        }

        EventScheduleController.resetSideContent(); // remove content of sidebar
        EventScheduleController.setDisplayedLocalDateTime(eventDutyModel.getStarttime().toLocalDateTime()); // set agenda view to week of created event
        EventScheduleController.setSelectedAppointment(eventDutyModel); // select last created appointment
    }
    public static void insertHofkapelleEventDuty(EventDutyModel eventDutyModel) {
        EventDuty.insertNewEventDuty(eventDutyModel.getEventDutyEntity());
    }

    public static void insertConcertEventDuty(EventDutyModel eventDutyModel) {
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