package Domain.Model;

import javax.persistence.*;

/**
 * Created by Bernd on 06.04.2017.
 */
@Entity
@Table(name = "BrassInstrumentation", schema = "sem4_team2", catalog = "")
public class BrassInstrumentationEntity {
    private int brassInstrumentationId;
    private int horn;
    private int trumpet;
    private int trombone;
    private int tube;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brassInstrumentationID")
    public int getBrassInstrumentationId() {
        return brassInstrumentationId;
    }

    public void setBrassInstrumentationId(int brassInstrumentationId) {
        this.brassInstrumentationId = brassInstrumentationId;
    }

    @Basic
    @Column(name = "horn")
    public int getHorn() {
        return horn;
    }

    public void setHorn(int horn) {
        this.horn = horn;
    }

    @Basic
    @Column(name = "trumpet")
    public int getTrumpet() {
        return trumpet;
    }

    public void setTrumpet(int trumpet) {
        this.trumpet = trumpet;
    }

    @Basic
    @Column(name = "trombone")
    public int getTrombone() {
        return trombone;
    }

    public void setTrombone(int trombone) {
        this.trombone = trombone;
    }

    @Basic
    @Column(name = "tube")
    public int getTube() {
        return tube;
    }

    public void setTube(int tube) {
        this.tube = tube;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BrassInstrumentationEntity that = (BrassInstrumentationEntity) o;

        if (brassInstrumentationId != that.brassInstrumentationId) return false;
        if (horn != that.horn) return false;
        if (trumpet != that.trumpet) return false;
        if (trombone != that.trombone) return false;
        if (tube != that.tube) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = brassInstrumentationId;
        result = 31 * result + horn;
        result = 31 * result + trumpet;
        result = 31 * result + trombone;
        result = 31 * result + tube;
        return result;
    }
}
