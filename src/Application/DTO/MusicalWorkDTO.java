package Application.DTO;

import Domain.Interfaces.IInstrumentation;
import Domain.Interfaces.IMusicalWork;

/**
 * Created by timor on 27.04.2017.
 */
public class MusicalWorkDTO implements IMusicalWork {
    private int instrumentationId;
    private int id;
    private String name;
    private String composer;
    private IInstrumentation instrumentation;
    private IInstrumentation alternativeInstrumentation;

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

    public IInstrumentation getInstrumentation() {
        return instrumentation;
    }

    public void setInstrumentation(IInstrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    public IInstrumentation getAlternativeInstrumentation() {
        return alternativeInstrumentation;
    }

    public void setAlternativeInstrumentation(IInstrumentation instrumentation) {
        this.alternativeInstrumentation = instrumentation;
    }
}