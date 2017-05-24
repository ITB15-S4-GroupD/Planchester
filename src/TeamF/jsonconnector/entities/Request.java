package TeamF.jsonconnector.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import TeamF.jsonconnector.enums.request.ActionType;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Request<T extends JSONObjectEntity> implements JSONObjectEntity {
    private ActionType _actionType;
    private List<Pair<String, String>> _parameterKeyValueList;
    private T _entity;

    @JsonGetter("action_type")
    public ActionType getActionType() {
        return _actionType;
    }

    @JsonGetter("parameter_key_value_list")
    public List<Pair<String, String>> getParameterValueList() {
        return _parameterKeyValueList;
    }

    @JsonGetter("entity")
    public T getEntity() {
        return _entity;
    }

    @JsonSetter("action_type")
    public void setActionType(ActionType actionType) {
        _actionType = actionType;
    }

    @JsonSetter("parameter_key_value_list")
    public void setParameterKeyList(List<Pair<String, String>> parameterKeyValueList) {
        _parameterKeyValueList = parameterKeyValueList;
    }

    @JsonGetter("entity")
    public void setEntity(T entity) {
        _entity = entity;
    }

    @Override
    public String getEntityName() {
        return "Request";
    }

    @Override
    public String getDisplayName() {
        return getEntityName();
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
