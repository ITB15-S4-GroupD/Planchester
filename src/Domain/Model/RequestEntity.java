package Domain.Model;

import javax.persistence.*;

/**
 * Created by Bernd on 06.04.2017.
 */
@Entity
@Table(name = "Request", schema = "sem4_team2", catalog = "")
@IdClass(RequestEntityPK.class)
public class RequestEntity {
    private int eventDuty;
    private int musician;
    private Enum requestType;
    private String description;
    private EventDutyEntity eventDutyByEventDuty;
    private PersonEntity personByMusician;

    @Id
    @Column(name = "eventDuty")
    public int getEventDuty() {
        return eventDuty;
    }

    public void setEventDuty(int eventDuty) {
        this.eventDuty = eventDuty;
    }

    @Id
    @Column(name = "musician")
    public int getMusician() {
        return musician;
    }

    public void setMusician(int musician) {
        this.musician = musician;
    }

    @Basic
    @Column(name = "requestType")
    public Enum getRequestType() {
        return requestType;
    }

    public void setRequestType(Enum requestType) {
        this.requestType = requestType;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestEntity that = (RequestEntity) o;

        if (eventDuty != that.eventDuty) return false;
        if (musician != that.musician) return false;
        if (requestType != null ? !requestType.equals(that.requestType) : that.requestType != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = eventDuty;
        result = 31 * result + musician;
        result = 31 * result + (requestType != null ? requestType.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "eventDuty", referencedColumnName = "eventDutyID", nullable = false)
    public EventDutyEntity getEventDutyByEventDuty() {
        return eventDutyByEventDuty;
    }

    public void setEventDutyByEventDuty(EventDutyEntity eventDutyByEventDuty) {
        this.eventDutyByEventDuty = eventDutyByEventDuty;
    }

    @ManyToOne
    @JoinColumn(name = "musician", referencedColumnName = "personId", nullable = false)
    public PersonEntity getPersonByMusician() {
        return personByMusician;
    }

    public void setPersonByMusician(PersonEntity personByMusician) {
        this.personByMusician = personByMusician;
    }
}
