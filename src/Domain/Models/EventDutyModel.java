package Domain.Models;

import Domain.Entities.EventDutyEntity;
import Domain.Enum.EventStatus;
import Domain.Enum.EventType;
import Utils.Validator;

import javax.xml.bind.ValidationException;
import java.sql.Timestamp;

/**
 * Created by julia on 13.04.2017.
 */
public class EventDutyModel {
    private int eventDutyId;
    private String name;
    private String description;
    private Timestamp starttime;
    private Timestamp endtime;
    private String eventType;
    private String eventStatus;
    private String conductor;
    private String location;
    private double defaultPoints;
    private EventDutyModel eventDutyByRehearsalFor;
    private InstrumentationModel instrumentation;

    public EventDutyModel() {
    }

    public EventDutyModel(EventDutyEntity eventDutyEntity) {
        eventDutyId = eventDutyEntity.getEventDutyId();
        name = eventDutyEntity.getName();
        description = eventDutyEntity.getDescription();
        starttime = eventDutyEntity.getStarttime();
        endtime = eventDutyEntity.getEndtime();
        eventType = eventDutyEntity.getEventType();
        eventStatus = eventDutyEntity.getEventStatus();
        conductor = eventDutyEntity.getConductor();
        location = eventDutyEntity.getLocation();
        defaultPoints = eventDutyEntity.getDefaultPoints();
        eventDutyByRehearsalFor = eventDutyEntity.getEventDutyByRehearsalFor() != null ? new EventDutyModel(eventDutyEntity.getEventDutyByRehearsalFor()) : null;
        //TODO bernd: instrumentation = eventDutyEntity.getInstrumentation();
    }

    public EventDutyEntity getEventDutyEntity() {
        EventDutyEntity eventDutyEntity = new EventDutyEntity();
        eventDutyEntity.setEventDutyId(eventDutyId);
        eventDutyEntity.setName(name);
        eventDutyEntity.setDescription(description);
        eventDutyEntity.setStarttime(starttime);
        eventDutyEntity.setEndtime(endtime);
        eventDutyEntity.setEventType(eventType);
        eventDutyEntity.setEventStatus(eventStatus);
        eventDutyEntity.setConductor(conductor);
        eventDutyEntity.setLocation(location);
        eventDutyEntity.setDefaultPoints(defaultPoints);
        eventDutyEntity.setEventDutyByRehearsalFor(eventDutyByRehearsalFor != null ? eventDutyByRehearsalFor.getEventDutyEntity() : null);
        //TODO bernd: setInstrumentation
        return eventDutyEntity;
    }

    public boolean preValidate() {
        //TODO julia
        return false;
    }

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
        Validator.validateMandatoryDouble(defaultPoints, 0, Double.MAX_VALUE);
    }

    public int getEventDutyId() {
        return eventDutyId;
    }

    public void setEventDutyId(int eventDutyId) {
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
        this.endtime = endtime;
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

    public double getDefaultPoints() {
        return defaultPoints;
    }

    public void setDefaultPoints(double defaultPoints) {
        this.defaultPoints = defaultPoints;
    }

    public EventDutyModel getEventDutyByRehearsalFor() {
        return eventDutyByRehearsalFor;
    }

    public void setEventDutyByRehearsalFor(EventDutyModel eventDutyByRehearsalFor) {
        this.eventDutyByRehearsalFor = eventDutyByRehearsalFor;
    }

    public InstrumentationModel getInstrumentation() {
        return instrumentation;
    }

    public void setInstrumentation(InstrumentationModel instrumentation) {
        this.instrumentation = instrumentation;
    }
}