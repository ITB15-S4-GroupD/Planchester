package TeamF.jsonconnector.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import TeamF.jsonconnector.enums.InstrumentType;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Instrument implements JSONObjectEntity {
    private int _instrumentID;
    private InstrumentType _instrumentType;
    private String _brand;
    private String _model;

    @JsonGetter("id")
    public int getInstrumentID() {
        return _instrumentID;
    }

    @JsonGetter("type")
    public InstrumentType getInstrumentType() {
        return _instrumentType;
    }

    @JsonGetter("brand")
    public String getBrand() {
        return _brand;
    }

    @JsonGetter("model")
    public String getModel() {
        return _model;
    }

    @JsonSetter("type")
    public void setInstrumentType(InstrumentType instrumentType) {
        _instrumentType = instrumentType;
    }

    @JsonSetter("brand")
    public void setBrand(String brand) {
        _brand = brand;
    }

    @JsonSetter("model")
    public void setModel(String model) {
        _model = model;
    }

    @JsonSetter("id")
    public void setInstrumentID(int instrumentID) {
        _instrumentID = instrumentID;
    }

    @Override
    public String getEntityName() {
        return "Instrument";
    }

    @Override
    public String getDisplayName() {
        return getBrand() + " " + getModel();
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
