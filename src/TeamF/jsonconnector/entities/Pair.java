package TeamF.jsonconnector.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pair<K, V> {
    private K _key;
    private V _value;

    @JsonGetter("key")
    public K getKey() {
        return _key;
    }

    @JsonGetter("value")
    public V getValue() {
        return _value;
    }

    @JsonSetter("key")
    public void setKey(K key) {
        _key = key;
    }

    @JsonSetter("value")
    public void setValue(V value) {
        _value = value;
    }
}
