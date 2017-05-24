package TeamF.Application;

import Persistence.Entities.*;
import Persistence.PersistanceFacade;
import javafx.util.Pair;
import TeamF.Application.entities.SpecialInstrumentation;
import TeamF.Hibernate.facade.MusicalWorkFacade;
import TeamF.Domain.entities.Instrumentation;
import TeamF.Domain.entities.MusicalWork;
import TeamF.Domain.enums.EntityType;
import TeamF.Domain.interfaces.DomainEntity;
import TeamF.Domain.logic.DomainEntityManager;
import TeamF.Domain.logic.MusicalWorkLogic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MusicalWorkApplication {

    PersistanceFacade<MusicalWorkEntity> musicalWorkEntityPersistanceFacade = new PersistanceFacade<>(MusicalWorkEntity.class);

    public Pair<MusicalWorkEntity, List<Pair<String, String>>> addMusicalWork(TeamF.jsonconnector.entities.MusicalWork musicalWork) {
        MusicalWorkEntity musicalWorkEntity = new MusicalWorkEntity();
        musicalWorkEntity.setMusicalWorkId(musicalWork.getMusicalWorkID());
        musicalWorkEntity.setName(musicalWork.getName());
        musicalWorkEntity.setComposer(musicalWork.getComposer());

        StringInstrumentationEntity stringInstrumentationEntity = new StringInstrumentationEntity();
        stringInstrumentationEntity.setViolin1(musicalWork.getInstrumentation().getViolin1());
        stringInstrumentationEntity.setViolin2(musicalWork.getInstrumentation().getViolin2());
        stringInstrumentationEntity.setViola(musicalWork.getInstrumentation().getViola());
        stringInstrumentationEntity.setVioloncello(musicalWork.getInstrumentation().getViolincello());
        stringInstrumentationEntity.setDoublebass(musicalWork.getInstrumentation().getDoublebass());

        PersistanceFacade<StringInstrumentationEntity> stringInstrumentationEntityPersistanceFacade = new PersistanceFacade<>(StringInstrumentationEntity.class);
        stringInstrumentationEntity = stringInstrumentationEntityPersistanceFacade.put(stringInstrumentationEntity);

        WoodInstrumentationEntity woodInstrumentationEntity = new WoodInstrumentationEntity();
        woodInstrumentationEntity.setFlute(musicalWork.getInstrumentation().getFlute());
        woodInstrumentationEntity.setOboe(musicalWork.getInstrumentation().getOboe());
        woodInstrumentationEntity.setClarinet(musicalWork.getInstrumentation().getClarinet());
        woodInstrumentationEntity.setBassoon(musicalWork.getInstrumentation().getBassoon());

        PersistanceFacade<WoodInstrumentationEntity> woodInstrumentationEntityPersistanceFacade = new PersistanceFacade<>(WoodInstrumentationEntity.class);
        woodInstrumentationEntity = woodInstrumentationEntityPersistanceFacade.put(woodInstrumentationEntity);

        BrassInstrumentationEntity brassInstrumentationEntity = new BrassInstrumentationEntity();
        brassInstrumentationEntity.setHorn(musicalWork.getInstrumentation().getHorn());
        brassInstrumentationEntity.setTrumpet(musicalWork.getInstrumentation().getTrumpet());
        brassInstrumentationEntity.setTrombone(musicalWork.getInstrumentation().getTrombone());
        brassInstrumentationEntity.setTube(musicalWork.getInstrumentation().getTube());

        PersistanceFacade<BrassInstrumentationEntity> brassInstrumentationEntityPersistanceFacade = new PersistanceFacade<>(BrassInstrumentationEntity.class);
        brassInstrumentationEntity = brassInstrumentationEntityPersistanceFacade.put(brassInstrumentationEntity);

        PercussionInstrumentationEntity percussionInstrumentationEntity = new PercussionInstrumentationEntity();
        percussionInstrumentationEntity.setKettledrum(musicalWork.getInstrumentation().getKettledrum());
        percussionInstrumentationEntity.setPercussion(musicalWork.getInstrumentation().getPercussion());
        percussionInstrumentationEntity.setHarp(musicalWork.getInstrumentation().getHarp());

        PersistanceFacade<PercussionInstrumentationEntity> percussionInstrumentationEntityPersistanceFacade = new PersistanceFacade<>(PercussionInstrumentationEntity.class);
        percussionInstrumentationEntity = percussionInstrumentationEntityPersistanceFacade.put(percussionInstrumentationEntity);

        InstrumentationEntity instrumentationEntity = new InstrumentationEntity();
        instrumentationEntity.setStringInstrumentationByStringInstrumentation(stringInstrumentationEntity);
        instrumentationEntity.setStringInstrumentation(stringInstrumentationEntity.getStringInstrumentationId());

        instrumentationEntity.setWoodInstrumentationByWoodInstrumentation(woodInstrumentationEntity);
        instrumentationEntity.setWoodInstrumentation(woodInstrumentationEntity.getWoodInstrumentationId());

        instrumentationEntity.setBrassInstrumentationByBrassInstrumentation(brassInstrumentationEntity);
        instrumentationEntity.setBrassInstrumentation(brassInstrumentationEntity.getBrassInstrumentationId());

        instrumentationEntity.setPercussionInstrumentationByPercussionInstrumentation(percussionInstrumentationEntity);
        instrumentationEntity.setPercussionInstrumentation(percussionInstrumentationEntity.getPercussionInstrumentationId());

        PersistanceFacade<InstrumentationEntity> instrumentationEntityPersistanceFacade = new PersistanceFacade<>(InstrumentationEntity.class);
        instrumentationEntity = instrumentationEntityPersistanceFacade.put(instrumentationEntity);

        musicalWorkEntity.setInstrumentationId(instrumentationEntity.getInstrumentationId());
        musicalWorkEntity.setInstrumentationByInstrumentationId(instrumentationEntity);

        musicalWorkEntity = musicalWorkEntityPersistanceFacade.put(musicalWorkEntity);
        return new Pair<>(musicalWorkEntity, new LinkedList<>());
    }

    public Pair<MusicalWorkEntity, List<Pair<String, String>>> updateMusicalWork(TeamF.jsonconnector.entities.MusicalWork musicalWork) {
        MusicalWorkEntity musicalWorkEntity = musicalWorkEntityPersistanceFacade.get(musicalWork.getMusicalWorkID());
        musicalWorkEntity.setMusicalWorkId(musicalWork.getMusicalWorkID());
        musicalWorkEntity.setName(musicalWork.getName());
        musicalWorkEntity.setComposer(musicalWork.getComposer());

        StringInstrumentationEntity stringInstrumentationEntity = new StringInstrumentationEntity();
        stringInstrumentationEntity.setViolin1(musicalWork.getInstrumentation().getViolin1());
        stringInstrumentationEntity.setViolin2(musicalWork.getInstrumentation().getViolin2());
        stringInstrumentationEntity.setViola(musicalWork.getInstrumentation().getViola());
        stringInstrumentationEntity.setVioloncello(musicalWork.getInstrumentation().getViolincello());
        stringInstrumentationEntity.setDoublebass(musicalWork.getInstrumentation().getDoublebass());

        PersistanceFacade<StringInstrumentationEntity> stringInstrumentationEntityPersistanceFacade = new PersistanceFacade<>(StringInstrumentationEntity.class);
        stringInstrumentationEntity = stringInstrumentationEntityPersistanceFacade.put(stringInstrumentationEntity);

        WoodInstrumentationEntity woodInstrumentationEntity = new WoodInstrumentationEntity();
        woodInstrumentationEntity.setFlute(musicalWork.getInstrumentation().getFlute());
        woodInstrumentationEntity.setOboe(musicalWork.getInstrumentation().getOboe());
        woodInstrumentationEntity.setClarinet(musicalWork.getInstrumentation().getClarinet());
        woodInstrumentationEntity.setBassoon(musicalWork.getInstrumentation().getBassoon());

        PersistanceFacade<WoodInstrumentationEntity> woodInstrumentationEntityPersistanceFacade = new PersistanceFacade<>(WoodInstrumentationEntity.class);
        woodInstrumentationEntity = woodInstrumentationEntityPersistanceFacade.put(woodInstrumentationEntity);

        BrassInstrumentationEntity brassInstrumentationEntity = new BrassInstrumentationEntity();
        brassInstrumentationEntity.setHorn(musicalWork.getInstrumentation().getHorn());
        brassInstrumentationEntity.setTrumpet(musicalWork.getInstrumentation().getTrumpet());
        brassInstrumentationEntity.setTrombone(musicalWork.getInstrumentation().getTrombone());
        brassInstrumentationEntity.setTube(musicalWork.getInstrumentation().getTube());

        PersistanceFacade<BrassInstrumentationEntity> brassInstrumentationEntityPersistanceFacade = new PersistanceFacade<>(BrassInstrumentationEntity.class);
        brassInstrumentationEntity = brassInstrumentationEntityPersistanceFacade.put(brassInstrumentationEntity);

        PercussionInstrumentationEntity percussionInstrumentationEntity = new PercussionInstrumentationEntity();
        percussionInstrumentationEntity.setKettledrum(musicalWork.getInstrumentation().getKettledrum());
        percussionInstrumentationEntity.setPercussion(musicalWork.getInstrumentation().getPercussion());
        percussionInstrumentationEntity.setHarp(musicalWork.getInstrumentation().getHarp());

        PersistanceFacade<PercussionInstrumentationEntity> percussionInstrumentationEntityPersistanceFacade = new PersistanceFacade<>(PercussionInstrumentationEntity.class);
        percussionInstrumentationEntity = percussionInstrumentationEntityPersistanceFacade.put(percussionInstrumentationEntity);

        InstrumentationEntity instrumentationEntity = new InstrumentationEntity();
        instrumentationEntity.setStringInstrumentationByStringInstrumentation(stringInstrumentationEntity);
        instrumentationEntity.setStringInstrumentation(stringInstrumentationEntity.getStringInstrumentationId());

        instrumentationEntity.setWoodInstrumentationByWoodInstrumentation(woodInstrumentationEntity);
        instrumentationEntity.setWoodInstrumentation(woodInstrumentationEntity.getWoodInstrumentationId());

        instrumentationEntity.setBrassInstrumentationByBrassInstrumentation(brassInstrumentationEntity);
        instrumentationEntity.setBrassInstrumentation(brassInstrumentationEntity.getBrassInstrumentationId());

        instrumentationEntity.setPercussionInstrumentationByPercussionInstrumentation(percussionInstrumentationEntity);
        instrumentationEntity.setPercussionInstrumentation(percussionInstrumentationEntity.getPercussionInstrumentationId());

        PersistanceFacade<InstrumentationEntity> instrumentationEntityPersistanceFacade = new PersistanceFacade<>(InstrumentationEntity.class);
        instrumentationEntity = instrumentationEntityPersistanceFacade.put(instrumentationEntity);

        musicalWorkEntity.setInstrumentationId(instrumentationEntity.getInstrumentationId());
        musicalWorkEntity.setInstrumentationByInstrumentationId(instrumentationEntity);

        musicalWorkEntity = musicalWorkEntityPersistanceFacade.put(musicalWorkEntity);
        return new Pair<>(musicalWorkEntity, new LinkedList<>());
    }

    public void deleteMusicalWork(TeamF.jsonconnector.entities.MusicalWork musicalWork) {
        musicalWorkEntityPersistanceFacade.remove(musicalWork.getMusicalWorkID());
    }

    public List<TeamF.jsonconnector.entities.MusicalWork> getMusicalWorkList(){
        List<TeamF.jsonconnector.entities.MusicalWork> musicalWorks = new ArrayList<>();

        for(MusicalWorkEntity musicalWork : musicalWorkEntityPersistanceFacade.list(null)) {
            TeamF.jsonconnector.entities.MusicalWork newMusicalWork = new TeamF.jsonconnector.entities.MusicalWork();

            TeamF.jsonconnector.entities.Instrumentation newInstrumentation = new TeamF.jsonconnector.entities.Instrumentation();
            newInstrumentation.setBassoon(musicalWork.getInstrumentationByInstrumentationId().getWoodInstrumentationByWoodInstrumentation().getBassoon());
            newInstrumentation.setClarinet(musicalWork.getInstrumentationByInstrumentationId().getWoodInstrumentationByWoodInstrumentation().getClarinet());
            newInstrumentation.setDoublebass(musicalWork.getInstrumentationByInstrumentationId().getStringInstrumentationByStringInstrumentation().getDoublebass());
            newInstrumentation.setViolin1(musicalWork.getInstrumentationByInstrumentationId().getStringInstrumentationByStringInstrumentation().getViolin1());
            newInstrumentation.setFlute(musicalWork.getInstrumentationByInstrumentationId().getWoodInstrumentationByWoodInstrumentation().getFlute());
            newInstrumentation.setHarp(musicalWork.getInstrumentationByInstrumentationId().getPercussionInstrumentationByPercussionInstrumentation().getHarp());
            newInstrumentation.setHorn(musicalWork.getInstrumentationByInstrumentationId().getBrassInstrumentationByBrassInstrumentation().getHorn());
            newInstrumentation.setKettledrum(musicalWork.getInstrumentationByInstrumentationId().getPercussionInstrumentationByPercussionInstrumentation().getKettledrum());
            newInstrumentation.setOboe(musicalWork.getInstrumentationByInstrumentationId().getWoodInstrumentationByWoodInstrumentation().getOboe());
            newInstrumentation.setPercussion(musicalWork.getInstrumentationByInstrumentationId().getPercussionInstrumentationByPercussionInstrumentation().getPercussion());
            newInstrumentation.setViolin2(musicalWork.getInstrumentationByInstrumentationId().getStringInstrumentationByStringInstrumentation().getViolin2());
            newInstrumentation.setTrombone(musicalWork.getInstrumentationByInstrumentationId().getBrassInstrumentationByBrassInstrumentation().getTrombone());
            newInstrumentation.setTrumpet(musicalWork.getInstrumentationByInstrumentationId().getBrassInstrumentationByBrassInstrumentation().getTrumpet());
            newInstrumentation.setTube(musicalWork.getInstrumentationByInstrumentationId().getBrassInstrumentationByBrassInstrumentation().getTube());
            newInstrumentation.setViola(musicalWork.getInstrumentationByInstrumentationId().getStringInstrumentationByStringInstrumentation().getViola());
            newInstrumentation.setViolincello(musicalWork.getInstrumentationByInstrumentationId().getStringInstrumentationByStringInstrumentation().getVioloncello());

            newMusicalWork.setComposer(musicalWork.getComposer());
            newMusicalWork.setInstrumentation(newInstrumentation);
            newMusicalWork.setName(musicalWork.getName());
            newMusicalWork.setMusicalWorkID(musicalWork.getMusicalWorkId());

            musicalWorks.add(newMusicalWork);
        }
        return musicalWorks;
    }
}