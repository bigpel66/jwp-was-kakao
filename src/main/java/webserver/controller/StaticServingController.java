package webserver.controller;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;
import java.net.URISyntaxException;

import static utils.FileIoUtils.*;
import static webserver.enums.MediaType.TEXT_CSS;
import static webserver.enums.MediaType.TEXT_HTML;

public class StaticServingController implements Controller {
    @Override
    public void doService(HttpRequest req, HttpResponse response) {
        try {
            byte[] body = getBody(req);
            String contentType = getContentType(req);
            response.setStatusOK(body, contentType);
        } catch (Exception ex) {
            response.setNotFound();
        }
    }

    private static byte[] getBody(HttpRequest httpRequest) throws IOException, URISyntaxException {
        String filePath = httpRequest.pathValue();
        if (isFileFromTemplates(filePath)) {
            return loadFileFromTemplates(filePath, httpRequest.attribute());
        } else if (isFileFromStatic(filePath)) {
            return loadFileFromStatic(filePath);
        }
        throw new IllegalArgumentException("찾을 수 없는 자원입니다.");
    }

    private static String getContentType(HttpRequest httpRequest) {
        String accept = httpRequest.accept();
        String contentType = httpRequest.contentType();
        if (accept.contains(TEXT_CSS.value())) {
            return TEXT_CSS.value();
        }
        if (!contentType.isBlank()) {
            return contentType;
        }
        return TEXT_HTML.value();
    }
}
