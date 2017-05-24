package TeamF.client.pages.instrumentationmanagement;

import TeamF.jsonconnector.entities.Instrumentation;

public class InstrumentationParameter {
    private Instrumentation _instrumentation;

    public Instrumentation getInstrumentation(){
        return _instrumentation;
    }

    public void setInstrumentation(Instrumentation instrumentation){
        _instrumentation = instrumentation;
    }
}
