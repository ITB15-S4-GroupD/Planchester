package Persistence;

import Persistence.Entities.EventDutyEntity;
import Utils.DateHelper;
import org.hibernate.Session;

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
}