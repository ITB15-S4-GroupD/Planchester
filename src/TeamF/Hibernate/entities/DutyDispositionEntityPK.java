package TeamF.Hibernate.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class DutyDispositionEntityPK implements Serializable {
    private int eventDuty;
    private int musician;

    @Column(name = "eventDuty", nullable = false)
    @Id
    public int getEventDuty() {
        return eventDuty;
    }

    public void setEventDuty(int eventDuty) {
        this.eventDuty = eventDuty;
    }

    @Column(name = "musician", nullable = false)
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

        DutyDispositionEntityPK that = (DutyDispositionEntityPK) o;

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
