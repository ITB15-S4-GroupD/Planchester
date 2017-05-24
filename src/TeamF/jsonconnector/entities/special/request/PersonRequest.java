package TeamF.jsonconnector.entities.special.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import TeamF.jsonconnector.entities.Person;
import TeamF.jsonconnector.entities.Request;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonRequest extends Request<Person> {
}
