package Domain.Model;

import javax.persistence.*;

/**
 * Created by Bernd on 06.04.2017.
 */
@Entity
@Table(name = "EventDuty_SectionDutyRoster", schema = "sem4_team2", catalog = "")
@IdClass(EventDutySectionDutyRosterEntityPK.class)
public class EventDutySectionDutyRosterEntity {
    private int eventDuty;
    private int sectionDutyRoster;
    private EventDutyEntity eventDutyByEventDuty;

    @Id
    @ManyToOne
    @JoinColumn(name = "eventDuty", referencedColumnName = "eventDutyID", nullable = false)
    @Column(name = "eventDuty")
    public int getEventDuty() {
        return eventDuty;
    }

    public void setEventDuty(int eventDuty) {
        this.eventDuty = eventDuty;
    }

    @Id
    @Column(name = "sectionDutyRoster")
    public int getSectionDutyRoster() {
        return sectionDutyRoster;
    }

    public void setSectionDutyRoster(int sectionDutyRoster) {
        this.sectionDutyRoster = sectionDutyRoster;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventDutySectionDutyRosterEntity that = (EventDutySectionDutyRosterEntity) o;

        if (eventDuty != that.eventDuty) return false;
        if (sectionDutyRoster != that.sectionDutyRoster) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = eventDuty;
        result = 31 * result + sectionDutyRoster;
        return result;
    }

    public void setEventDutyByEventDuty(EventDutyEntity eventDutyByEventDuty) {
        this.eventDutyByEventDuty = eventDutyByEventDuty;
    }
}
