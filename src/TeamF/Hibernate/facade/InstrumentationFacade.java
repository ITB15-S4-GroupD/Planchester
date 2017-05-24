package TeamF.Hibernate.facade;

import TeamF.Hibernate.entities.*;
import TeamF.Hibernate.enums.SectionType;
import TeamF.Hibernate.helper.StoreHelper;
import TeamF.Domain.entities.Instrumentation;
import TeamF.Domain.entities.SpecialInstrumentation;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class InstrumentationFacade extends BaseDatabaseFacade<Instrumentation> {
    public InstrumentationFacade() {
        super();
    }

    public InstrumentationFacade(EntityManager session) {
        super(session);
    }

    /** Function creates Hibernate entity objects for the instrumentation and the wood-, brass-,..instrumentations of it
     * persists all wood-, brass-,..instrumentations
     * sets the ID of the wood-, brass-,..instrumentations into the instrumentation
     * persists the Hibernate entity instrumentationEntity object
     * persist the special instrumentation (for that Domain entity SpecialInstrumentation object is converted to Hibernate entity specialInstrumentationEntity object)
     * store the entities with StoreHelper's storeEntities-method
     * return ID of the Hibernate entity specialInstrumentationEntity object
     *
     * @param instrumentation
     * @return instrumentationEntity.getInstrumentationId()
     */
    private Integer addInstrumentation(Instrumentation instrumentation) {
        EntityManager session = getCurrentSession();
        session.getTransaction().begin();

        // create new Instrumentation entities
        InstrumentationEntity instrumentationEntity = new InstrumentationEntity();
        StringInstrumentationEntity stringInstrumentationEntity = getStringInstrumentationEntityFromInstrumentation(instrumentation);
        WoodInstrumentationEntity woodInstrumentationEntity = getWoodInstrumentationEntityFromInstrumentation(instrumentation);
        BrassInstrumentationEntity brassInstrumentationEntity = getBrassInstrumentationEntityFromInstrumentation(instrumentation);
        PercussionInstrumentationEntity percussionInstrumentationEntity = getPercussionInstrumentationEntityFromInstrumentation(instrumentation);

        // store all child entries in the transaction and get the id
        session.persist(stringInstrumentationEntity);
        session.persist(brassInstrumentationEntity);
        session.persist(woodInstrumentationEntity);
        session.persist(percussionInstrumentationEntity);

        // set the received ids
        instrumentationEntity.setStringInstrumentation(stringInstrumentationEntity.getStringInstrumentationId());
        instrumentationEntity.setBrassInstrumentation(brassInstrumentationEntity.getBrassInstrumentationId());
        instrumentationEntity.setWoodInstrumentation(woodInstrumentationEntity.getWoodInstrumentationId());
        instrumentationEntity.setPercussionInstrumentation(percussionInstrumentationEntity.getPercussionInstrumentationId());

        session.persist(instrumentationEntity);

        // set special-instrumentations
        for (SpecialInstrumentation specialInstrumentation : instrumentation.getSpecial()) {
            SpecialInstrumentationEntity specialInstrumentationEntity;
            specialInstrumentationEntity = convertToSpecialInstrumentationEntity(specialInstrumentation);
            specialInstrumentationEntity.setInstrumentationId(instrumentationEntity.getInstrumentationId());

            session.persist(specialInstrumentationEntity);
        }

        StoreHelper.storeEntities(session);
        return instrumentationEntity.getInstrumentationId();
    }

    public Instrumentation getInstrumentationByID(int id) {
        EntityManager session = getCurrentSession();

        Query query = session.createQuery("from InstrumentationEntity where id = :id");
        query.setParameter("id", id);
        query.setMaxResults(1);

        List<InstrumentationEntity> instrumentations = query.getResultList();
        InstrumentationEntity instrumentationEntity;
        Instrumentation instrumentation = new Instrumentation();

        if (instrumentations.size() > 0) {
            instrumentationEntity = instrumentations.get(0);
            instrumentation = convertToInstrumentation(instrumentationEntity);
        }

        return instrumentation;
    }

    public List<Instrumentation> getInstrumentations() {
        EntityManager session = getCurrentSession();

        Query query = session.createQuery("from InstrumentationEntity");
        List<InstrumentationEntity> instrumentationEntities = query.getResultList();
        List<Instrumentation> instrumentations = new ArrayList<>();

        for (InstrumentationEntity ie : instrumentationEntities) {
            instrumentations.add(convertToInstrumentation(ie));
        }

        return instrumentations;
    }

    /**
     * Function to convert InstrumentationEntity Object to Instrumentation. Returns the Instrumentation after creating and setting Informations from InstrumentationEntity object.
     * @return instrumentation    Instrumentation        returns Instrumentation Object
     */
    protected Instrumentation convertToInstrumentation(InstrumentationEntity entity) {
        Instrumentation instrumentation = new Instrumentation();
        instrumentation.setInstrumentationID(entity.getInstrumentationId());

        BrassInstrumentationEntity bie = getBrassInstrumentationEntity(entity.getBrassInstrumentation());
        WoodInstrumentationEntity wie = getWoodInstrumentationEntity(entity.getWoodInstrumentation());
        StringInstrumentationEntity sie = getStringInstrumentationEntity(entity.getStringInstrumentation());
        PercussionInstrumentationEntity pie = getPercussionInstrumentationEntity(entity.getPercussionInstrumentation());
        List<SpecialInstrumentationEntity> speiList = getSpecialInstrumentationEntities(entity.getInstrumentationId());

        instrumentation.setFlute(wie.getFlute());
        instrumentation.setOboe(wie.getOboe());
        instrumentation.setBassoon(wie.getBassoon());
        instrumentation.setClarinet(wie.getClarinet());

        instrumentation.setTube(bie.getTube());
        instrumentation.setHorn(bie.getHorn());
        instrumentation.setTrombone(bie.getTrombone());
        instrumentation.setTrumpet(bie.getTrumpet());

        instrumentation.setViolin1(sie.getViolin1());
        instrumentation.setViolin2(sie.getViolin2());
        instrumentation.setViola(sie.getViola());
        instrumentation.setViolincello(sie.getViolincello());
        instrumentation.setDoublebass(sie.getDoublebass());

        instrumentation.setKettledrum(pie.getKettledrum());
        instrumentation.setHarp(pie.getHarp());
        instrumentation.setPercussion(pie.getPercussion());

        if (speiList != null) {
            for (SpecialInstrumentationEntity spei : speiList) {
                instrumentation.addToSpecial(spei.getInstrumentationId(), spei.getSpecialInstrument(), spei.getSpecialInstrumentationNumber(), spei.getSectionType().toString());
            }
        }

        return instrumentation;
    }

    private SpecialInstrumentationEntity convertToSpecialInstrumentationEntity(SpecialInstrumentation specialInstrumentation) {
        SpecialInstrumentationEntity specialInstrumentationEntity = new SpecialInstrumentationEntity();

        try {
            specialInstrumentationEntity.setSectionType(SectionType.valueOf(specialInstrumentation.getSectionType()));
        } catch (Exception e) {
        }

        specialInstrumentationEntity.setSpecialInstrument(specialInstrumentation.getSpecialInstrument());
        specialInstrumentationEntity.setSpecialInstrumentationNumber(specialInstrumentation.getSpecialInstrumentCount());

        return specialInstrumentationEntity;
    }

    private WoodInstrumentationEntity getWoodInstrumentationEntityFromInstrumentation (Instrumentation instrumentation) {
        WoodInstrumentationEntity woodInstrumentationEntity = new WoodInstrumentationEntity();

        woodInstrumentationEntity.setFlute(instrumentation.getFlute());
        woodInstrumentationEntity.setOboe(instrumentation.getOboe());
        woodInstrumentationEntity.setClarinet(instrumentation.getClarinet());
        woodInstrumentationEntity.setBassoon(instrumentation.getBassoon());

        return woodInstrumentationEntity;
    }

    private StringInstrumentationEntity getStringInstrumentationEntityFromInstrumentation (Instrumentation instrumentation) {
        StringInstrumentationEntity stringInstrumentationEntity = new StringInstrumentationEntity();

        stringInstrumentationEntity.setViolin1(instrumentation.getViolin1());
        stringInstrumentationEntity.setViolin2(instrumentation.getViolin2());
        stringInstrumentationEntity.setViola(instrumentation.getViola());
        stringInstrumentationEntity.setViolincello(instrumentation.getViolincello());
        stringInstrumentationEntity.setDoublebass(instrumentation.getDoublebass());

        return stringInstrumentationEntity;
    }

    private BrassInstrumentationEntity getBrassInstrumentationEntityFromInstrumentation (Instrumentation instrumentation) {
        BrassInstrumentationEntity brassInstrumentationEntity = new BrassInstrumentationEntity();

        brassInstrumentationEntity.setHorn(instrumentation.getHorn());
        brassInstrumentationEntity.setTrumpet(instrumentation.getTrumpet());
        brassInstrumentationEntity.setTrombone(instrumentation.getTrombone());
        brassInstrumentationEntity.setTube(instrumentation.getTube());

        return brassInstrumentationEntity;
    }

    private PercussionInstrumentationEntity getPercussionInstrumentationEntityFromInstrumentation (Instrumentation instrumentation) {
        PercussionInstrumentationEntity percussionInstrumentationEntity = new PercussionInstrumentationEntity();

        percussionInstrumentationEntity.setKettledrum(instrumentation.getKettledrum());
        percussionInstrumentationEntity.setPercussion(instrumentation.getPercussion());
        percussionInstrumentationEntity.setHarp(instrumentation.getHarp());

        return percussionInstrumentationEntity;
    }

    private WoodInstrumentationEntity getWoodInstrumentationEntity(int woodInstrumentationID) {
        EntityManager session = getCurrentSession();
        Query query = session.createQuery("from WoodInstrumentationEntity where woodInstrumentationId = :id");
        query.setParameter("id", woodInstrumentationID);
        query.setMaxResults(1);

        List<WoodInstrumentationEntity> wie = query.getResultList();

        WoodInstrumentationEntity entity = new WoodInstrumentationEntity();

        if (wie.size() > 0) {
            entity = wie.get(0);
        }

        return entity;
    }

    private StringInstrumentationEntity getStringInstrumentationEntity(int stringInstrumentationID) {
        EntityManager session = getCurrentSession();
        Query query = session.createQuery("from StringInstrumentationEntity where stringInstrumentationId = :id");
        query.setParameter("id", stringInstrumentationID);
        query.setMaxResults(1);

        List<StringInstrumentationEntity> sie = query.getResultList();

        StringInstrumentationEntity entity = new StringInstrumentationEntity();

        if (sie.size() > 0) {
            entity = sie.get(0);
        }

        return entity;
    }

    private PercussionInstrumentationEntity getPercussionInstrumentationEntity(int percussionInstrumentationID) {
        EntityManager session = getCurrentSession();
        Query query = session.createQuery("from PercussionInstrumentationEntity where percussionInstrumentationId = :id");
        query.setParameter("id", percussionInstrumentationID);
        query.setMaxResults(1);

        List<PercussionInstrumentationEntity> pie = query.getResultList();

        PercussionInstrumentationEntity entity = new PercussionInstrumentationEntity();

        if (pie.size() > 0) {
            entity = pie.get(0);
        }

        return entity;
    }

    private List<SpecialInstrumentationEntity> getSpecialInstrumentationEntities(int instrumentationID) {
        EntityManager session = getCurrentSession();
        Query query = session.createQuery("from SpecialInstrumentationEntity where instrumentationId = :id");
        query.setParameter("id", instrumentationID);

        List<SpecialInstrumentationEntity> spei = query.getResultList();

        return spei;
    }

    @Override
    public int add(Instrumentation value) {
        return addInstrumentation(value);
    }

    @Override
    public int update(Instrumentation value) {
        EntityManager session = getCurrentSession();
        session.getTransaction().begin();
        int returnId;

        InstrumentationEntity ie = convertToInstrumentationEntity(value);

        if (ie.getInstrumentationId() > 0){
            ie = session.merge(ie);
            returnId = ie.getInstrumentationId();
        }else{
            returnId = this.add(value);
        }

        StoreHelper.storeEntities(session);
        return returnId;
    }

    private InstrumentationEntity convertToInstrumentationEntity(Instrumentation value) {
        InstrumentationEntity ie = new InstrumentationEntity();

        ie.setInstrumentationId(value.getInstrumentationID());

        StringInstrumentationEntity stringIE = new StringInstrumentationEntity();
        stringIE.setViolin1(value.getViolin1());
        stringIE.setViolin2(value.getViolin2());
        stringIE.setViola(value.getViola());
        stringIE.setViolincello(value.getViolincello());
        stringIE.setDoublebass(value.getDoublebass());
        ie.setStringInstrumentationByStringInstrumentation(stringIE);

        WoodInstrumentationEntity woodIE = new WoodInstrumentationEntity();
        woodIE.setFlute(value.getFlute());
        woodIE.setOboe(value.getOboe());
        woodIE.setClarinet(value.getClarinet());
        woodIE.setBassoon(value.getBassoon());
        ie.setWoodInstrumentationByWoodInstrumentation(woodIE);

        BrassInstrumentationEntity brassIE = new BrassInstrumentationEntity();
        brassIE.setHorn(value.getHorn());
        brassIE.setTrumpet(value.getTrumpet());
        brassIE.setTrombone(value.getTrombone());
        brassIE.setTube(value.getTube());
        ie.setBrassInstrumentationByBrassInstrumentation(brassIE);

        PercussionInstrumentationEntity percussionIE = new PercussionInstrumentationEntity();
        percussionIE.setKettledrum(value.getKettledrum());
        percussionIE.setPercussion(value.getPercussion());
        percussionIE.setHarp(value.getHarp());
        ie.setPercussionInstrumentationByPercussionInstrumentation(percussionIE);

        //SpecialInstrumentationEntity specialIE = new SpecialInstrumentationEntity();
        //specialIE.setSpecialInstrument(value.getSpecialInstrumentation());

        return ie;
    }

    private BrassInstrumentationEntity getBrassInstrumentationEntity(int brassInstrumentationID) {
        EntityManager session = getCurrentSession();
        Query query = session.createQuery("from BrassInstrumentationEntity where brassInstrumentationId = :id");
        query.setParameter("id", brassInstrumentationID);
        query.setMaxResults(1);

        List<BrassInstrumentationEntity> bie = query.getResultList();

        BrassInstrumentationEntity entity = new BrassInstrumentationEntity();

        if (bie.size() > 0) {
            entity = bie.get(0);
        }

        return entity;
    }

    @Override
    public boolean delete(int id) {
        EntityManager session = getCurrentSession();
        session.getTransaction().begin();

        Instrumentation instrumentation = getInstrumentationByID(id);
        session.remove(convertToInstrumentationEntity(instrumentation));

        return StoreHelper.storeEntities(session);
    }
}
