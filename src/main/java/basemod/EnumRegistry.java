package basemod;

import java.util.HashMap;
import java.util.Map;

public class EnumRegistry<TKey extends Enum<?>, TValue> {
    Map<String, TValue> registry = new HashMap<>();

    public void register(String key, TValue value) {
        registry.put(key, value);
    }

    public TValue get(TKey key) {
        return registry.getOrDefault(key.name(), null);
    }
}
