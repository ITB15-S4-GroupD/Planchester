package Application;

import Domain.PresentationModels.EventDutyDTO;
import Persistence.EventDuty;
import Utils.DateHelper;

import java.time.Month;
import java.util.Calendar;
import java.util.List;

/**
 * Created by timorzipa on 06/04/2017.
 */
public class EventSchedule {

    public static void publish(Month month) {

    }

    public static void createOpera(EventDutyDTO eventDuty) {
        EventDuty.createNewEventDuty(eventDuty);
    }

    public static void createTour(EventDutyDTO eventDuty) {

    }

    public static void createHofkapelle(EventDutyDTO eventDuty) {

    }

    public static void createConcert(EventDutyDTO eventDuty) {

    }

    public static void update(EventDutyDTO eventDuty) {

    }

    public static void delete(EventDutyDTO eventDuty) {

    }

    public static List<EventDutyDTO> getEventDutyForActualWeek() {
        return getEventDutyInRange(DateHelper.getStartOfWeek(Calendar.getInstance()), DateHelper.getEndOfWeek(Calendar.getInstance()));
    }

    public static List<EventDutyDTO> getEventDutyInRange(Calendar start, Calendar end) {
        return EventDuty.getEventDutyInRange(start, end);
    }

    //todo julia: just for tests, final should be getAllEventDutyInBetween, never getAll()!
    public static List<EventDutyDTO> getAllEventDuty() {
        return EventDuty.getAllEventDuty();
    }
}
