package webserver.common;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Uri {
    private static final Pattern PATH_PATTERN = Pattern.compile("^/([\\w\\d\\-._~%!$&'()*+,;=:@/]*)$");
    private static final Pattern PARAM_PATTERN = Pattern.compile("^([\\w\\d가-힣\\-._%$'()=@/]+=[\\w\\d가-힣\\-._%$'()=@/]+)$");
    private static final String DEFAULT_PATH = "/";
    private static final String QUERY_DELIMITER = "\\?";
    private static final String ENTRY_DELIMITER = "&";
    private static final String FIELD_DELIMITER = "=";

    private Path path;
    private QueryString queryString;

    public Uri(String uri) {
        (new Parser(uri)).parse();
    }

    public Path path() {
        return this.path;
    }

    public QueryString queryString() {
        return this.queryString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Uri uri = (Uri) o;
        return Objects.equals(path, uri.path) && Objects.equals(queryString, uri.queryString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, queryString);
    }

    private final class Parser {
        private static final int PATH_INDEX = 0;
        private static final int PARAM_INDEX = 1;
        private static final int KEY_INDEX = 0;
        private static final int VALUE_INDEX = 1;

        private final String uri;

        Parser(String uri) {
            Objects.requireNonNull(uri);
            this.uri = uri;
        }

        void parse() {
            Uri.this.queryString = new QueryString();
            String[] chunks = this.uri.split(QUERY_DELIMITER);
            if (chunks[PATH_INDEX].isBlank()) {
                chunks[PATH_INDEX] = DEFAULT_PATH;
            }
            validateAndExtractPath(chunks[PATH_INDEX]);
            validateAndExtractParams(chunks);
        }

        private void validateAndExtractPath(String path) {
            Matcher matcher = PATH_PATTERN.matcher(path);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Path 형태가 올바르지 않습니다.");
            }
            Uri.this.path = Path.of(path);
        }

        private void validateAndExtractParams(String[] chunks) {
            if (chunks.length > 2) {
                throw new IllegalArgumentException("쿼리 파라미터의 구분자는 1개만 주어져야 합니다.");
            }
            if (chunks.length == 1) {
                return;
            }
            extractParams(chunks);
        }

        private void extractParams(String[] chunks) {
            String rawParams = URLDecoder.decode(chunks[PARAM_INDEX], StandardCharsets.UTF_8);
            if (rawParams.endsWith(ENTRY_DELIMITER)) {
                throw new IllegalArgumentException("쿼리 파라미터의 구성이 완전하지 않습니다.");
            }
            String[] params = rawParams.split(ENTRY_DELIMITER);
            Arrays.stream(params).forEach(e -> {
                Matcher matcher = Uri.PARAM_PATTERN.matcher(e);
                if (!matcher.matches()) {
                    throw new IllegalArgumentException("쿼리 파라미터 키, 값 구분이 올바르지 않습니다.");
                }
                putParam(e);
            });
        }

        private void putParam(String param) {
            String[] keyValue = param.split(FIELD_DELIMITER);
            Uri.this.queryString.put(keyValue[KEY_INDEX], keyValue[VALUE_INDEX]);
        }
    }
}
