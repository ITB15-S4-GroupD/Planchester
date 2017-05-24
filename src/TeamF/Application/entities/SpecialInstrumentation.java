package TeamF.Application.entities;

public class SpecialInstrumentation {
    private int _id;
    private String _sectionType;
    private String _specialInstrumentation;
    private int _specialInstrumentationCount;

    public void setID(int id) {
        _id = id;
    }

    public void setSectionType(String sectionType) {
        _sectionType = sectionType;
    }

    public void setSpecialInstrumentation(String specialInstrumentation) {
        _specialInstrumentation = specialInstrumentation;
    }

    public void setSpecialInstrumentationCount(int specialInstrumentationCount) {
        _specialInstrumentationCount = specialInstrumentationCount;
    }

    public int getID() {
        return _id;
    }

    public String getSectionType() {
        return _sectionType;
    }

    public String getSpecialInstrumentation() {
        return _specialInstrumentation;
    }

    public int getspecialInstrumentationCount() {
        return _specialInstrumentationCount;
    }
}
