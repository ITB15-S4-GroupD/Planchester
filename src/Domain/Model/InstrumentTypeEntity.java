package Domain.Model;

import javax.persistence.*;

/**
 * Created by Bernd on 06.04.2017.
 */
@Entity
@Table(name = "InstrumentType", schema = "sem4_team2", catalog = "")
public class InstrumentTypeEntity {
    private int instrumentTypeId;
    private String instrumentType;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instrumentTypeID")
    public int getInstrumentTypeId() {
        return instrumentTypeId;
    }

    public void setInstrumentTypeId(int instrumentTypeId) {
        this.instrumentTypeId = instrumentTypeId;
    }

    @Basic
    @Column(name = "instrumentType")
    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstrumentTypeEntity that = (InstrumentTypeEntity) o;

        if (instrumentTypeId != that.instrumentTypeId) return false;
        if (instrumentType != null ? !instrumentType.equals(that.instrumentType) : that.instrumentType != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = instrumentTypeId;
        result = 31 * result + (instrumentType != null ? instrumentType.hashCode() : 0);
        return result;
    }
}
