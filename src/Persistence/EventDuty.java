package Persistence;

import Domain.Entities.EventDutyEntity;
import Domain.PresentationModels.EventDutyDTO;
import Utils.DateHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.activation.DataHandler;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by julia on 10.04.2017.
 */
public class EventDuty {

    public static List<EventDutyDTO> getAllEventDuty() {
        Session session = DatabaseHelper.beginSession();
        Query query = session.createQuery("FROM EventDutyEntity");
        List list = query.list();

        List<EventDutyDTO> eventDutyList = new ArrayList<>();
        for(Object o : list) {
            EventDutyEntity eventDuty = (EventDutyEntity) o;
            eventDutyList.add(new EventDutyDTO(eventDuty));
        }
        DatabaseHelper.closeSession(session);
        return eventDutyList;
    }

    public static List<EventDutyDTO> getEventDutyInRange(Calendar start, Calendar end) {
        Session session = DatabaseHelper.beginSession();
        List list = session.createQuery("FROM EventDutyEntity WHERE starttime >= '"
                + DateHelper.convertCalendarToTimestamp(start)
                + "' AND endtime <= '"
                + DateHelper.convertCalendarToTimestamp(end)
                + "'").list();

        List<EventDutyDTO> eventDutyList = new ArrayList<>();
        for(Object o : list) {
            EventDutyEntity eventDuty = (EventDutyEntity) o;
            eventDutyList.add(new EventDutyDTO(eventDuty));
        }
        DatabaseHelper.closeSession(session);
        return eventDutyList;
    }

    public static void createNewEventDuty(EventDutyDTO eventDutyDTO) {
        Session session = DatabaseHelper.beginSession();
        session.save(eventDutyDTO.getEventDuty());
        DatabaseHelper.closeSession(session);
    }
}
