package Persistence.Entities;

import javax.persistence.*;

/**
 * Created by Bernd on 06.04.2017.
 */
@Entity
@Table(name = "DutyDisposition", schema = "sem4_team2", catalog = "")
@IdClass(DutyDispositionEntityPK.class)
public class DutyDispositionEntity {
    private int eventDuty;
    private int musician;
    private double points;
    private String description;
    private String dutyDispositionStatus;
    private EventDutyEntity eventDutyByEventDuty;
    private PersonEntity personByMusician;

    @Id
    @Column(name = "eventDuty", nullable = false)
    public int getEventDuty() {
        return eventDuty;
    }

    public void setEventDuty(int eventDuty) {
        this.eventDuty = eventDuty;
    }

    @Id
    @Column(name = "musician", nullable = false)
    public int getMusician() {
        return musician;
    }

    public void setMusician(int musician) {
        this.musician = musician;
    }

    @Basic
    @Column(name = "points", nullable = false, precision = 0)
    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "dutyDispositionStatus", nullable = false, columnDefinition = "enum('Spare', 'Illness', 'Normal')")
    public String getDutyDispositionStatus() {
        return dutyDispositionStatus;
    }

    public void setDutyDispositionStatus(String dutyDispositionStatus) {
        this.dutyDispositionStatus = dutyDispositionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DutyDispositionEntity that = (DutyDispositionEntity) o;

        if (eventDuty != that.eventDuty) return false;
        if (musician != that.musician) return false;
        if (Double.compare(that.points, points) != 0) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (dutyDispositionStatus != null ? !dutyDispositionStatus.equals(that.dutyDispositionStatus) : that.dutyDispositionStatus != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = eventDuty;
        result = 31 * result + musician;
        temp = Double.doubleToLongBits(points);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (dutyDispositionStatus != null ? dutyDispositionStatus.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "eventDuty", referencedColumnName = "eventDutyID", nullable = false, insertable = false, updatable = false)
    public EventDutyEntity getEventDutyByEventDuty() {
        return eventDutyByEventDuty;
    }

    public void setEventDutyByEventDuty(EventDutyEntity eventDutyByEventDuty) {
        this.eventDutyByEventDuty = eventDutyByEventDuty;
    }

    @ManyToOne
    @JoinColumn(name = "musician", referencedColumnName = "personId", nullable = false, insertable = false, updatable = false)
    public PersonEntity getPersonByMusician() {
        return personByMusician;
    }

    public void setPersonByMusician(PersonEntity personByMusician) {
        this.personByMusician = personByMusician;
    }
}
