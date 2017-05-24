package TeamF.Hibernate.entities;

import javax.persistence.*;

@Entity
@Table(name = "EventDuty_MusicalWork", schema = "sem4_team2", catalog = "")
@IdClass(EventDutyMusicalWorkEntityPK.class)
public class EventDutyMusicalWorkEntity {
    private int eventDuty;
    private int musicalWork;
    private Integer alternativeInstrumentation;
    private EventDutyEntity eventDutyByEventDuty;
    private MusicalWorkEntity musicalWorkByMusicalWork;
    private InstrumentationEntity instrumentationByAlternativeInstrumentation;

    @Id
    @Column(name = "eventDuty", nullable = false)
    public int getEventDuty() {
        return eventDuty;
    }

    public void setEventDuty(int eventDuty) {
        this.eventDuty = eventDuty;
    }

    @Id
    @Column(name = "musicalWork", nullable = false)
    public int getMusicalWork() {
        return musicalWork;
    }

    public void setMusicalWork(int musicalWork) {
        this.musicalWork = musicalWork;
    }

    @Basic
    @Column(name = "alternativeInstrumentation", nullable = true)
    public Integer getAlternativeInstrumentation() {
        return alternativeInstrumentation;
    }

    public void setAlternativeInstrumentation(Integer alternativeInstrumentation) {
        this.alternativeInstrumentation = alternativeInstrumentation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventDutyMusicalWorkEntity that = (EventDutyMusicalWorkEntity) o;

        if (eventDuty != that.eventDuty) return false;
        if (musicalWork != that.musicalWork) return false;
        if (alternativeInstrumentation != null ? !alternativeInstrumentation.equals(that.alternativeInstrumentation) : that.alternativeInstrumentation != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = eventDuty;
        result = 31 * result + musicalWork;
        result = 31 * result + (alternativeInstrumentation != null ? alternativeInstrumentation.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "eventDuty", referencedColumnName = "eventDutyID", nullable = false, insertable = false, updatable = false)
    public EventDutyEntity getEventDutyByEventDuty() {
        return eventDutyByEventDuty;
    }

    public void setEventDutyByEventDuty(EventDutyEntity eventDutyByEventDuty) {
        this.eventDutyByEventDuty = eventDutyByEventDuty;
    }

    @ManyToOne
    @JoinColumn(name = "musicalWork", referencedColumnName = "musicalWorkID", nullable = false, insertable = false, updatable = false)
    public MusicalWorkEntity getMusicalWorkByMusicalWork() {
        return musicalWorkByMusicalWork;
    }

    public void setMusicalWorkByMusicalWork(MusicalWorkEntity musicalWorkByMusicalWork) {
        this.musicalWorkByMusicalWork = musicalWorkByMusicalWork;
    }

    @ManyToOne
    @JoinColumn(name = "alternativeInstrumentation", referencedColumnName = "instrumentationID", insertable = false, updatable = false)
    public InstrumentationEntity getInstrumentationByAlternativeInstrumentation() {
        return instrumentationByAlternativeInstrumentation;
    }

    public void setInstrumentationByAlternativeInstrumentation(InstrumentationEntity instrumentationByAlternativeInstrumentation) {
        this.instrumentationByAlternativeInstrumentation = instrumentationByAlternativeInstrumentation;
    }
}
