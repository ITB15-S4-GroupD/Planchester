package TeamF.Hibernate.entities;

import TeamF.Hibernate.enums.DutyRosterStatus;
import TeamF.Hibernate.enums.SectionType;
import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "SectionDutyRoster", schema = "sem4_team2", catalog = "")
public class SectionDutyRosterEntity {
    private int sectionDutyRosterId;
    private DutyRosterStatus dutyRosterStatus;
    private SectionType sectionType;
    private Collection<EventDutySectionDutyRosterEntity> eventDutySectionDutyRostersBySectionDutyRosterId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sectionDutyRosterID", nullable = false)
    public int getSectionDutyRosterId() {
        return sectionDutyRosterId;
    }

    public void setSectionDutyRosterId(int sectionDutyRosterId) {
        this.sectionDutyRosterId = sectionDutyRosterId;
    }

    @Basic
    @Column(name = "dutyRosterStatus", nullable = false)
    @Enumerated(EnumType.STRING)
    public DutyRosterStatus getDutyRosterStatus() {
        return dutyRosterStatus;
    }

    public void setDutyRosterStatus(DutyRosterStatus dutyRosterStatus) {
        this.dutyRosterStatus = dutyRosterStatus;
    }

    @Basic
    @Column(name = "sectionType", nullable = false, precision = 0)
    @Enumerated(EnumType.STRING)
    public SectionType getSectionType() {
        return sectionType;
    }

    public void setSectionType(SectionType sectionType) {
        this.sectionType = sectionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SectionDutyRosterEntity that = (SectionDutyRosterEntity) o;

        if (sectionDutyRosterId != that.sectionDutyRosterId) return false;
        if (dutyRosterStatus != null ? !dutyRosterStatus.equals(that.dutyRosterStatus) : that.dutyRosterStatus != null)
            return false;
        if (sectionType != null ? !sectionType.equals(that.sectionType) : that.sectionType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sectionDutyRosterId;
        result = 31 * result + (dutyRosterStatus != null ? dutyRosterStatus.hashCode() : 0);
        result = 31 * result + (sectionType != null ? sectionType.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "sectionDutyRosterBySectionDutyRoster")
    public Collection<EventDutySectionDutyRosterEntity> getEventDutySectionDutyRostersBySectionDutyRosterId() {
        return eventDutySectionDutyRostersBySectionDutyRosterId;
    }

    public void setEventDutySectionDutyRostersBySectionDutyRosterId(Collection<EventDutySectionDutyRosterEntity> eventDutySectionDutyRostersBySectionDutyRosterId) {
        this.eventDutySectionDutyRostersBySectionDutyRosterId = eventDutySectionDutyRostersBySectionDutyRosterId;
    }
}
