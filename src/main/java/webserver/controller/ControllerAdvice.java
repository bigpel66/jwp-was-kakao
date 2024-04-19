package webserver.controller;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

@FunctionalInterface
public interface ControllerAdvice {
    void accept(HttpRequest req, HttpResponse res, Controller controller);
}
