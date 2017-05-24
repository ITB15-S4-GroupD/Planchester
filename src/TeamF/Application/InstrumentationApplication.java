package TeamF.Application;

import javafx.util.Pair;
import TeamF.Application.entities.SpecialInstrumentation;
import TeamF.Hibernate.facade.InstrumentationFacade;
import TeamF.Hibernate.facade.SessionFactory;
import TeamF.Domain.entities.Instrumentation;
import TeamF.Domain.enums.EntityType;
import TeamF.Domain.interfaces.DomainEntity;
import TeamF.Domain.logic.DomainEntityManager;
import TeamF.Domain.logic.InstrumentationLogic;

import javax.persistence.EntityManager;
import java.util.LinkedList;
import java.util.List;

public class InstrumentationApplication {
    private EntityManager session = SessionFactory.getSession();
    private InstrumentationFacade instrumentationFacade = new InstrumentationFacade(session);

    public InstrumentationApplication() {
    }

    public void closeSession() {
        instrumentationFacade.closeSession();
    }

    public List<Instrumentation> getInstrumentationList() {
        return instrumentationFacade.getInstrumentations();
    }

    public Pair<DomainEntity, List<Pair<String, String>>> addInstrumentation (int id, Integer violin1, Integer violin2, Integer viola, Integer violincello,
                                                                              Integer doublebass, Integer flute, Integer oboe, Integer clarinet, Integer bassoon, Integer horn, Integer trumpet,
                                                                              Integer trombone, Integer tube, Integer kettledrum, Integer percussion, Integer harp,
                                                                              List<SpecialInstrumentation> specialInstrumentationList) {
        Instrumentation instrumentation = new Instrumentation();
        instrumentation.setInstrumentationID(id);
        instrumentation.setViolin1(violin1);
        instrumentation.setViolin2(violin2);
        instrumentation.setViola(viola);
        instrumentation.setViolincello(violincello);
        instrumentation.setDoublebass(doublebass);
        instrumentation.setFlute(flute);
        instrumentation.setOboe(oboe);
        instrumentation.setClarinet(clarinet);
        instrumentation.setBassoon(bassoon);
        instrumentation.setHorn(horn);
        instrumentation.setTrumpet(trumpet);
        instrumentation.setTrombone(trombone);
        instrumentation.setTube(tube);
        instrumentation.setKettledrum(kettledrum);
        instrumentation.setPercussion(percussion);
        instrumentation.setHarp(harp);

        if(specialInstrumentationList != null) {
            for(SpecialInstrumentation item : specialInstrumentationList) {
                instrumentation.addToSpecial(item.getID(), item.getSpecialInstrumentation(), item.getspecialInstrumentationCount(), item.getSectionType());
            }
        }

        InstrumentationLogic instrumentationLogic = (InstrumentationLogic) DomainEntityManager.getLogic(EntityType.INSTRUMENTATION);
        List<Pair<String, String>> errorList = instrumentationLogic.validate(instrumentation);

        if (errorList.size() > 0) {
            return new Pair<>(instrumentation, errorList);
        }

        Integer resultID = instrumentationFacade.add(instrumentation);
        instrumentation.setInstrumentationID(resultID);

        return new Pair<>(instrumentation, new LinkedList<>());
    }
}
