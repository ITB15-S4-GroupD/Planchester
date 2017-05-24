package TeamF.Domain.logic;

import javafx.util.Pair;
import TeamF.Domain.entities.Instrumentation;
import TeamF.Domain.enums.InstrumentType;
import TeamF.Domain.enums.errors.InstrumentationError;
import TeamF.Domain.enums.properties.InstrumentationProperty;
import TeamF.Domain.helper.IntegerHelper;
import TeamF.Domain.interfaces.EntityLogic;

import java.util.LinkedList;
import java.util.List;

public class InstrumentationLogic implements EntityLogic<Instrumentation, InstrumentationProperty> {

    @Override
    public List<Pair<String, String>> validate(Instrumentation instrumentation, InstrumentationProperty... properties) {
        List<Pair<String, String>> resultList = new LinkedList<>();

        boolean allZero = true;

        for (InstrumentationProperty property : properties) {
            if (instrumentation.getByInstrumentType(InstrumentType.valueOf(property.toString())) == null) {
                resultList.add(new Pair<>(String.valueOf(InstrumentType.valueOf(property.toString())), "is empty"));
            } else {
                if (IntegerHelper.isBiggerThanZero(instrumentation.getByInstrumentType(InstrumentType.valueOf(property.toString())))) {
                    allZero = false;
                }
            }
        }

        if (allZero) {
            resultList.add(new Pair<>(String.valueOf(InstrumentationError.ALLZERO.toString()), "All inputs are 0!"));
        }

        return resultList;
    }

    @Override
    public List<Pair<String, String>> validate(Instrumentation instrumentation) {
        return validate(instrumentation, InstrumentationProperty.values());
    }
}
