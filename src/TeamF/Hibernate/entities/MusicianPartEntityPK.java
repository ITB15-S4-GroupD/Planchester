package TeamF.Hibernate.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class MusicianPartEntityPK implements Serializable {
    private int musician;
    private int part;

    @Column(name = "musician", nullable = false)
    @Id
    public int getMusician() {
        return musician;
    }

    public void setMusician(int musician) {
        this.musician = musician;
    }

    @Column(name = "part", nullable = false)
    @Id
    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicianPartEntityPK that = (MusicianPartEntityPK) o;

        if (musician != that.musician) return false;
        if (part != that.part) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = musician;
        result = 31 * result + part;
        return result;
    }
}
