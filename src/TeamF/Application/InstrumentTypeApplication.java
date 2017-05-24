package TeamF.Application;

import TeamF.Hibernate.facade.*;
import javax.persistence.EntityManager;
import java.util.List;

public class InstrumentTypeApplication {
    private EntityManager session = SessionFactory.getSession();
    private InstrumentTypeFacade instrumentTypeFacade = new InstrumentTypeFacade(session);

    public InstrumentTypeApplication() { }

    public void closeSession() {
        instrumentTypeFacade.closeSession();
    }

    public List<TeamF.Domain.entities.InstrumentType> getInstrumentTypeList() {
        // @TODO: implement and fix issues
        //return instrumentTypeFacade.getAllInstrumentTypes();
        return null;
    }
}