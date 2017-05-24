package TeamF.Hibernate.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class EventDutyMusicalWorkEntityPK implements Serializable {
    private int eventDuty;
    private int musicalWork;

    @Column(name = "eventDuty", nullable = false)
    @Id
    public int getEventDuty() {
        return eventDuty;
    }

    public void setEventDuty(int eventDuty) {
        this.eventDuty = eventDuty;
    }

    @Column(name = "musicalWork", nullable = false)
    @Id
    public int getMusicalWork() {
        return musicalWork;
    }

    public void setMusicalWork(int musicalWork) {
        this.musicalWork = musicalWork;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventDutyMusicalWorkEntityPK that = (EventDutyMusicalWorkEntityPK) o;

        if (eventDuty != that.eventDuty) return false;
        if (musicalWork != that.musicalWork) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = eventDuty;
        result = 31 * result + musicalWork;
        return result;
    }
}
