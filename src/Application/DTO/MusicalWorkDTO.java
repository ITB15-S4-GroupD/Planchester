package Application.DTO;

/**
 * Created by timor on 27.04.2017.
 */
public class MusicalWorkDTO {

    private String name;
    private String composer;
    private InstrumentationDTO instrumentation;
    private InstrumentationDTO alternativeInstrumentation;

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

    public InstrumentationDTO getInstrumentation() {
        return instrumentation;
    }

    public void setInstrumentation(InstrumentationDTO instrumentation) {
        this.instrumentation = instrumentation;
    }

    public InstrumentationDTO getAlternativeInstrumentation() {
        return alternativeInstrumentation;
    }

    public void setAlternativeInstrumentation(InstrumentationDTO instrumentation) {
        this.alternativeInstrumentation = instrumentation;
    }
}