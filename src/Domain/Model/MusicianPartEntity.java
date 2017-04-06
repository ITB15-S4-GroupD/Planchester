package Domain.Model;

import javax.persistence.*;

/**
 * Created by Bernd on 06.04.2017.
 */
@Entity
@Table(name = "Musician_Part", schema = "sem4_team2", catalog = "")
@IdClass(MusicianPartEntityPK.class)
public class MusicianPartEntity {
    private int musician;
    private int part;

    @Id
    @Column(name = "musician")
    public int getMusician() {
        return musician;
    }

    public void setMusician(int musician) {
        this.musician = musician;
    }

    @Id
    @Column(name = "part")
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

        MusicianPartEntity that = (MusicianPartEntity) o;

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
