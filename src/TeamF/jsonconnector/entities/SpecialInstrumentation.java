package TeamF.jsonconnector.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpecialInstrumentation implements JSONObjectEntity {
    private int _specialInstrumentationID;
    private String _sectionType;
    private String _specialInstrumentation;
    private int _specialInstrumentationCount;

    @JsonGetter("id")
    public int getSpecialInstrumentationID() {
        return _specialInstrumentationID;
    }

    @JsonGetter("section_type")
    public String getSectionType() {
        return _sectionType;
    }

    @JsonGetter("special_instrumentation")
    public String getSpecialInstrumentation() {
        return _specialInstrumentation;
    }

    @JsonGetter("special_instrumentation_count")
    public int getSpecialInstrumentCount() {
        return _specialInstrumentationCount;
    }

    @JsonSetter("id")
    public void setSpecialInstrumentationID(int id) {
        _specialInstrumentationID = id;
    }

    @JsonSetter("section_type")
    public void setSectionType(String sectionType) {
        _sectionType = sectionType;
    }

    @JsonSetter("special_instrumentation")
    public void setSpecialInstrumentation(String specialInstrumentation) {
        _specialInstrumentation = specialInstrumentation;
    }

    @JsonSetter("special_instrumentation_count")
    public void setSpecialInstrumentationCount(int specialInstrumentCount) {
        _specialInstrumentationCount = specialInstrumentCount;
    }

    @Override
    public String getEntityName() {
        return "Special Instrumentation";
    }

    @Override
    public String getDisplayName() {
        String text;

        if(getSpecialInstrumentation() != null && getSpecialInstrumentation().length() >= 3) {
            text = getSpecialInstrumentation().substring(0, 3);
        } else {
            text = getSpecialInstrumentation();
        }

        return text + "(" + getSpecialInstrumentCount() + ")";
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
