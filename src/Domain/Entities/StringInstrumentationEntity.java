package Domain.Entities;

import javax.persistence.*;

/**
 * Created by Bernd on 06.04.2017.
 */
@Entity
@Table(name = "StringInstrumentation", schema = "sem4_team2", catalog = "")
public class StringInstrumentationEntity {
    private int stringInstrumentationId;
    private int violin1;
    private int violin2;
    private int viola;
    private int violincello;
    private int doublebass;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stringInstrumentationID")
    public int getStringInstrumentationId() {
        return stringInstrumentationId;
    }

    public void setStringInstrumentationId(int stringInstrumentationId) {
        this.stringInstrumentationId = stringInstrumentationId;
    }

    @Basic
    @Column(name = "violin1")
    public int getViolin1() {
        return violin1;
    }

    public void setViolin1(int violin1) {
        this.violin1 = violin1;
    }

    @Basic
    @Column(name = "violin2")
    public int getViolin2() {
        return violin2;
    }

    public void setViolin2(int violin2) {
        this.violin2 = violin2;
    }

    @Basic
    @Column(name = "viola")
    public int getViola() {
        return viola;
    }

    public void setViola(int viola) {
        this.viola = viola;
    }

    @Basic
    @Column(name = "violincello")
    public int getViolincello() {
        return violincello;
    }

    public void setViolincello(int violincello) {
        this.violincello = violincello;
    }

    @Basic
    @Column(name = "doublebass")
    public int getDoublebass() {
        return doublebass;
    }

    public void setDoublebass(int doublebass) {
        this.doublebass = doublebass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringInstrumentationEntity that = (StringInstrumentationEntity) o;

        if (stringInstrumentationId != that.stringInstrumentationId) return false;
        if (violin1 != that.violin1) return false;
        if (violin2 != that.violin2) return false;
        if (viola != that.viola) return false;
        if (violincello != that.violincello) return false;
        if (doublebass != that.doublebass) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stringInstrumentationId;
        result = 31 * result + violin1;
        result = 31 * result + violin2;
        result = 31 * result + viola;
        result = 31 * result + violincello;
        result = 31 * result + doublebass;
        return result;
    }
}
