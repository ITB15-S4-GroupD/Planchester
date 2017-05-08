package Persistence;


import Persistence.Entities.DutyDispositionEntity;
import Persistence.Entities.EventDutyEntity;
import Utils.DateHelper;
import org.hibernate.Session;

import java.util.Calendar;
import java.util.List;

/**
 * Created by TheeLenchen on 08.05.2017.
 */
public class DutyDispositionRDBMapper extends Mapper<DutyDispositionEntity>{

    @Override
    Class<DutyDispositionEntity> getEntityClass() {
        return DutyDispositionEntity.class;
    }

    public static List<DutyDispositionEntity> getDutyRosterInRange(Calendar start, Calendar end) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        List list = session.createQuery("SELECT * FROM EventDutyEntity JOIN SectionDutyRosterEntity, EventDutySectionDutyRosterEntity " +
                "WHERE " +
                "EventDutyEntity.eventDutyId = EventDutySectionDutyRosterEntity.eventDuty " +
                "AND SectionDutyRosterEntity.sectionDutyRosterId = EventDutySectionDutyRosterEntity.sectionDutyRoster " +
                "AND starttime >= '"
                + DateHelper.convertCalendarToTimestamp(start)
                + "' AND endtime <= '"
                + DateHelper.convertCalendarToTimestamp(end)+"'").list();
        DatabaseConnectionHandler.getInstance().commitTransaction();
        return list;
    }
}
