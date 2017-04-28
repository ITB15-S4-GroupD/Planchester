package Persistence;

import Persistence.Entities.EventDutyMusicalWorkEntity;


/**
 * Created by timor on 27.04.2017.
 */
public class EventDutyMusicalWorkRDBMapper extends Mapper<EventDutyMusicalWorkEntity> {
    @Override
    Class<EventDutyMusicalWorkEntity> getEntityClass() {
        return EventDutyMusicalWorkEntity.class;
    }

}