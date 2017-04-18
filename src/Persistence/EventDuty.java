package Persistence;

import Domain.Entities.EventDutyEntity;
import Domain.Models.EventDutyModel;
import Utils.DateHelper;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by julia on 10.04.2017.
 */
public class EventDuty {

    public static void insertEventDuty(EventDutyModel eventDutyModel) {
        Session session = DatabaseHelper.beginSession();
        session.save(eventDutyModel.getEventDutyEntity());
        DatabaseHelper.closeSession(session);
    }

    public static List<EventDutyModel> getAllEventDuty() {
        Session session = DatabaseHelper.beginSession();
        Query query = session.createQuery("FROM EventDutyEntity");
        List list = query.list();

        List<EventDutyModel> eventDutyList = new ArrayList<EventDutyModel>();
        for(Object o : list) {
            EventDutyEntity eventDutyEntity = (EventDutyEntity) o;
            eventDutyList.add(new EventDutyModel(eventDutyEntity));
        }
        DatabaseHelper.closeSession(session);
        return eventDutyList;
    }

    public static List<EventDutyModel> getEventDutyInRange(Calendar start, Calendar end) {
        Session session = DatabaseHelper.beginSession();
        List list = session.createQuery("FROM EventDutyEntity WHERE starttime >= '"
                + DateHelper.convertCalendarToTimestamp(start)
                + "' AND endtime <= '"
                + DateHelper.convertCalendarToTimestamp(end)
                + "'").list();

        List<EventDutyModel> eventDutyList = new ArrayList<EventDutyModel>();
        for(Object o : list) {
            EventDutyEntity eventDutyEntity = (EventDutyEntity) o;
            eventDutyList.add(new EventDutyModel(eventDutyEntity));
        }
        DatabaseHelper.closeSession(session);
        return eventDutyList;
    }
}