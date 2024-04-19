package webserver.request;

import utils.IOUtils;
import webserver.common.Cookie;
import webserver.common.HttpHeaders;
import webserver.common.Path;
import webserver.common.Uri;
import webserver.controller.RequestMapper.Entry;
import webserver.enums.Method;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static webserver.common.Cookie.JAVA_SESSION_KEY;

public final class HttpRequest {
    private static final String ZERO_STRING = "";
    private static final String ZERO_INTEGER = "0";

    private RequestLine requestLine;
    private final HttpHeaders httpHeaders;
    private final Cookie cookie;
    private final RequestBody requestBody;
    private Object attribute;

    public HttpRequest() {
        this.requestLine = null;
        this.httpHeaders = null;
        this.requestBody = null;
        this.cookie = null;
    }

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        this.requestLine = new RequestLine(br);
        this.httpHeaders = new HttpHeaders(br);
        this.cookie = new Cookie(headers().get(HttpHeaders.COOKIE).orElse(ZERO_STRING));
        if (!isRequestBodyAcceptable()) {
            this.requestBody = new RequestBody();
            return;
        }
        this.requestBody = new RequestBody(RequestBodyParsingStrategyFactory.create(
                contentType(),
                IOUtils.readData(br, contentLength())));
    }

    private boolean isRequestBodyAcceptable() {
        Method method = requestLine.method();
        return method.isRequestBodyAcceptable();
    }

    public boolean requiresInitialCookie(String path) {
        return !cookie().all().containsKey(Cookie.JAVA_SESSION_KEY) &&
                Arrays.stream(Entry.STATIC_SORTED_ENTRY).noneMatch(path::startsWith);
    }

    public String accept() {
        return this.httpHeaders.accept().orElse(ZERO_STRING);
    }

    public String contentType() {
        return this.httpHeaders.contentType().orElse(ZERO_STRING);
    }

    public int contentLength() {
        return Integer.parseInt(this.httpHeaders.contentLength().orElse(ZERO_INTEGER));
    }

    public RequestLine requestLine() {
        return this.requestLine;
    }

    public HttpHeaders headers() {
        return this.httpHeaders;
    }

    public Cookie cookie() {
        return this.cookie;
    }

    public String sessionId() {
        return this.cookie.all().getOrDefault(JAVA_SESSION_KEY, ZERO_STRING);
    }

    public void putCookie(String key, String value) {
        this.cookie.put(key, value);
    }

    public RequestBody body() {
        return this.requestBody;
    }

    public Method method() {
        return this.requestLine.method();
    }

    public Path path() {
        return this.requestLine.uri().path();
    }

    public String pathValue() {
        return this.requestLine.uri().path().value();
    }

    public Map<String, Object> params() {
        return this.requestLine().uri().queryString().params();
    }

    public Map<String, Object> contents() {
        return this.requestBody.contents();
    }

    public String httpVersion() {
        return this.requestLine.httpVersion().raw();
    }

    public Optional<Object> getAttribute() {
        return Optional.ofNullable(this.attribute);
    }

    public void setAttribute(Object attribute) {
        this.attribute = attribute;
    }

    public void redirectUri(Uri uri) {
        this.requestLine = new RequestLine(this.requestLine, uri);
    }
}
