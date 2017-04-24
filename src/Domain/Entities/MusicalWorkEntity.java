package Domain.Entities;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Bernd on 06.04.2017.
 */
@Entity
@Table(name = "MusicalWork", schema = "sem4_team2", catalog = "")
public class MusicalWorkEntity {
    private int musicalWorkId;
    private int instrumentationId;
    private String name;
    private String composer;
    private Collection<EventDutyMusicalWorkEntity> eventDutyMusicalWorksByMusicalWorkId;
    private InstrumentationEntity instrumentationByInstrumentationId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "musicalWorkID", nullable = false)
    public int getMusicalWorkId() {
        return musicalWorkId;
    }

    public void setMusicalWorkId(int musicalWorkId) {
        this.musicalWorkId = musicalWorkId;
    }

    @Basic
    @Column(name = "instrumentationID", nullable = false)
    public int getInstrumentationId() {
        return instrumentationId;
    }

    public void setInstrumentationId(int instrumentationId) {
        this.instrumentationId = instrumentationId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "composer", nullable = true, length = 255)
    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicalWorkEntity that = (MusicalWorkEntity) o;

        if (musicalWorkId != that.musicalWorkId) return false;
        if (instrumentationId != that.instrumentationId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (composer != null ? !composer.equals(that.composer) : that.composer != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = musicalWorkId;
        result = 31 * result + instrumentationId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (composer != null ? composer.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "musicalWorkByMusicalWork")
    public Collection<EventDutyMusicalWorkEntity> getEventDutyMusicalWorksByMusicalWorkId() {
        return eventDutyMusicalWorksByMusicalWorkId;
    }

    public void setEventDutyMusicalWorksByMusicalWorkId(Collection<EventDutyMusicalWorkEntity> eventDutyMusicalWorksByMusicalWorkId) {
        this.eventDutyMusicalWorksByMusicalWorkId = eventDutyMusicalWorksByMusicalWorkId;
    }

    @ManyToOne
    @JoinColumn(name = "instrumentationID", referencedColumnName = "instrumentationID", nullable = false, insertable = false, updatable = false)
    public InstrumentationEntity getInstrumentationByInstrumentationId() {
        return instrumentationByInstrumentationId;
    }

    public void setInstrumentationByInstrumentationId(InstrumentationEntity instrumentationByInstrumentationId) {
        this.instrumentationByInstrumentationId = instrumentationByInstrumentationId;
    }
}
