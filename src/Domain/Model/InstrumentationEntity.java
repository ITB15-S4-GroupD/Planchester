package Domain.Model;

import javax.persistence.*;

/**
 * Created by Bernd on 06.04.2017.
 */
@Entity
@Table(name = "Instrumentation", schema = "sem4_team2", catalog = "")
public class InstrumentationEntity {
    private int instrumentationId;
    private BrassInstrumentationEntity brassInstrumentationByBrassInstrumentation;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instrumentationID")
    public int getInstrumentationId() {
        return instrumentationId;
    }

    public void setInstrumentationId(int instrumentationId) {
        this.instrumentationId = instrumentationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstrumentationEntity that = (InstrumentationEntity) o;

        if (instrumentationId != that.instrumentationId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return instrumentationId;
    }

    @ManyToOne
    @JoinColumn(name = "brassInstrumentation", referencedColumnName = "brassInstrumentationID", nullable = false)
    public BrassInstrumentationEntity getBrassInstrumentationByBrassInstrumentation() {
        return brassInstrumentationByBrassInstrumentation;
    }

    public void setBrassInstrumentationByBrassInstrumentation(BrassInstrumentationEntity brassInstrumentationByBrassInstrumentation) {
        this.brassInstrumentationByBrassInstrumentation = brassInstrumentationByBrassInstrumentation;
    }
}
