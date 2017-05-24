package TeamF.jsonconnector.entities.special.errorlist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import TeamF.jsonconnector.entities.Instrumentation;
import TeamF.jsonconnector.entities.list.ErrorList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InstrumentationErrorList extends ErrorList<Instrumentation> {
}
