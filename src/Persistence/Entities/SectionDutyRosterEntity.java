package Persistence.Entities;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Bernd on 06.04.2017.
 */
@Entity
@Table(name = "SectionDutyRoster", schema = "sem4_team2", catalog = "")
public class SectionDutyRosterEntity {
    private int sectionDutyRosterId;
    private String dutyRosterStatus;
    private String sectionType;
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

    @Column(name = "dutyRosterStatus", nullable = false, columnDefinition = "enum('Unpublished', 'Published')")
    public String getDutyRosterStatus() {
        return dutyRosterStatus;
    }

    public void setDutyRosterStatus(String dutyRosterStatus) {
        this.dutyRosterStatus = dutyRosterStatus;
    }

    @Column(name = "sectionType", nullable = false, precision = 0, columnDefinition = "enum('Violin1', 'Violin2', 'Viola', 'Violoncello', 'Doublebass', 'Woodwind', 'Brass', 'Percussion')")
    public String getSectionType() {
        return sectionType;
    }

    public void setSectionType(String sectionType) {
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
