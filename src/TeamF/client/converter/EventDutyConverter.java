package TeamF.client.converter;

import TeamF.jsonconnector.entities.Error;
import TeamF.jsonconnector.entities.EventDuty;
import TeamF.jsonconnector.entities.Pair;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;

import java.util.ArrayList;
import java.util.List;

public class EventDutyConverter {
    public static List<Pair<JSONObjectEntity, List<Error>>> getAbstractList(List<Pair<EventDuty, List<Error>>> errorList) {
        List<Pair<JSONObjectEntity, List<Error>>> resultErrorList = new ArrayList<>(errorList.size());
        Pair<JSONObjectEntity, List<Error>> tmpPair;

        for(Pair<EventDuty, List<Error>> item : errorList) {
            tmpPair = new Pair<>();
            tmpPair.setKey(item.getKey());
            tmpPair.setValue(item.getValue());
            resultErrorList.add(tmpPair);
        }
        return resultErrorList;
    }
}
