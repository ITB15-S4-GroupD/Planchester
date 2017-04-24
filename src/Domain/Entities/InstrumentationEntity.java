package Domain.Entities;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Bernd on 06.04.2017.
 */
@Entity
@Table(name = "Instrumentation", schema = "sem4_team2", catalog = "")
public class InstrumentationEntity {
    private int instrumentationId;
    private int stringInstrumentation;
    private int woodInstrumentation;
    private int brassInstrumentation;
    private int percussionInstrumentation;
    private Collection<EventDutyEntity> eventDutiesByInstrumentationId;
    private Collection<EventDutyMusicalWorkEntity> eventDutyMusicalWorksByInstrumentationId;
    private StringInstrumentationEntity stringInstrumentationByStringInstrumentation;
    private WoodInstrumentationEntity woodInstrumentationByWoodInstrumentation;
    private BrassInstrumentationEntity brassInstrumentationByBrassInstrumentation;
    private PercussionInstrumentationEntity percussionInstrumentationByPercussionInstrumentation;
    private Collection<MusicalWorkEntity> musicalWorksByInstrumentationId;
    private Collection<SpecialInstrumentationEntity> specialInstrumentationsByInstrumentationId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instrumentationID", nullable = false)
    public int getInstrumentationId() {
        return instrumentationId;
    }

    public void setInstrumentationId(int instrumentationId) {
        this.instrumentationId = instrumentationId;
    }

    @Basic
    @Column(name = "stringInstrumentation", nullable = false)
    public int getStringInstrumentation() {
        return stringInstrumentation;
    }

    public void setStringInstrumentation(int stringInstrumentation) {
        this.stringInstrumentation = stringInstrumentation;
    }

    @Basic
    @Column(name = "woodInstrumentation", nullable = false)
    public int getWoodInstrumentation() {
        return woodInstrumentation;
    }

    public void setWoodInstrumentation(int woodInstrumentation) {
        this.woodInstrumentation = woodInstrumentation;
    }

    @Basic
    @Column(name = "brassInstrumentation", nullable = false)
    public int getBrassInstrumentation() {
        return brassInstrumentation;
    }

    public void setBrassInstrumentation(int brassInstrumentation) {
        this.brassInstrumentation = brassInstrumentation;
    }

    @Basic
    @Column(name = "percussionInstrumentation", nullable = false)
    public int getPercussionInstrumentation() {
        return percussionInstrumentation;
    }

    public void setPercussionInstrumentation(int percussionInstrumentation) {
        this.percussionInstrumentation = percussionInstrumentation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstrumentationEntity that = (InstrumentationEntity) o;

        if (instrumentationId != that.instrumentationId) return false;
        if (stringInstrumentation != that.stringInstrumentation) return false;
        if (woodInstrumentation != that.woodInstrumentation) return false;
        if (brassInstrumentation != that.brassInstrumentation) return false;
        if (percussionInstrumentation != that.percussionInstrumentation) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = instrumentationId;
        result = 31 * result + stringInstrumentation;
        result = 31 * result + woodInstrumentation;
        result = 31 * result + brassInstrumentation;
        result = 31 * result + percussionInstrumentation;
        return result;
    }

    @OneToMany(mappedBy = "instrumentationByInstrumentation")
    public Collection<EventDutyEntity> getEventDutiesByInstrumentationId() {
        return eventDutiesByInstrumentationId;
    }

    public void setEventDutiesByInstrumentationId(Collection<EventDutyEntity> eventDutiesByInstrumentationId) {
        this.eventDutiesByInstrumentationId = eventDutiesByInstrumentationId;
    }

    @OneToMany(mappedBy = "instrumentationByAlternativeInstrumentation")
    public Collection<EventDutyMusicalWorkEntity> getEventDutyMusicalWorksByInstrumentationId() {
        return eventDutyMusicalWorksByInstrumentationId;
    }

    public void setEventDutyMusicalWorksByInstrumentationId(Collection<EventDutyMusicalWorkEntity> eventDutyMusicalWorksByInstrumentationId) {
        this.eventDutyMusicalWorksByInstrumentationId = eventDutyMusicalWorksByInstrumentationId;
    }

    @ManyToOne
    @JoinColumn(name = "stringInstrumentation", referencedColumnName = "stringInstrumentationID", nullable = false, insertable = false, updatable = false)
    public StringInstrumentationEntity getStringInstrumentationByStringInstrumentation() {
        return stringInstrumentationByStringInstrumentation;
    }

    public void setStringInstrumentationByStringInstrumentation(StringInstrumentationEntity stringInstrumentationByStringInstrumentation) {
        this.stringInstrumentationByStringInstrumentation = stringInstrumentationByStringInstrumentation;
    }

    @ManyToOne
    @JoinColumn(name = "woodInstrumentation", referencedColumnName = "woodInstrumentationID", nullable = false, insertable = false, updatable = false)
    public WoodInstrumentationEntity getWoodInstrumentationByWoodInstrumentation() {
        return woodInstrumentationByWoodInstrumentation;
    }

    public void setWoodInstrumentationByWoodInstrumentation(WoodInstrumentationEntity woodInstrumentationByWoodInstrumentation) {
        this.woodInstrumentationByWoodInstrumentation = woodInstrumentationByWoodInstrumentation;
    }

    @ManyToOne
    @JoinColumn(name = "brassInstrumentation", referencedColumnName = "brassInstrumentationID", nullable = false, insertable = false, updatable = false)
    public BrassInstrumentationEntity getBrassInstrumentationByBrassInstrumentation() {
        return brassInstrumentationByBrassInstrumentation;
    }

    public void setBrassInstrumentationByBrassInstrumentation(BrassInstrumentationEntity brassInstrumentationByBrassInstrumentation) {
        this.brassInstrumentationByBrassInstrumentation = brassInstrumentationByBrassInstrumentation;
    }

    @ManyToOne
    @JoinColumn(name = "percussionInstrumentation", referencedColumnName = "percussionInstrumentationID", nullable = false, insertable = false, updatable = false)
    public PercussionInstrumentationEntity getPercussionInstrumentationByPercussionInstrumentation() {
        return percussionInstrumentationByPercussionInstrumentation;
    }

    public void setPercussionInstrumentationByPercussionInstrumentation(PercussionInstrumentationEntity percussionInstrumentationByPercussionInstrumentation) {
        this.percussionInstrumentationByPercussionInstrumentation = percussionInstrumentationByPercussionInstrumentation;
    }

    @OneToMany(mappedBy = "instrumentationByInstrumentationId")
    public Collection<MusicalWorkEntity> getMusicalWorksByInstrumentationId() {
        return musicalWorksByInstrumentationId;
    }

    public void setMusicalWorksByInstrumentationId(Collection<MusicalWorkEntity> musicalWorksByInstrumentationId) {
        this.musicalWorksByInstrumentationId = musicalWorksByInstrumentationId;
    }

    @OneToMany(mappedBy = "instrumentationByInstrumentationId")
    public Collection<SpecialInstrumentationEntity> getSpecialInstrumentationsByInstrumentationId() {
        return specialInstrumentationsByInstrumentationId;
    }

    public void setSpecialInstrumentationsByInstrumentationId(Collection<SpecialInstrumentationEntity> specialInstrumentationsByInstrumentationId) {
        this.specialInstrumentationsByInstrumentationId = specialInstrumentationsByInstrumentationId;
    }
}