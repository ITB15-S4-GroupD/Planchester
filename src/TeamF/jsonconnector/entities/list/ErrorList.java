package TeamF.jsonconnector.entities.list;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import TeamF.jsonconnector.entities.Error;
import TeamF.jsonconnector.entities.Pair;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorList<T extends JSONObjectEntity> implements JSONObjectEntity {
    private List<Pair<T, List<Error>>> _errors;

    @JsonGetter("errors")
    public List<Pair<T, List<Error>>> getKeyValueList() {
        return _errors;
    }

    @JsonSetter("errors")
    public void setErrorList(List<Pair<T, List<Error>>> errors) {
        _errors = errors;
    }

    @Override
    public String getEntityName() {
        return "Error List";
    }

    @Override
    public String getDisplayName() {
        return getEntityName();
    }
}