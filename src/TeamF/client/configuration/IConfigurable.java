package TeamF.client.configuration;

import java.util.Map;
import java.util.Set;

public interface IConfigurable {
    public Set<Map.Entry<String, Object>> getList();
}