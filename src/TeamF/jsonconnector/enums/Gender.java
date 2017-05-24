package TeamF.jsonconnector.enums;

public enum Gender {
    MALE("m"),
    FEMALE("w");

    private String _value;

    private Gender(String value) {
        _value = value;
    }

    @Override
    public String toString() {
        return _value;
    }
}