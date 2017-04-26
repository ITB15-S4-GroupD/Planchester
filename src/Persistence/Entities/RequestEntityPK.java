package Persistence.Entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Bernd on 06.04.2017.
 */
public class RequestEntityPK implements Serializable {
    private int eventDuty;
    private int musician;

    @Column(name = "eventDuty")
    @Id
    public int getEventDuty() {
        return eventDuty;
    }

    public void setEventDuty(int eventDuty) {
        this.eventDuty = eventDuty;
    }

    @Column(name = "musician")
    @Id
    public int getMusician() {
        return musician;
    }

    public void setMusician(int musician) {
        this.musician = musician;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestEntityPK that = (RequestEntityPK) o;

        if (eventDuty != that.eventDuty) return false;
        if (musician != that.musician) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = eventDuty;
        result = 31 * result + musician;
        return result;
    }
}
