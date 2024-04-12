package webserver.request;

import utils.IOUtils;
import webserver.common.HttpHeaders;
import webserver.common.Path;
import webserver.enums.Method;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public final class HttpRequest {
    private static final String ZERO_STRING = "";
    private static final String ZERO_INTEGER = "0";

    private final RequestLine requestLine;
    private final HttpHeaders httpHeaders;
    private final RequestBody requestBody;
    private Object attribute;

    public HttpRequest() {
        this.requestLine = null;
        this.httpHeaders = null;
        this.requestBody = null;
    }

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        this.requestLine = new RequestLine(br);
        this.httpHeaders = new HttpHeaders(br);
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

    public Object attribute() {
        return attribute;
    }

    public void setAttribute(Object attribute) {
        this.attribute = attribute;
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

    public RequestBody requestBody() {
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
}
