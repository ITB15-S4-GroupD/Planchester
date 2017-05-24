package TeamF.jsonconnector.entities.special.errorlist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import TeamF.jsonconnector.entities.list.ErrorList;
import TeamF.jsonconnector.entities.EventDuty;

/**
 * maps to a specific type (required for Jackson Json API to avoid class cast exception)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDutyErrorList extends ErrorList<EventDuty> {
}