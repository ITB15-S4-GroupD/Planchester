package TeamF.Hibernate.entities;

import javax.persistence.*;

@Entity
@Table(name = "Instrument", schema = "sem4_team2", catalog = "")
public class InstrumentEntity {
    private int instrumentId;
    private int instrumentType;
    private String brand;
    private String model;
    private String description;
    private Integer musician;
    private InstrumentTypeEntity instrumentTypeByInstrumentType;
    private PersonEntity personByMusician;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instrumentID", nullable = false)
    public int getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(int instrumentId) {
        this.instrumentId = instrumentId;
    }

    @Basic
    @Column(name = "instrumentType", nullable = false)
    public int getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(int instrumentType) {
        this.instrumentType = instrumentType;
    }

    @Basic
    @Column(name = "brand", nullable = true, length = 255)
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Basic
    @Column(name = "model", nullable = true, length = 255)
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "musician", nullable = true)
    public Integer getMusician() {
        return musician;
    }

    public void setMusician(Integer musician) {
        this.musician = musician;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstrumentEntity that = (InstrumentEntity) o;

        if (instrumentId != that.instrumentId) return false;
        if (instrumentType != that.instrumentType) return false;
        if (brand != null ? !brand.equals(that.brand) : that.brand != null) return false;
        if (model != null ? !model.equals(that.model) : that.model != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (musician != null ? !musician.equals(that.musician) : that.musician != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = instrumentId;
        result = 31 * result + instrumentType;
        result = 31 * result + (brand != null ? brand.hashCode() : 0);
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (musician != null ? musician.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "instrumentType", referencedColumnName = "instrumentTypeID", nullable = false, insertable = false, updatable = false)
    public InstrumentTypeEntity getInstrumentTypeByInstrumentType() {
        return instrumentTypeByInstrumentType;
    }

    public void setInstrumentTypeByInstrumentType(InstrumentTypeEntity instrumentTypeByInstrumentType) {
        this.instrumentTypeByInstrumentType = instrumentTypeByInstrumentType;
    }

    @ManyToOne
    @JoinColumn(name = "musician", referencedColumnName = "personId", insertable = false, updatable = false)
    public PersonEntity getPersonByMusician() {
        return personByMusician;
    }

    public void setPersonByMusician(PersonEntity personByMusician) {
        this.personByMusician = personByMusician;
    }
}
