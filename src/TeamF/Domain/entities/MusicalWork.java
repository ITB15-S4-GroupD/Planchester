package TeamF.Domain.entities;

import TeamF.Domain.interfaces.DomainEntity;

public class MusicalWork implements DomainEntity {
    private int _musicalWorkID;
    private Instrumentation _instrumentation;
    private String _name;
    private String _composer;
    private Instrumentation _alternativeInstrumentation;

    public int getMusicalWorkID() {
        return _musicalWorkID;
    }

    public Instrumentation getInstrumentation() {
        return _instrumentation;
    }

    public String getName() {
        return _name;
    }

    public String getComposer() {
        return _composer;
    }

    public Instrumentation getAlternativeInstrumentation() {
        return _alternativeInstrumentation;
    }

    public void setMusicalWorkID(int musicalWorkID) {
        _musicalWorkID = musicalWorkID;
    }

    public void setInstrumentation(Instrumentation instrumentation) {
        _instrumentation = instrumentation;
    }

    public void setName(String name) {
        _name = name;
    }

    public void setComposer(String composer) {
        _composer = composer;
    }

    public void setAlternativeInstrumentation(Instrumentation alternativeInstrumentation) {
        _alternativeInstrumentation = alternativeInstrumentation;
    }

    @Override
    public int getID() {
        return getMusicalWorkID();
    }
}
