package Domain.Interfaces;

import Application.DTO.InstrumentationDTO;

/**
 * Created by julia on 08.05.2017.
 */
public interface IMusicalWork {
    int getInstrumentationId();
    void setInstrumentationId(int instrumentationId);
    int getId();
    void setId(int id);
    String getName();
    void setName(String name);
    String getComposer();
    void setComposer(String composer);
    IInstrumentation getInstrumentation();
    void setInstrumentation(IInstrumentation instrumentation);
    IInstrumentation getAlternativeInstrumentation();
    void setAlternativeInstrumentation(IInstrumentation instrumentation);
}
