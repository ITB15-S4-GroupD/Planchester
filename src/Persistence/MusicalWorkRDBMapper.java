package Persistence;

import Persistence.Entities.EventDutyEntity;
import Persistence.Entities.MusicalWorkEntity;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by timor on 27.04.2017.
 */
public class MusicalWorkRDBMapper extends Mapper<MusicalWorkEntity> {
    @Override
    Class<MusicalWorkEntity> getEntityClass() {
        return MusicalWorkEntity.class;
    }

    public static List<MusicalWorkEntity> getAllMusicalWorks() {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        List list = session.createQuery("FROM MusicalWorkEntity").list();
        DatabaseConnectionHandler.getInstance().commitTransaction();
        return list;
    }
}