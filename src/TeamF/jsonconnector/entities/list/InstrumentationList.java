package TeamF.jsonconnector.entities.list;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import TeamF.jsonconnector.entities.Instrumentation;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InstrumentationList implements JSONObjectEntity {
    private List<Instrumentation> _instrumentationList;

    @JsonGetter("instrumentation")
    public List<Instrumentation> getInstrumentationList() {
        return _instrumentationList;
    }

    @JsonSetter("instrumentation")
    public void setInstrumentationList(List<Instrumentation> instrumentationList) {
        _instrumentationList = instrumentationList;
    }

    @Override
    public String getEntityName() {
        return "Instrumentation";
    }

    @Override
    public String getDisplayName() {
        return getEntityName();
    }
}
