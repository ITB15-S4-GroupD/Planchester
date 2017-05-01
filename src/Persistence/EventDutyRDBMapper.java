package Persistence;

import Persistence.Entities.EventDutyEntity;
import Utils.DateHelper;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

/**
 * Created by julia on 10.04.2017.
 */
public class EventDutyRDBMapper extends Mapper<EventDutyEntity> {
    @Override
    Class<EventDutyEntity> getEntityClass() {
        return EventDutyEntity.class;
    }

    public static List<EventDutyEntity> getEventDutyInRange(Calendar start, Calendar end) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        List list = session.createQuery("FROM EventDutyEntity WHERE starttime >= '"
                + DateHelper.convertCalendarToTimestamp(start)
                + "' AND endtime <= '"
                + DateHelper.convertCalendarToTimestamp(end)
                + "'").list();
        DatabaseConnectionHandler.getInstance().commitTransaction();
        return list;
    }

    public static List<EventDutyEntity> getAllEventDutiesInRange(Calendar start, Calendar end) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();

        List<EventDutyEntity> list = session.createQuery("FROM EventDutyEntity " +
                "WHERE (starttime <= '"
                + DateHelper.convertCalendarToTimestamp(end)
                + "' AND endtime >= '"
                + DateHelper.convertCalendarToTimestamp(start)
                + "')").list();
        DatabaseConnectionHandler.getInstance().commitTransaction();
        return list;
    }

    public static List<EventDutyEntity> getEventDutyByDetails (String eventDutyName, Timestamp eventDutyStarttime) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        List<EventDutyEntity> list = session.createQuery("FROM EventDutyEntity WHERE (name = '"
                + eventDutyName + "' AND starttime = '"
                + eventDutyStarttime + "')" ).list();
        DatabaseConnectionHandler.getInstance().commitTransaction();
        return list;
    }

    public List<EventDutyEntity> getAllRehearsalsOfEventDuty(Integer eventDutyID) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        List<EventDutyEntity> list = session.createQuery("FROM EventDutyEntity WHERE (rehearsalFor = " + eventDutyID + ")").list();
        DatabaseConnectionHandler.getInstance().commitTransaction();
        return list;
    }
}