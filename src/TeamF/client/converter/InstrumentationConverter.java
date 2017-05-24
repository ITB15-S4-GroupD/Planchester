package TeamF.client.converter;

import TeamF.jsonconnector.entities.Instrumentation;
import TeamF.jsonconnector.entities.Pair;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;
import TeamF.jsonconnector.entities.Error;

import java.util.ArrayList;
import java.util.List;

public class InstrumentationConverter {
    public static List<Pair<JSONObjectEntity, List<Error>>> getAbstractList(List<Pair<Instrumentation, List<Error>>> errorList) {
        List<Pair<JSONObjectEntity, List<Error>>> resultErrorList = new ArrayList<>(errorList.size());
        Pair<JSONObjectEntity, List<Error>> tmpPair;

        for(Pair<Instrumentation, List<Error>> item : errorList) {
            tmpPair = new Pair<>();
            tmpPair.setKey(item.getKey());
            tmpPair.setValue(item.getValue());
            resultErrorList.add(tmpPair);
        }

        return resultErrorList;
    }
}
