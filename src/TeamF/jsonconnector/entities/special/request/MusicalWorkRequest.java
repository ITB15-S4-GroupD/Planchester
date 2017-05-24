package TeamF.jsonconnector.entities.special.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import TeamF.jsonconnector.entities.MusicalWork;
import TeamF.jsonconnector.entities.Request;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MusicalWorkRequest extends Request<MusicalWork> {
}
