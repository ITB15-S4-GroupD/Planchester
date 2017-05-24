package TeamF.Hibernate.entities;

import javax.persistence.*;

@Entity
@Table(name = "EventDuty_SectionDutyRoster", schema = "sem4_team2", catalog = "")
@IdClass(EventDutySectionDutyRosterEntityPK.class)
public class EventDutySectionDutyRosterEntity {
    private int eventDuty;
    private int sectionDutyRoster;
    private EventDutyEntity eventDutyByEventDuty;
    private SectionDutyRosterEntity sectionDutyRosterBySectionDutyRoster;

    @Id
    @Column(name = "eventDuty", nullable = false)
    public int getEventDuty() {
        return eventDuty;
    }

    public void setEventDuty(int eventDuty) {
        this.eventDuty = eventDuty;
    }

    @Id
    @Column(name = "sectionDutyRoster", nullable = false)
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

    @ManyToOne
    @JoinColumn(name = "eventDuty", referencedColumnName = "eventDutyID", nullable = false, insertable = false, updatable = false)
    public EventDutyEntity getEventDutyByEventDuty() {
        return eventDutyByEventDuty;
    }

    public void setEventDutyByEventDuty(EventDutyEntity eventDutyByEventDuty) {
        this.eventDutyByEventDuty = eventDutyByEventDuty;
    }

    @ManyToOne
    @JoinColumn(name = "sectionDutyRoster", referencedColumnName = "sectionDutyRosterID", nullable = false, insertable = false, updatable = false)
    public SectionDutyRosterEntity getSectionDutyRosterBySectionDutyRoster() {
        return sectionDutyRosterBySectionDutyRoster;
    }

    public void setSectionDutyRosterBySectionDutyRoster(SectionDutyRosterEntity sectionDutyRosterBySectionDutyRoster) {
        this.sectionDutyRosterBySectionDutyRoster = sectionDutyRosterBySectionDutyRoster;
    }
}
