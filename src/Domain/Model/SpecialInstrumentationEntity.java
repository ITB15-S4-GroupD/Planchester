package Domain.Model;

import javax.persistence.*;

/**
 * Created by Bernd on 06.04.2017.
 */
@Entity
@Table(name = "SpecialInstrumentation", schema = "sem4_team2", catalog = "")
public class SpecialInstrumentationEntity {
    private int specialInstrumentationId;
    private Enum sectionType;
    private String specialInstrument;
    private int specialInstrumentationNumber;
    private InstrumentationEntity instrumentationByInstrumentationId;

    @Id
    @Column(name = "specialInstrumentationID")
    public int getSpecialInstrumentationId() {
        return specialInstrumentationId;
    }

    public void setSpecialInstrumentationId(int specialInstrumentationId) {
        this.specialInstrumentationId = specialInstrumentationId;
    }

    @Basic
    @Column(name = "sectionType")
    public Enum getSectionType() {
        return sectionType;
    }

    public void setSectionType(Enum sectionType) {
        this.sectionType = sectionType;
    }

    @Basic
    @Column(name = "specialInstrument")
    public String getSpecialInstrument() {
        return specialInstrument;
    }

    public void setSpecialInstrument(String specialInstrument) {
        this.specialInstrument = specialInstrument;
    }

    @Basic
    @Column(name = "specialInstrumentationNumber")
    public int getSpecialInstrumentationNumber() {
        return specialInstrumentationNumber;
    }

    public void setSpecialInstrumentationNumber(int specialInstrumentationNumber) {
        this.specialInstrumentationNumber = specialInstrumentationNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpecialInstrumentationEntity that = (SpecialInstrumentationEntity) o;

        if (specialInstrumentationId != that.specialInstrumentationId) return false;
        if (specialInstrumentationNumber != that.specialInstrumentationNumber) return false;
        if (sectionType != null ? !sectionType.equals(that.sectionType) : that.sectionType != null) return false;
        if (specialInstrument != null ? !specialInstrument.equals(that.specialInstrument) : that.specialInstrument != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = specialInstrumentationId;
        result = 31 * result + (sectionType != null ? sectionType.hashCode() : 0);
        result = 31 * result + (specialInstrument != null ? specialInstrument.hashCode() : 0);
        result = 31 * result + specialInstrumentationNumber;
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