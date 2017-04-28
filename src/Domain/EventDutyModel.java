package Domain;

import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import Utils.Validator;
import javax.xml.bind.ValidationException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by julia on 13.04.2017.
 */
public class EventDutyModel {
    private Integer eventDutyId;
    private String name;
    private String description;
    private Timestamp starttime;
    private Timestamp endtime;
    private String eventType;
    private String eventStatus;
    private String conductor;
    private String location;
    private Double defaultPoints;
    private Integer rehearsalFor;
    private Integer instrumentation;
    private List<MusicalWorkModel> musicalWorks;

    public EventDutyModel() { }

    public void validate() throws ValidationException {
        Validator.validateMandatoryString(name, 255);
        Validator.validateString(description, 255);
        Validator.validateMandatoryTimestamp(starttime);
        Validator.validateTimestampAfterToday(starttime);
        Validator.validateMandatoryTimestamp(endtime);
        Validator.validateTimestampAfterToday(endtime);
        Validator.validateTimestamp1BeforeTimestamp2(starttime, endtime);
        if(eventType == null || !EventType.contains(eventType)) {
            throw new ValidationException("Eventtype wrong");
        }
        if(eventStatus == null || !EventStatus.contains(eventStatus)) {
            throw new ValidationException("Eventstatus wrong");
        }
        Validator.validateString(conductor, 25);
        Validator.validateString(location, 255);
        Validator.validateDouble(defaultPoints, 0, Double.MAX_VALUE);
    }

    public Integer getEventDutyId() {
        return eventDutyId;
    }

    public void setEventDutyId(Integer eventDutyId) {
        this.eventDutyId = eventDutyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getStarttime() {
        return starttime;
    }

    public void setStarttime(Timestamp starttime) {
        this.starttime = starttime;
    }

    public Timestamp getEndtime() {
        return endtime;
    }

    public void setEndtime(Timestamp endtime) {
        this.endtime = (endtime == null ? Timestamp.valueOf(starttime.toLocalDateTime().plusHours(2)) : endtime);
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getDefaultPoints() {
        return defaultPoints;
    }

    public void setDefaultPoints(Double defaultPoints) {
        this.defaultPoints = defaultPoints;
    }

    public Integer getRehearsalFor() {
        return rehearsalFor;
    }

    public void setRehearsalFor(Integer rehearsalFor) {
        this.rehearsalFor = rehearsalFor;
    }

    public Integer getInstrumentation() {
        return instrumentation;
    }

    public void setInstrumentation(Integer instrumentation) {
        this.instrumentation = instrumentation;
    }

    public List<MusicalWorkModel> getMusicalWorks() {
        return musicalWorks;
    }

    public void setMusicalWorks(List<MusicalWorkModel> musicalWorks) {
        this.musicalWorks = musicalWorks;
    }
}