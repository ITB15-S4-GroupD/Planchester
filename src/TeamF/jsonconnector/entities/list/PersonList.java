package TeamF.jsonconnector.entities.list;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import TeamF.jsonconnector.entities.Person;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonList implements JSONObjectEntity {
    private List<Person> _personList;

    @JsonGetter("persons")
    public List<Person> getPersonList() {
        return _personList;
    }

    @JsonSetter("persons")
    public void setPersonList(List<Person> personList) {
        _personList = personList;
    }

    @Override
    public String getEntityName() {
        return "Persons";
    }

    @Override
    public String getDisplayName() {
        return getEntityName();
    }
}
