package webserver.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.common.Cookie;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public final class ResponseRenderer {
    private static final Logger logger = LoggerFactory.getLogger(ResponseRenderer.class);

    private final DataOutputStream dos;

    public ResponseRenderer(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void render(HttpResponse httpResponse) {
        try {
            dos.writeBytes(httpResponse.statusLine() + "\r\n");
            writeCookie(httpResponse.cookie());
            writeHeaders(httpResponse);
            dos.writeBytes("\r\n");
            dos.write(httpResponse.contents(), 0, httpResponse.contents().length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void writeCookie(Cookie cookie) throws IOException {
        if (cookie.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : cookie.all().entrySet()) {
            dos.writeBytes("Set-Cookie: " + entry.getKey() + "=" + entry.getValue());
            dos.writeBytes("; " + Cookie.PATH_KEY + "=" + Cookie.PATH_VALUE + "\r\n");
        }
    }

    private void writeHeaders(HttpResponse httpResponse) throws IOException {
        for (Map.Entry<String, String> entry : httpResponse.headers().all().entrySet()) {
            dos.writeBytes(entry.getKey() + ": " + entry.getValue() + "\r\n");
        }
    }
}
