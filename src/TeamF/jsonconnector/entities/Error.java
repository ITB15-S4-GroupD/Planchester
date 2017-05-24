package TeamF.jsonconnector.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Error implements JSONObjectEntity {
    private String _key;
    private String _value;

    @JsonGetter("key")
    public String getKey() {
        return _key;
    }

    @JsonGetter("value")
    public String getValue() {
        return _value;
    }

    @JsonSetter("key")
    public void setKey(String key) {
        _key = key;
    }

    @JsonSetter("value")
    public void setValue(String value) {
        _value = value;
    }

    @Override
    public String getEntityName() {
        return "Error";
    }

    @Override
    public String getDisplayName() {
        return getKey();
    }
}