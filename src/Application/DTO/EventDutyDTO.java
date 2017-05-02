package Application.DTO;

import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by julia on 26.04.2017.
 */
public class EventDutyDTO {
    private Integer eventDutyID;
    private String name;
    private String description;
    private Timestamp startTime;
    private Timestamp endTime;
    private Enum<EventType> eventType;
    private Enum<EventStatus> eventStatus;
    private String conductor;
    private String eventLocation;
    private List<MusicalWorkDTO> musicalWorks;
    private Double points;
    private Integer instrumentation;
    private Integer rehearsalFor;

    public Integer getEventDutyID() {
        return eventDutyID;
    }

    public void setEventDutyID(Integer eventDutyID) {
        this.eventDutyID = eventDutyID;
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
        this.endTime = endTime;
    }

    public Enum<EventType> getEventType() {
        return eventType;
    }

    public void setEventType(Enum<EventType> eventType) {
        this.eventType = eventType;
    }

    public Enum<EventStatus> getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(Enum<EventStatus> eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public List<MusicalWorkDTO> getMusicalWorks() {
        return musicalWorks;
    }

    public void setMusicalWorks(List<MusicalWorkDTO> musicalWorks) {
        this.musicalWorks = musicalWorks;
    }

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public Integer getInstrumentation() {
        return instrumentation;
    }

    public void setInstrumentation(Integer instrumentation) {
        this.instrumentation = instrumentation;
    }

    public Integer getRehearsalFor() {
        return rehearsalFor;
    }

    public void setRehearsalFor(Integer rehearsalFor) {
        this.rehearsalFor = rehearsalFor;
    }
}