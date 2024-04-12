package webserver.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class QueryString {
    private final Map<String, Object> params;

    public QueryString() {
        this.params = new HashMap<>();
    }

    public void put(String key, Object value) {
        this.params.putIfAbsent(key, value);
    }

    public Optional<Object> get(String key) {
        return Optional.ofNullable(this.params.get(key));
    }

    public Map<String, Object> params() {
        return this.params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryString that = (QueryString) o;
        return Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(params);
    }
}
