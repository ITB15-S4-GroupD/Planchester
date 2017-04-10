package Application;

import Domain.PresentationModels.EventDutyDTO;
import Persistence.EventDuty;

import java.time.Month;
import java.util.Date;
import java.util.List;

/**
 * Created by timorzipa on 06/04/2017.
 */
public class EventSchedule {

    public static void publish(Month month){

    }

    public static void createOpera(EventDutyDTO eventDuty){

    }

    public static void createTour(EventDutyDTO eventDuty){

    }

    public static void createHofkapelle(EventDutyDTO eventDuty){

    }

    public static void createConcert(EventDutyDTO eventDuty){

    }

    public static void update(EventDutyDTO eventDuty)
    {

    }

    public static void delete(EventDutyDTO eventDuty){

    }

    public static List<EventDutyDTO> getAllEventDutyInBetween(Date start, Date end){
        return null;
    }

    //todo julia: just for tests, final should be getAllEventDutyInBetween, never getAll()!
    public static List<EventDutyDTO> getAllEventDuty(){
        return EventDuty.getAllEventDuty();
    }
}
