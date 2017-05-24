package TeamF.Application.helper;

import javafx.util.Pair;
import TeamF.Application.EventApplication;
import TeamF.Application.InstrumentationApplication;
import TeamF.Domain.entities.Instrumentation;
import TeamF.Domain.entities.MusicalWork;

import java.util.List;

public class DomainEntityHelper {
    public static List<MusicalWork> getMusicalWorkList() {
        EventApplication facade = new EventApplication();
        List<MusicalWork> musicalWorkList = facade.getMusicalWorkList();

        return musicalWorkList;
    }

    public static List<Instrumentation> getInstrumentationList() {
        InstrumentationApplication facade = new InstrumentationApplication();
        List<Instrumentation> instrumentationList = facade.getInstrumentationList();

        return instrumentationList;
    }

    public static List<Pair<MusicalWork, Instrumentation>> getMusicalWorkInstrumentationList() {
        EventApplication facade = new EventApplication();
        List<Pair<MusicalWork, Instrumentation>> musicalWorkInstrumentation = facade.getMusicalWorkInstrumentationList();

        return musicalWorkInstrumentation;
    }
}
