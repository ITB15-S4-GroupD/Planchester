package TeamF.Hibernate.facade;

import Persistence.Entities.InstrumentTypeEntity;
import Persistence.Entities.MusicianPartEntity;
import Persistence.Entities.PartEntity;
import Persistence.Entities.PartTypeEntity;
import TeamF.Domain.enums.InstrumentType;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class InstrumentTypeFacade extends BaseDatabaseFacade<InstrumentType> {
    public InstrumentTypeFacade() {
        super();
    }

    public InstrumentTypeFacade(EntityManager session) {
        super(session);
    }

    /**
     * Function to get all InstrumentTypes. Returns a List of InstrumentTypes
     *
     * @return instrumentTypes      List<InstrumentType>         returns a list of instrument types
     */
    public List<InstrumentType> getAllInstrumentTypes() {
        EntityManager session = getCurrentSession();
        Query query = session.createQuery("from InstrumentTypeEntity");

        List<InstrumentTypeEntity> instrumentTypeEntities = query.getResultList();
        List<InstrumentType> instrumentTypes = new ArrayList<>(instrumentTypeEntities.size());
        InstrumentType instrumentType;

        for (InstrumentTypeEntity entity : instrumentTypeEntities) {
            instrumentType = convertToInstrumentType(entity);
            instrumentTypes.add(instrumentType);
        }

        return instrumentTypes;
    }

    public InstrumentType getInstrumentTypeById(int id) {
        EntityManager session = getCurrentSession();
        Query query = session.createQuery("from InstrumentTypeEntity where instrumentTypeId = :id");
        query.setParameter("id", id);
        query.setMaxResults(1);

        List<InstrumentTypeEntity> instrumentTypeEntities = query.getResultList();
        // @TODO: should be possible
        InstrumentType instrumentType = null;
        //InstrumentType instrumentType = new InstrumentType();

        if (instrumentTypeEntities.size() > 0) {
            InstrumentTypeEntity entity = instrumentTypeEntities.get(0);
            instrumentType = convertToInstrumentType(entity);
        }

        return instrumentType;
    }

    protected List<String> getPlayedInstrumentsByPersonId(int id) {
        EntityManager session = getCurrentSession();

        // prevent SQL injections
        Query musicianPartQuery = session.createQuery("from MusicianPartEntity where musician = :id");
        musicianPartQuery.setParameter("id", id);

        List<MusicianPartEntity> musicianPartEntities = musicianPartQuery.getResultList();
        List<String> parts = new ArrayList<>();

        for (MusicianPartEntity musicianpartEntity : musicianPartEntities) {
            Query partQuery = session.createQuery("from PartEntity where partId = :id");
            partQuery.setParameter("id", musicianpartEntity.getPart());

            List<PartEntity> partEntities = partQuery.getResultList();

            if (!(partEntities.isEmpty())) {
                Query partTypeQuery = session.createQuery("from PartTypeEntity where partTypeId = :id");
                partTypeQuery.setParameter("id", partEntities.get(0).getPartType());
                List<PartTypeEntity> partTypeEntities = partTypeQuery.getResultList();

                if (!partTypeEntities.isEmpty()) {
                    parts.add(partTypeEntities.get(0).getPartType());
                }
            }
        }

        return parts;
    }

    protected Integer getPartIdByPlayedInstrument (InstrumentType instrumentType) {
        Integer partId;

        if (instrumentType.equals(InstrumentType.FIRSTVIOLIN)) {
            partId = 1;
        } else if (instrumentType.equals(InstrumentType.SECONDVIOLIN)) {
            partId = 2;
        } else if (instrumentType.equals(InstrumentType.VIOLA)) {
            partId = 3;
        } else if (instrumentType.equals(InstrumentType.VIOLONCELLO)) {
            partId = 4;
        } else if (instrumentType.equals(InstrumentType.DOUBLEBASS)) {
            partId = 5;
        } else if (instrumentType.equals(InstrumentType.FLUTE)) {
            partId = 17;
        } else if (instrumentType.equals(InstrumentType.OBOE)) {
            partId = 19;
        } else if (instrumentType.equals(InstrumentType.CLARINET)) {
            partId = 20;
        } else if (instrumentType.equals(InstrumentType.BASSOON)) {
            partId = 22;
        } else if (instrumentType.equals(InstrumentType.HORN)) {
            partId = 26;
        } else if (instrumentType.equals(InstrumentType.TRUMPET)) {
            partId = 9;
        } else if (instrumentType.equals(InstrumentType.TROMBONE)) {
            partId = 10;
        } else if (instrumentType.equals(InstrumentType.TUBE)) {
            partId = 13;
        } else if (instrumentType.equals(InstrumentType.KETTLEDRUM)) {
            partId = 14;
        } else if (instrumentType.equals(InstrumentType.PERCUSSION)) {
            partId = 15;
        } else if (instrumentType.equals(InstrumentType.HARP)) {
            partId = 16;
        } else {
            partId = -1;
        }

        return partId;
    }

    /**
     * Function to convert an InstrumentTypeEntity object to an InstrumentType. Returns the InstrumentType after creating and setting information from InstrumentTypeEntity object.
     * @return instrumentType      InstrumentType        returns a instrument type object
     */
    protected InstrumentType convertToInstrumentType(InstrumentTypeEntity instrumentTypeEntity) {
        // @TODO: should not be an enum: read the values from the DB
        // InstrumentType instrumentType = new InstrumentType();
        //InstrumentType.setInstrumentTypeID(instrumentTypeEntity.getInstrumentTypeId());

        InstrumentType instrumentType = InstrumentType.valueOf(instrumentTypeEntity.getInstrumentType());

        return instrumentType;
    }

    @Override
    public int add(InstrumentType value) {
        return 0;
    }

    @Override
    public int update(InstrumentType value) {
        return 0;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}