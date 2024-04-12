package webserver.common;

import java.util.Objects;

public final class Path {
    private final String value;

    private Path(String value) {
        this.value = value;
    }

    public static Path of(String value) {
        return new Path(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return Objects.equals(value, path.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return this.value;
    }
}
