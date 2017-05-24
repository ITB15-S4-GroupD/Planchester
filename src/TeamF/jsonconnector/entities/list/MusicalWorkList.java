package TeamF.jsonconnector.entities.list;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import TeamF.jsonconnector.entities.MusicalWork;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MusicalWorkList implements JSONObjectEntity {
    private List<MusicalWork> _musicalWorkList;

    @JsonGetter("musical_works")
    public List<MusicalWork> getMusicalWorkList() {
        return _musicalWorkList;
    }

    @JsonSetter("musical_works")
    public void setMusicalWorkList(List<MusicalWork> musicalWorkList) {
        _musicalWorkList = musicalWorkList;
    }

    @Override
    public String getEntityName() {
        return "Musical Works";
    }

    @Override
    public String getDisplayName() {
        return getEntityName();
    }
}
