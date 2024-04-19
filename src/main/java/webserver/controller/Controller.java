package webserver.controller;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;

@FunctionalInterface
public interface Controller {
    void doService(HttpRequest req, HttpResponse res) throws IOException;
}
