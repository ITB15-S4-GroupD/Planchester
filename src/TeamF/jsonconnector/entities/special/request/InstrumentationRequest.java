package TeamF.jsonconnector.entities.special.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import TeamF.jsonconnector.entities.Instrumentation;
import TeamF.jsonconnector.entities.Request;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InstrumentationRequest extends Request<Instrumentation> {
}
