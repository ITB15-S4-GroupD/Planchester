package Domain.Model;

import javax.persistence.*;

/**
 * Created by Bernd on 06.04.2017.
 */
@Entity
@Table(name = "MusicalWork", schema = "sem4_team2", catalog = "")
public class MusicalWorkEntity {
    private int musicalWorkId;
    private String name;
    private String composer;
    private InstrumentationEntity instrumentationByInstrumentationId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "musicalWorkID")
    public int getMusicalWorkId() {
        return musicalWorkId;
    }

    public void setMusicalWorkId(int musicalWorkId) {
        this.musicalWorkId = musicalWorkId;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "composer")
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
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (composer != null ? !composer.equals(that.composer) : that.composer != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = musicalWorkId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (composer != null ? composer.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "instrumentationID", referencedColumnName = "instrumentationID", nullable = false)
    public InstrumentationEntity getInstrumentationByInstrumentationId() {
        return instrumentationByInstrumentationId;
    }

    public void setInstrumentationByInstrumentationId(InstrumentationEntity instrumentationByInstrumentationId) {
        this.instrumentationByInstrumentationId = instrumentationByInstrumentationId;
    }
}
