package TeamF.jsonconnector.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MusicalWork implements JSONObjectEntity {
    private int _musicalWorkID;
    private Instrumentation _instrumentation;
    private String _name;
    private String _composer;
    private Instrumentation _alternativeInstrumentationId;

    @JsonGetter("id")
    public int getMusicalWorkID() {
        return _musicalWorkID;
    }

    @JsonGetter("instrumentation")
    public Instrumentation getInstrumentation() {
        return _instrumentation;
    }

    @JsonGetter("name")
    public String getName() {
        return _name;
    }

    @JsonGetter("composer")
    public String getComposer() {
        return _composer;
    }

    @JsonGetter("alternative_instrumentation")
    public Instrumentation getAlternativeInstrumentationId() {
        return _alternativeInstrumentationId;
    }

    @JsonSetter("id")
    public void setMusicalWorkID(int musicalWorkID) {
        _musicalWorkID = musicalWorkID;
    }

    @JsonSetter("instrumentation")
    public void setInstrumentation(Instrumentation instrumentation) {
        _instrumentation = instrumentation;
    }

    @JsonSetter("name")
    public void setName(String name) {
        _name = name;
    }

    @JsonSetter("composer")
    public void setComposer(String composer) {
        _composer = composer;
    }

    @JsonSetter("alternative_instrumentation")
    public void setAlternativeInstrumentationId(Instrumentation alternativeInstrumentationId) {
        _alternativeInstrumentationId = alternativeInstrumentationId;
    }

    @Override
    public String getEntityName() {
        return "Musical Work";
    }

    @Override
    public String getDisplayName() {
        return getName() + " (" + getComposer() + ")";
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
