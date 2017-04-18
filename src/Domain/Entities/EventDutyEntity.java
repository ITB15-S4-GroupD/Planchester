package Domain.Entities;

import Domain.Models.EventDutyModel;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Bernd on 06.04.2017.
 */
@Entity
@Table(name = "EventDuty", schema = "sem4_team2", catalog = "")
public class EventDutyEntity {
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
    private EventDutyEntity eventDutyByRehearsalFor;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventDutyID")
    public int getEventDutyId() {
        return eventDutyId;
    }

    public void setEventDutyId(int eventDutyId) {
        this.eventDutyId = eventDutyId;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "starttime")
    public Timestamp getStarttime() {
        return starttime;
    }

    public void setStarttime(Timestamp starttime) {
        this.starttime = starttime;
    }

    @Basic
    @Column(name = "endtime")
    public Timestamp getEndtime() {
        return endtime;
    }

    public void setEndtime(Timestamp endtime) {
        this.endtime = endtime;
    }

    @Column(name = "eventType", columnDefinition = "enum('Opera', 'Concert', 'Hofkapelle', 'Tour', 'Rehearsal', 'NonMusicalEvent')")
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Column(name = "eventStatus", columnDefinition = "enum('Unpublished', 'Published', 'Cancelled')")
    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    @Basic
    @Column(name = "conductor")
    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    @Basic
    @Column(name = "location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Basic
    @Column(name = "defaultPoints")
    public double getDefaultPoints() {
        return defaultPoints;
    }

    public void setDefaultPoints(double defaultPoints) {
        this.defaultPoints = defaultPoints;
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
        temp = Double.doubleToLongBits(defaultPoints);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "rehearsalFor", referencedColumnName = "eventDutyID")
    public EventDutyEntity getEventDutyByRehearsalFor() {
        return eventDutyByRehearsalFor;
    }

    public void setEventDutyByRehearsalFor(EventDutyEntity eventDutyByRehearsalFor) {
        this.eventDutyByRehearsalFor = eventDutyByRehearsalFor;
    }
}
