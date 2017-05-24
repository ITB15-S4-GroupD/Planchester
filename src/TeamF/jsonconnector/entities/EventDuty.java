package TeamF.jsonconnector.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import TeamF.jsonconnector.enums.EventStatus;
import TeamF.jsonconnector.enums.EventType;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDuty implements JSONObjectEntity {
    private int _eventDutyID;
    private String _name;
    private String _description;
    private LocalDateTime _startTime;
    private LocalDateTime _endTime;
    private EventType _eventType;
    private EventStatus _eventStatus;
    private String _conductor;
    private String _location;
    private EventDuty _rehearsalFor;
    private double _defaultPoints;
    private Instrumentation _maxInstrumentation;
    private List<MusicalWork> _musicalWorkList = new LinkedList<>();
    private List<Instrumentation> _instrumentationList = new LinkedList<>();

    @JsonGetter("id")
    public int getEventDutyID() {
        return _eventDutyID;
    }

    @JsonGetter("name")
    public String getName() {
        return _name;
    }

    @JsonGetter("description")
    public String getDescription() {
        return _description;
    }

    @JsonGetter("start_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public LocalDateTime getStartTime() {
        return _startTime;
    }

    @JsonGetter("end_time")
    public LocalDateTime getEndTime() {
        return _endTime;
    }

    @JsonGetter("type")
    public EventType getEventType() {
        return _eventType;
    }

    @JsonGetter("status")
    public EventStatus getEventStatus() {
        return _eventStatus;
    }

    @JsonGetter("conductor")
    public String getConductor() {
        return _conductor;
    }

    @JsonGetter("location")
    public String getLocation() {
        return _location;
    }

    @JsonGetter("rehearsal_for")
    public EventDuty getRehearsalFor() {
        return _rehearsalFor;
    }

    @JsonGetter("default_points")
    public double getDefaultPoints() {
        return _defaultPoints;
    }

    //@JsonGetter("instrumentation")
    public Instrumentation getInstrumentation() {
        return _maxInstrumentation;
    }

    @JsonGetter("musical_works")
    public List<MusicalWork> getMusicalWorkList() {
        return _musicalWorkList;
    }

    @JsonGetter("max_instrumentation")
    public Instrumentation getMaxInstrumentation() {
        return _maxInstrumentation;
    }

    @JsonSetter("id")
    public void setEventDutyID(int eventDutyID) {
        _eventDutyID = eventDutyID;
    }

    @JsonSetter("name")
    public void setName(String name) {
        _name = name;
    }

    @JsonSetter("description")
    public void setDescription(String description) {
        _description = description;
    }

    @JsonSetter("start_time")
    public void setStartTime(LocalDateTime startTime) {
        _startTime = startTime;
    }

    @JsonSetter("end_time")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public void setEndTime(LocalDateTime endTime) {
        _endTime = endTime;
    }

    @JsonSetter("type")
    public void setEventType(EventType eventType) {
        _eventType = eventType;
    }

    @JsonSetter("status")
    public void setEventStatus(EventStatus eventStatus) {
        _eventStatus = eventStatus;
    }

    @JsonSetter("conductor")
    public void setConductor(String conductor) {
        _conductor = conductor;
    }

    @JsonSetter("location")
    public void setLocation(String location) {
        _location = location;
    }

    @JsonSetter("rehearsal_for")
    public void setRehearsalFor(EventDuty rehearsalFor) {
        _rehearsalFor = rehearsalFor;
    }

    @JsonSetter("default_points")
    public void setDefaultPoints(double defaultPoints) {
        _defaultPoints = defaultPoints;
    }

    @JsonSetter("musical_works")
    public void setMusicalWorkList(List<MusicalWork> e) {
        this._musicalWorkList = e;
    }

    @JsonSetter("max_instrumentation")
    public void setMaxInstrumentation(Instrumentation instrumentation) {
        _maxInstrumentation = instrumentation;
    }

    @Override
    public String getEntityName() {
        return "Event";
    }

    @Override
    public String getDisplayName() {
        return getName();
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}