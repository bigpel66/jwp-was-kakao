package webserver.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class HttpHeaders {
    public static final String ACCEPT = "Accept";
    public static final String LOCATION = "Location";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    private static final String HEADER_DELIMITER = ": ";

    private Map<String, String> headers;

    public HttpHeaders(BufferedReader br) {
        try {
            (new Parser(br)).parse();
        } catch (IOException e) {
            throw new IllegalArgumentException("Http 헤더를 파싱할 수 없습니다.");
        }
    }

    public HttpHeaders(String headers) {
        this(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(headers.getBytes()))));
    }

    public HttpHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Optional<String> accept() {
        return get(ACCEPT);
    }

    public Optional<String> contentType() {
        return get(CONTENT_TYPE);
    }

    public Optional<String> contentLength() {
        return get(CONTENT_LENGTH);
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(headers.get(key));
    }

    public void put(String key, String value) {
        this.headers.putIfAbsent(key, value);
    }

    public Map<String, String> all() {
        return this.headers;
    }

    private final class Parser {
        private static final int KEY_INDEX = 0;
        private static final int VALUE_INDEX = 1;

        private final BufferedReader br;

        Parser(BufferedReader br) {
            this.br = br;
        }

        void parse() throws IOException {
            HttpHeaders.this.headers = new HashMap<>();
            String line = br.readLine();
            while (line != null && !line.isBlank()) {
                putHeader(line);
                line = br.readLine();
            }
        }

        private void putHeader(String line) {
            String[] keyValue = line.split(HEADER_DELIMITER);
            HttpHeaders.this.headers.put(keyValue[KEY_INDEX], keyValue[VALUE_INDEX]);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpHeaders headers1 = (HttpHeaders) o;
        return Objects.equals(headers, headers1.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headers);
    }
}
