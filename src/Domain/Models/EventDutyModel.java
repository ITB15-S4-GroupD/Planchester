package Domain.Models;

import Domain.Interfaces.IEventDuty;
import Domain.Interfaces.IInstrumentation;
import Utils.Enum.DutyRosterStatus;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import Utils.Validator;
import javax.xml.bind.ValidationException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by julia on 13.04.2017.
 */
public class EventDutyModel implements IEventDuty {
    private Integer eventDutyId;
    private String name;
    private String description;
    private Timestamp startTime;
    private Timestamp endTime;
    private EventType eventType;
    private EventStatus eventStatus;
    private String conductor;
    private String location;
    private Double defaultPoints;
    private Integer rehearsalFor;
    private IInstrumentation instrumentation;
    private List<MusicalWorkModel> musicalWorks;
    private DutyRosterStatus dutyRosterStatus;
    private Integer instrumentationId;

    public EventDutyModel() { }

    public void validate() throws ValidationException {
        Validator.validateMandatoryString(name, 255);
        Validator.validateString(description, 255);
        Validator.validateMandatoryTimestamp(startTime);
        Validator.validateTimestampAfterToday(startTime);
        Validator.validateMandatoryTimestamp(endTime);
        Validator.validateTimestampAfterToday(endTime);
        Validator.validateTimestamp1BeforeTimestamp2(startTime, endTime);
        if(eventType == null || !EventType.contains(eventType.toString())) {
            throw new ValidationException("Eventtype wrong");
        }
        if(eventStatus == null || !EventStatus.contains(eventStatus.toString())) {
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

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = (endTime == null ? Timestamp.valueOf(startTime.toLocalDateTime().plusHours(2)) : endTime);
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(EventStatus eventStatus) {
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

    public Double getPoints() {
        return defaultPoints;
    }

    public void setPoints(Double defaultPoints) {
        this.defaultPoints = defaultPoints;
    }

    public Integer getRehearsalFor() {
        return rehearsalFor;
    }

    public void setRehearsalFor(Integer rehearsalFor) {
        this.rehearsalFor = rehearsalFor;
    }

    public IInstrumentation getInstrumentation() {
        return instrumentation;
    }

    public void setInstrumentation(IInstrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    public Integer getInstrumentationId() {
        return instrumentationId;
    }

    public void setInstrumentationId(Integer instrumentationId) {
        this.instrumentationId = instrumentationId;
    }

    public DutyRosterStatus getDutyRosterStatus() {
        return dutyRosterStatus;
    }

    public void setDutyRosterStatus(DutyRosterStatus dutyRosterStatus) {
        this.dutyRosterStatus = dutyRosterStatus;
    }

    public List<MusicalWorkModel> getMusicalWorks() {
        return musicalWorks;
    }

    public void setMusicalWorks(List<MusicalWorkModel> musicalWorks) {
        this.musicalWorks = musicalWorks;
    }
}