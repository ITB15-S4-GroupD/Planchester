package TeamF.client.entities;

public class KeyValuePair {
    private String _key;
    private Object _value;

    public KeyValuePair(String key, Object value) {
        _key = key;
        _value = value;
    }

    public String getKey() {
        return _key;
    }

    public Object getValue() {
        return _value;
    }

    public void setKey(String key) {
        _key = key;
    }

    public void setValue(Object value) {
        _value = value;
    }

    @Override
    public String toString() {
        return getKey();
    }
}
