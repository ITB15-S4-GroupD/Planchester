package Persistence;

import Persistence.Entities.EventDutyEntity;
import Persistence.Entities.InstrumentationEntity;
import Utils.DateHelper;
import org.hibernate.Session;

import java.util.Calendar;
import java.util.List;

/**
 * Created by julia on 10.04.2017.
 */
public class InstrumentationRDBMapper extends Mapper<InstrumentationEntity> {
    @Override
    Class<InstrumentationEntity> getEntityClass() {
        return InstrumentationEntity.class;
    }



    /*

     Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        List<Integer> alternative = session.createQuery("SELECT alternativeInstrumentation FROM EventDutyMusicalWorkEntity WHERE eventDuty=" + eventID).list();

        List<Integer> list = session.createQuery("SELECT musicalWork FROM EventDutyMusicalWorkEntity WHERE eventDuty=" + eventID + " AND alternativeInstrumentation=null").list();

        for (Integer integer : list) {
            List<Integer> add = session.createQuery("SELECT instrumentationId FROM MusicalWorkEntity WHERE musicalWorkId=" + list).list();
            ids.addAll(add);

        }


     */

}