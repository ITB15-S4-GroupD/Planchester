package Persistence;

import Persistence.Entities.EventDutyMusicalWorkEntity;
import org.hibernate.Session;


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
}