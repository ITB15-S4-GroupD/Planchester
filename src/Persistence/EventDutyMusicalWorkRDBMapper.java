package Persistence;

import Persistence.Entities.EventDutyMusicalWorkEntity;
import org.hibernate.Session;

import java.util.List;


/**
 * Created by timor on 27.04.2017.
 */
public class EventDutyMusicalWorkRDBMapper extends Mapper<EventDutyMusicalWorkEntity> {
    @Override
    Class<EventDutyMusicalWorkEntity> getEntityClass() {
        return EventDutyMusicalWorkEntity.class;
    }

    public static void remove(EventDutyMusicalWorkEntity eventDutyMusicalWorkEntity) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        session.delete(eventDutyMusicalWorkEntity);
        DatabaseConnectionHandler.getInstance().commitTransaction();
    }

    public static List<Integer> getAllMusicalWorkIDsByEventID(Integer eventID) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        List<Integer> list = session.createQuery("SELECT musicalWork FROM EventDutyMusicalWorkEntity WHERE eventDuty=" + eventID).list();
        DatabaseConnectionHandler.getInstance().commitTransaction();
        return list;
    }
}