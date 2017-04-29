package Persistence;

import Persistence.Entities.EventDutyEntity;
import Persistence.Entities.EventDutyMusicalWorkEntity;
import Persistence.Entities.MusicalWorkEntity;
import Persistence.Entities.InstrumentEntity;

import java.util.HashMap;

/**
 * Created by julia on 26.04.2017.
 */
public class PersistanceFacade {
    HashMap<Class, Mapper> mappers = new HashMap<Class, Mapper> ();

    public PersistanceFacade() {
        mappers.put(EventDutyEntity.class , new EventDutyRDBMapper());
        mappers.put(MusicalWorkEntity.class , new MusicalWorkRDBMapper());
        mappers.put(EventDutyMusicalWorkEntity.class, new EventDutyMusicalWorkRDBMapper());
        mappers.put(InstrumentEntity.class, new InstrumentationRDBMapper());
    }

    public Object get(int oid, Class persistenceClass) {
        Mapper mapper = mappers.get(persistenceClass);
        return mapper.get(oid);
    }
    public Object put(Object obj) {
        Mapper mapper = mappers.get(obj.getClass());
        return mapper.put(obj);
    }

    public void remove(int oid, Class persistenceClass) {
        Mapper mapper = mappers.get(persistenceClass);
        mapper.remove(oid);
    }

    public Mapper getMapper(Class persistenceClass) {
        return mappers.get(persistenceClass);
    }
}