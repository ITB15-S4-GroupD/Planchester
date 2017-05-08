package Persistence;

import Persistence.Entities.*;

import java.util.HashMap;

/**
 * Created by julia on 26.04.2017.
 */
public class PersistanceFacade {
    HashMap<Class, Mapper> mappers = new HashMap<Class, Mapper> ();

    public PersistanceFacade() {
        mappers.put(AccountEntity.class, new AccountRDBMapper());
        mappers.put(BrassInstrumentationEntity.class, new BrassInstrumentationRDBMapper());
        mappers.put(EventDutyMusicalWorkEntity.class, new EventDutyMusicalWorkRDBMapper());
        mappers.put(EventDutyEntity.class , new EventDutyRDBMapper());
        mappers.put(InstrumentEntity.class, new InstrumentationRDBMapper());
        mappers.put(MusicalWorkEntity.class , new MusicalWorkRDBMapper());
        mappers.put(MusicianPartEntity.class, new MusicianPartRDBMapper());
        mappers.put(PercussionInstrumentationEntity.class, new PercussionInstrumentationRDBMapper());
        mappers.put(StringInstrumentationEntity.class, new StringInstrumentationRDBMapper());
        mappers.put(WoodInstrumentationEntity.class, new WoodInstrumentationRDBMapper());
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