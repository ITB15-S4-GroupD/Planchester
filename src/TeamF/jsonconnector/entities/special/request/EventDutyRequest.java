package TeamF.jsonconnector.entities.special.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import TeamF.jsonconnector.entities.EventDuty;
import TeamF.jsonconnector.entities.Request;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDutyRequest extends Request<EventDuty> {
}
