package webserver.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class Session {
    public static final String USER_FIELD = "user";
    
    private final String id;
    private final Map<String, Object> values;

    public Session(final String id) {
        this.id = id;
        this.values = new HashMap<>();
    }

    public String getId() {
        return this.id;
    }

    public int attributeSize() {
        return this.values.size();
    }

    public Optional<Object> getAttribute(final String name) {
        return Optional.ofNullable(this.values.get(name));
    }

    public void setAttribute(final String name, final Object value) {
        this.values.putIfAbsent(name, value);
    }

    public void removeAttribute(final String name) {
        this.values.remove(name);
    }

    public void invalidate() {
        this.values.clear();
    }
}
