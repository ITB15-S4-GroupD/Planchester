package TeamF.jsonconnector.entities.special.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import TeamF.jsonconnector.entities.Publish;
import TeamF.jsonconnector.entities.Request;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PublishRequest extends Request<Publish> {
}
