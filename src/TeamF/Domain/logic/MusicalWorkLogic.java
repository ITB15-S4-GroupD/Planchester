package TeamF.Domain.logic;

import javafx.util.Pair;
import TeamF.Domain.entities.MusicalWork;
import TeamF.Domain.enums.EntityType;
import TeamF.Domain.enums.properties.MusicalWorkProperty;
import TeamF.Domain.interfaces.EntityLogic;

import java.util.LinkedList;
import java.util.List;

public class MusicalWorkLogic implements EntityLogic<MusicalWork, MusicalWorkProperty> {

    @Override
    public List<Pair<String, String>> validate(MusicalWork object, MusicalWorkProperty... properties) {
        List<Pair<String, String>> resultList = new LinkedList<>();

        for( MusicalWorkProperty property : properties){
            switch (property){
                case CONDUCTOR:
                    if ( object.getComposer() == null) {
                        resultList.add(new Pair<>(String.valueOf(MusicalWorkProperty.CONDUCTOR), "is not inserted"));
                    }

                    break;

                case TITLE:
                    if ( object.getName() == null) {
                        resultList.add(new Pair<>(String.valueOf(MusicalWorkProperty.TITLE), "is not inserted"));
                    }

                    break;

                case INSTRUMENTATION:
                    InstrumentationLogic instrumentationLogic = (InstrumentationLogic) DomainEntityManager.getLogic(EntityType.INSTRUMENTATION);
                    if (!instrumentationLogic.validate(object.getInstrumentation()).isEmpty()) {
                        resultList.add(new Pair<>(String.valueOf(MusicalWorkProperty.INSTRUMENTATION), " all inputs are null!"));
                    }
                    break;
            }
        }
        return resultList;
    }

    @Override
    public List<Pair<String, String>> validate(MusicalWork object) {
        return validate(object, MusicalWorkProperty.values());
    }
}
