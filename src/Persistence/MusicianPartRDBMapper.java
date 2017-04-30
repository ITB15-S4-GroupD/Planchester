package Persistence;

import Persistence.Entities.EventDutyEntity;
import Persistence.Entities.MusicianPartEntity;
import Utils.DateHelper;
import org.hibernate.Session;

import java.util.Calendar;
import java.util.List;

/**
 * Created by julia on 10.04.2017.
 */
public class MusicianPartRDBMapper extends Mapper<MusicianPartEntity> {
    @Override
    Class<MusicianPartEntity> getEntityClass() {
        return MusicianPartEntity.class;
    }

    public static Integer getCountedMusicians(String instrumentFilter) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        Long count = (Long) session.createQuery("select count(*) " +
                "from MusicianPartEntity a " +
                "join PartEntity b on a.part = b.partId " +
                "join PartTypeEntity c ON b.partType = c.partTypeId " +
                "where c.partType like '"
                + instrumentFilter
                + "'").getSingleResult();
        DatabaseConnectionHandler.getInstance().commitTransaction();
        return ((Number) count).intValue();
    }
}