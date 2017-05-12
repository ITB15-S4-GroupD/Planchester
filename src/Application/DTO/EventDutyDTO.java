package Application.DTO;

import Domain.Interfaces.IEventDuty;
import Domain.Interfaces.IInstrumentation;
import Domain.Models.EventDutyModel;
import Utils.Enum.DutyRosterStatus;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;

import java.lang.instrument.Instrumentation;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by julia on 26.04.2017.
 */
public class EventDutyDTO implements IEventDuty {
    private Integer eventDutyId;
    private String name;
    private String description;
    private Timestamp startTime;
    private Timestamp endTime;
    private EventType eventType;
    private EventStatus eventStatus;
    private String conductor;
    private String eventLocation;
    private List<MusicalWorkDTO> musicalWorks;
    private Double points;
    private IInstrumentation instrumentation;
    private Integer rehearsalFor;
    private DutyRosterStatus dutyRosterStatus;
    private int instrumentationId;

    public Integer getEventDutyId() {
        return eventDutyId;
    }

    public void setEventDutyId(Integer eventDutyID) {
        this.eventDutyId = eventDutyID;
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

    public String getLocation() {
        return eventLocation;
    }

    public void setLocation(String eventLocation) {
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

    public IInstrumentation getInstrumentation() {
        return instrumentation;
    }

    public void setInstrumentation(IInstrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    public int getInstrumentationId() {
        return instrumentationId;
    }

    public void setInstrumentationId(int instrumentationId) {
        this.instrumentationId = instrumentationId;
    }

    public DutyRosterStatus getDutyRosterStatus() {
        return dutyRosterStatus;
    }

    public void setDutyRosterStatus(DutyRosterStatus dutyRosterStatus) {
        this.dutyRosterStatus = dutyRosterStatus;
    }

    public Integer getRehearsalFor() {
        return rehearsalFor;
    }

    public void setRehearsalFor(Integer rehearsalFor) {
        this.rehearsalFor = rehearsalFor;
    }
}