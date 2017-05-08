package Domain.Models;

/**
 * Created by timor on 27.04.2017.
 */
public class MusicalWorkModel {
    private int id;
    private int instrumentationId;
    private String name;
    private String composer;
    private InstrumentationModel instrumentation;
    private InstrumentationModel alternativeInstrumentation;

    public int getInstrumentationId() {
        return instrumentationId;
    }

    public void setInstrumentationId(int instrumentationId) {
        this.instrumentationId = instrumentationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public InstrumentationModel getInstrumentation() {
        return instrumentation;
    }

    public void setInstrumentation(InstrumentationModel instrumentation) {
        this.instrumentation = instrumentation;
    }

    public InstrumentationModel getAlternativeInstrumentation() {
        return alternativeInstrumentation;
    }

    public void setAlternativeInstrumentation(InstrumentationModel instrumentation) {
        this.alternativeInstrumentation = instrumentation;
    }
}