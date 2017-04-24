package Domain.Entities;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Bernd on 06.04.2017.
 */
@Entity
@Table(name = "WoodInstrumentation", schema = "sem4_team2", catalog = "")
public class WoodInstrumentationEntity {
    private int woodInstrumentationId;
    private int flute;
    private int oboe;
    private int clarinet;
    private int bassoon;
    private Collection<InstrumentationEntity> instrumentationsByWoodInstrumentationId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "woodInstrumentationID", nullable = false)
    public int getWoodInstrumentationId() {
        return woodInstrumentationId;
    }

    public void setWoodInstrumentationId(int woodInstrumentationId) {
        this.woodInstrumentationId = woodInstrumentationId;
    }

    @Basic
    @Column(name = "flute", nullable = false)
    public int getFlute() {
        return flute;
    }

    public void setFlute(int flute) {
        this.flute = flute;
    }

    @Basic
    @Column(name = "oboe", nullable = false)
    public int getOboe() {
        return oboe;
    }

    public void setOboe(int oboe) {
        this.oboe = oboe;
    }

    @Basic
    @Column(name = "clarinet", nullable = false)
    public int getClarinet() {
        return clarinet;
    }

    public void setClarinet(int clarinet) {
        this.clarinet = clarinet;
    }

    @Basic
    @Column(name = "bassoon", nullable = false)
    public int getBassoon() {
        return bassoon;
    }

    public void setBassoon(int bassoon) {
        this.bassoon = bassoon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WoodInstrumentationEntity that = (WoodInstrumentationEntity) o;

        if (woodInstrumentationId != that.woodInstrumentationId) return false;
        if (flute != that.flute) return false;
        if (oboe != that.oboe) return false;
        if (clarinet != that.clarinet) return false;
        if (bassoon != that.bassoon) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = woodInstrumentationId;
        result = 31 * result + flute;
        result = 31 * result + oboe;
        result = 31 * result + clarinet;
        result = 31 * result + bassoon;
        return result;
    }

    @OneToMany(mappedBy = "woodInstrumentationByWoodInstrumentation")
    public Collection<InstrumentationEntity> getInstrumentationsByWoodInstrumentationId() {
        return instrumentationsByWoodInstrumentationId;
    }

    public void setInstrumentationsByWoodInstrumentationId(Collection<InstrumentationEntity> instrumentationsByWoodInstrumentationId) {
        this.instrumentationsByWoodInstrumentationId = instrumentationsByWoodInstrumentationId;
    }
}
