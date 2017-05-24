package TeamF.Hibernate.entities;

import TeamF.Hibernate.enums.EventType;
import TeamF.Hibernate.enums.EventStatus;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "EventDuty", schema = "sem4_team2", catalog = "")
public class EventDutyEntity {
    private int eventDutyId;
    private String name;
    private String description;
    private LocalDateTime starttime;
    private LocalDateTime endtime;
    private EventType eventType;
    private EventStatus eventStatus;
    private String conductor;
    private String location;
    private Integer rehearsalFor;
    private double defaultPoints;
    private Integer instrumentation;
    private Collection<DutyDispositionEntity> dutyDispositionsByEventDutyId;
    private EventDutyEntity eventDutyByRehearsalFor;
    private Collection<EventDutyEntity> eventDutiesByEventDutyId;
    private InstrumentationEntity instrumentationByInstrumentation;
    private Collection<EventDutyMusicalWorkEntity> eventDutyMusicalWorksByEventDutyId;
    private Collection<EventDutySectionDutyRosterEntity> eventDutySectionDutyRostersByEventDutyId;
    private Collection<RequestEntity> requestsByEventDutyId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventDutyID", nullable = false)
    public int getEventDutyId() {
        return eventDutyId;
    }

    public void setEventDutyId(int eventDutyId) {
        this.eventDutyId = eventDutyId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "starttime", nullable = false)
    public LocalDateTime getStarttime() {
        return starttime;
    }

    public void setStarttime(LocalDateTime starttime) {
        this.starttime = starttime;
    }

    @Basic
    @Column(name = "endtime", nullable = false)
    public LocalDateTime getEndtime() {
        return endtime;
    }

    public void setEndtime(LocalDateTime endtime) {
        this.endtime = endtime;
    }

    @Basic
    @Column(name = "eventType", nullable = false)
    @Enumerated(EnumType.STRING)
    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    @Basic
    @Column(name = "eventStatus", nullable = false)
    @Enumerated(EnumType.STRING)
    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    @Basic
    @Column(name = "conductor", nullable = true, length = 255)
    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    @Basic
    @Column(name = "location", nullable = true, length = 255)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Basic
    @Column(name = "rehearsalFor", nullable = true)
    public Integer getRehearsalFor() {
        return rehearsalFor;
    }

    public void setRehearsalFor(Integer rehearsalFor) {
        this.rehearsalFor = rehearsalFor;
    }

    @Basic
    @Column(name = "defaultPoints", nullable = false, precision = 0)
    public double getDefaultPoints() {
        return defaultPoints;
    }

    public void setDefaultPoints(double defaultPoints) {
        this.defaultPoints = defaultPoints;
    }

    @Basic
    @Column(name = "instrumentation", nullable = true)
    public Integer getInstrumentation() {
        return instrumentation;
    }

    public void setInstrumentation(Integer instrumentation) {
        this.instrumentation = instrumentation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventDutyEntity that = (EventDutyEntity) o;

        if (eventDutyId != that.eventDutyId) return false;
        if (Double.compare(that.defaultPoints, defaultPoints) != 0) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (starttime != null ? !starttime.equals(that.starttime) : that.starttime != null) return false;
        if (endtime != null ? !endtime.equals(that.endtime) : that.endtime != null) return false;
        if (eventType != null ? !eventType.equals(that.eventType) : that.eventType != null) return false;
        if (eventStatus != null ? !eventStatus.equals(that.eventStatus) : that.eventStatus != null) return false;
        if (conductor != null ? !conductor.equals(that.conductor) : that.conductor != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (rehearsalFor != null ? !rehearsalFor.equals(that.rehearsalFor) : that.rehearsalFor != null) return false;
        if (instrumentation != null ? !instrumentation.equals(that.instrumentation) : that.instrumentation != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = eventDutyId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (starttime != null ? starttime.hashCode() : 0);
        result = 31 * result + (endtime != null ? endtime.hashCode() : 0);
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + (eventStatus != null ? eventStatus.hashCode() : 0);
        result = 31 * result + (conductor != null ? conductor.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (rehearsalFor != null ? rehearsalFor.hashCode() : 0);
        temp = Double.doubleToLongBits(defaultPoints);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (instrumentation != null ? instrumentation.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "eventDutyByEventDuty")
    public Collection<DutyDispositionEntity> getDutyDispositionsByEventDutyId() {
        return dutyDispositionsByEventDutyId;
    }

    public void setDutyDispositionsByEventDutyId(Collection<DutyDispositionEntity> dutyDispositionsByEventDutyId) {
        this.dutyDispositionsByEventDutyId = dutyDispositionsByEventDutyId;
    }

    @ManyToOne
    @JoinColumn(name = "rehearsalFor", referencedColumnName = "eventDutyID", insertable = false, updatable = false)
    public EventDutyEntity getEventDutyByRehearsalFor() {
        return eventDutyByRehearsalFor;
    }

    public void setEventDutyByRehearsalFor(EventDutyEntity eventDutyByRehearsalFor) {
        this.eventDutyByRehearsalFor = eventDutyByRehearsalFor;
    }

    @OneToMany(mappedBy = "eventDutyByRehearsalFor")
    public Collection<EventDutyEntity> getEventDutiesByEventDutyId() {
        return eventDutiesByEventDutyId;
    }

    public void setEventDutiesByEventDutyId(Collection<EventDutyEntity> eventDutiesByEventDutyId) {
        this.eventDutiesByEventDutyId = eventDutiesByEventDutyId;
    }

    @ManyToOne
    @JoinColumn(name = "instrumentation", referencedColumnName = "instrumentationID", insertable = false, updatable = false)
    public InstrumentationEntity getInstrumentationByInstrumentation() {
        return instrumentationByInstrumentation;
    }

    public void setInstrumentationByInstrumentation(InstrumentationEntity instrumentationByInstrumentation) {
        this.instrumentationByInstrumentation = instrumentationByInstrumentation;
    }

    @OneToMany(mappedBy = "eventDutyByEventDuty")
    public Collection<EventDutyMusicalWorkEntity> getEventDutyMusicalWorksByEventDutyId() {
        return eventDutyMusicalWorksByEventDutyId;
    }

    public void setEventDutyMusicalWorksByEventDutyId(Collection<EventDutyMusicalWorkEntity> eventDutyMusicalWorksByEventDutyId) {
        this.eventDutyMusicalWorksByEventDutyId = eventDutyMusicalWorksByEventDutyId;
    }

    @OneToMany(mappedBy = "eventDutyByEventDuty")
    public Collection<EventDutySectionDutyRosterEntity> getEventDutySectionDutyRostersByEventDutyId() {
        return eventDutySectionDutyRostersByEventDutyId;
    }

    public void setEventDutySectionDutyRostersByEventDutyId(Collection<EventDutySectionDutyRosterEntity> eventDutySectionDutyRostersByEventDutyId) {
        this.eventDutySectionDutyRostersByEventDutyId = eventDutySectionDutyRostersByEventDutyId;
    }

    @OneToMany(mappedBy = "eventDutyByEventDuty")
    public Collection<RequestEntity> getRequestsByEventDutyId() {
        return requestsByEventDutyId;
    }

    public void setRequestsByEventDutyId(Collection<RequestEntity> requestsByEventDutyId) {
        this.requestsByEventDutyId = requestsByEventDutyId;
    }
}
