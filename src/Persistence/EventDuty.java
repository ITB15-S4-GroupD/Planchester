package Persistence;

import Domain.Entities.EventDutyEntity;
import Domain.Models.EventDuty;
import Utils.DateHelper;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by julia on 10.04.2017.
 */
public class TestQueries {

    public static List<EventDuty> getAllEventDuty() {
        Session session = DatabaseHelper.beginSession();
        Query query = session.createQuery("FROM EventDutyEntity");
        List list = query.list();

        List<EventDuty> eventDutyList = new ArrayList<EventDuty>();
        for(Object o : list) {
            EventDutyEntity eventDutyEntity = (EventDutyEntity) o;
            eventDutyList.add(new EventDuty(eventDutyEntity));
        }
        DatabaseHelper.closeSession(session);
        return eventDutyList;
    }

    public static List<EventDuty> getEventDutyInRange(Calendar start, Calendar end) {
        Session session = DatabaseHelper.beginSession();
        List list = session.createQuery("FROM EventDutyEntity WHERE starttime >= '"
                + DateHelper.convertCalendarToTimestamp(start)
                + "' AND endtime <= '"
                + DateHelper.convertCalendarToTimestamp(end)
                + "'").list();

        List<EventDuty> eventDutyList = new ArrayList<EventDuty>();
        for(Object o : list) {
            EventDutyEntity eventDutyEntity = (EventDutyEntity) o;
            eventDutyList.add(new EventDuty(eventDutyEntity));
        }
        DatabaseHelper.closeSession(session);
        return eventDutyList;
    }

    public static void createNewEventDuty(EventDuty eventDuty) {
        Session session = DatabaseHelper.beginSession();
        session.save(eventDuty.getEventDuty());
        DatabaseHelper.closeSession(session);
    }
}
