package webserver.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.common.HttpHeaders;
import webserver.common.Uri;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HttpRequest 관련 테스트")
class HttpRequestTest {
    @Test
    void HttpRequest_객체가_생성된다() throws IOException {
        String request = "GET /index.html HTTP/1.1\r\nHost: localhost:8080\r\nConnection: keep-alive\r\nAccept: */*\r\n\r\nSampleContents";
        InputStream in1 = new ByteArrayInputStream(request.getBytes());
        InputStream in2 = new ByteArrayInputStream(request.getBytes());
        HttpRequest httpRequest = new HttpRequest(in1);
        BufferedReader br = new BufferedReader(new InputStreamReader(in2));
        assertThat(httpRequest.requestLine()).isEqualTo(new RequestLine(br));
        assertThat(httpRequest.headers()).isEqualTo(new HttpHeaders(br));
        assertThat(httpRequest.body()).isNotNull();
    }

    @Test
    void Attribute를_설정하고_읽을_수_있다() throws IOException {
        String request = "GET /index.html HTTP/1.1\r\nHost: localhost:8080\r\nConnection: keep-alive\r\nAccept: */*\r\n\r\nSampleContents";
        InputStream in = new ByteArrayInputStream(request.getBytes());
        HttpRequest httpRequest = new HttpRequest(in);
        String attribute = "attribute";
        httpRequest.setAttribute(attribute);
        assertThat(httpRequest.getAttribute().isPresent()).isTrue();
        assertThat(httpRequest.getAttribute().get()).isEqualTo(attribute);
    }

    @Test
    void Uri를_변경할_수_있다() throws IOException {
        String request = "GET /index.html HTTP/1.1\r\nHost: localhost:8080\r\nConnection: keep-alive\r\nAccept: */*\r\n\r\nSampleContents";
        InputStream in = new ByteArrayInputStream(request.getBytes());
        HttpRequest httpRequest = new HttpRequest(in);
        assertThat(httpRequest.pathValue()).isEqualTo("/index.html");
        String changedPath = "/changed";
        httpRequest.redirectUri(new Uri(changedPath));
        assertThat(httpRequest.pathValue()).isEqualTo(changedPath);
    }
}
