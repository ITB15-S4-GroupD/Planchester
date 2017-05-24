package TeamF.jsonconnector.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InstrumentType implements JSONObjectEntity {
    private int _id;
    private String _name;

    @JsonGetter("id")
    public int getID() {
        return _id;
    }

    @JsonGetter("name")
    public String getName() {
        return _name;
    }

    @JsonSetter("id")
    public void setID(int id) {
        _id = id;
    }

    @JsonSetter("name")
    public void setName(String name) {
        _name = name;
    }

    @Override
    public String getEntityName() {
        return "Instrument Type";
    }

    @Override
    public String getDisplayName() {
        return getName();
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}