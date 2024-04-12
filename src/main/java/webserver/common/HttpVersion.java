package webserver.common;

import java.util.Objects;

public final class HttpVersion {
    public static final String HTTP_1_1_RAW = "HTTP/1.1";
    public static final HttpVersion HTTP_1_1 = new HttpVersion(HTTP_1_1_RAW);
    private static final String SCHEME_DELIMITER = "/";
    private static final String VERSION_DELIMITER = "\\.";
    private static final int SCHEME_INDEX = 0;
    private static final int VERSION_INDEX = 1;
    private static final int MAJOR_INDEX = 0;
    private static final int MINOR_INDEX = 1;

    private String scheme;
    private int major;
    private int minor;
    private final String raw;

    private HttpVersion(String version) {
        (new Parser(version)).parse();
        this.raw = version;
    }

    public static HttpVersion of(String raw) {
        if (HTTP_1_1_RAW.equals(raw)) {
            return HTTP_1_1;
        }
        throw new UnsupportedOperationException("지원하지 않는 HTTP 버전입니다.");
    }

    public String scheme() {
        return this.scheme;
    }

    public int major() {
        return this.major;
    }

    public int minor() {
        return this.minor;
    }

    public String raw() {
        return this.raw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpVersion that = (HttpVersion) o;
        return Objects.equals(raw, that.raw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(raw);
    }

    private final class Parser {
        private final String version;

        Parser(String version) {
            this.version = version;
        }

        void parse() {
            String[] chunks = version.split(SCHEME_DELIMITER);
            validateSchemeChunk(chunks);
            HttpVersion.this.scheme = chunks[SCHEME_INDEX];
            String[] versions = validateVersionChunks(chunks);
            HttpVersion.this.major = Integer.parseInt(versions[MAJOR_INDEX]);
            HttpVersion.this.minor = Integer.parseInt(versions[MINOR_INDEX]);
        }

        private void validateSchemeChunk(String[] chunks) {
            checkExistence(chunks);
            checkCaseSensitive(chunks[SCHEME_INDEX]);
        }

        private void checkExistence(String[] chunks) {
            if (chunks.length < 1) {
                throw new IllegalArgumentException("스킴이 존재하지 않습니다.");
            }
        }

        private void checkCaseSensitive(String scheme) {
            if (!scheme.toUpperCase().equals(scheme)) {
                throw new IllegalArgumentException("스킴은 대문자로 구성되어야 합니다.");
            }
        }

        private String[] validateVersionChunks(String[] chunks) {
            checkChunkLength(chunks);
            String[] versions = chunks[VERSION_INDEX].split(VERSION_DELIMITER);
            checkVersionsLength(versions);
            return versions;
        }

        private void checkChunkLength(String[] chunks) {
            if (chunks.length < 2) {
                throw new IllegalArgumentException("버전 필드를 확인할 수 없습니다.");
            }
        }

        private void checkVersionsLength(String[] versions) {
            if (versions.length != 2) {
                throw new IllegalArgumentException("메이저 버전과 마이너 버전을 찾을 수 없습니다.");
            }
        }
    }
}
