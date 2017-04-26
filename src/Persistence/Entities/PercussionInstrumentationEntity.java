package Persistence.Entities;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Bernd on 06.04.2017.
 */
@Entity
@Table(name = "PercussionInstrumentation", schema = "sem4_team2", catalog = "")
public class PercussionInstrumentationEntity {
    private int percussionInstrumentationId;
    private int kettledrum;
    private int percussion;
    private int harp;
    private String percussionDescription;
    private Collection<InstrumentationEntity> instrumentationsByPercussionInstrumentationId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "percussionInstrumentationID", nullable = false)
    public int getPercussionInstrumentationId() {
        return percussionInstrumentationId;
    }

    public void setPercussionInstrumentationId(int percussionInstrumentationId) {
        this.percussionInstrumentationId = percussionInstrumentationId;
    }

    @Basic
    @Column(name = "kettledrum", nullable = false)
    public int getKettledrum() {
        return kettledrum;
    }

    public void setKettledrum(int kettledrum) {
        this.kettledrum = kettledrum;
    }

    @Basic
    @Column(name = "percussion", nullable = false)
    public int getPercussion() {
        return percussion;
    }

    public void setPercussion(int percussion) {
        this.percussion = percussion;
    }

    @Basic
    @Column(name = "harp", nullable = false)
    public int getHarp() {
        return harp;
    }

    public void setHarp(int harp) {
        this.harp = harp;
    }

    @Basic
    @Column(name = "percussionDescription", nullable = true, length = 255)
    public String getPercussionDescription() {
        return percussionDescription;
    }

    public void setPercussionDescription(String percussionDescription) {
        this.percussionDescription = percussionDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PercussionInstrumentationEntity that = (PercussionInstrumentationEntity) o;

        if (percussionInstrumentationId != that.percussionInstrumentationId) return false;
        if (kettledrum != that.kettledrum) return false;
        if (percussion != that.percussion) return false;
        if (harp != that.harp) return false;
        if (percussionDescription != null ? !percussionDescription.equals(that.percussionDescription) : that.percussionDescription != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = percussionInstrumentationId;
        result = 31 * result + kettledrum;
        result = 31 * result + percussion;
        result = 31 * result + harp;
        result = 31 * result + (percussionDescription != null ? percussionDescription.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "percussionInstrumentationByPercussionInstrumentation")
    public Collection<InstrumentationEntity> getInstrumentationsByPercussionInstrumentationId() {
        return instrumentationsByPercussionInstrumentationId;
    }

    public void setInstrumentationsByPercussionInstrumentationId(Collection<InstrumentationEntity> instrumentationsByPercussionInstrumentationId) {
        this.instrumentationsByPercussionInstrumentationId = instrumentationsByPercussionInstrumentationId;
    }
}
