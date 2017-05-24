package TeamF.jsonconnector.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import TeamF.jsonconnector.enums.PublishType;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Publish implements JSONObjectEntity {
    private PublishType _publishType;
    private int _month;
    private int _year;

    @JsonGetter("type")
    public PublishType getPublishType() {
        return _publishType;
    }

    @JsonGetter("month")
    public int getMonth() {
        return _month;
    }

    @JsonGetter("year")
    public int getYear() {
        return _year;
    }

    @JsonSetter("type")
    public void setPublishType(PublishType publishType) {
        _publishType = publishType;
    }

    @JsonSetter("month")
    public void setMonth(int month) {
        _month = month;
    }

    @JsonSetter("year")
    public void setYear(int year) {
        _year = year;
    }

    @Override
    public String getEntityName() {
        return "Publish";
    }

    @Override
    public String getDisplayName() {
        return getEntityName();
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
