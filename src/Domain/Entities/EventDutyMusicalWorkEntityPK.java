package Domain.Entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Bernd on 06.04.2017.
 */
public class EventDutyMusicalWorkEntityPK implements Serializable {
    private int eventDuty;
    private int musicalWork;

    @Column(name = "eventDuty")
    @Id
    public int getEventDuty() {
        return eventDuty;
    }

    public void setEventDuty(int eventDuty) {
        this.eventDuty = eventDuty;
    }

    @Column(name = "musicalWork")
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
