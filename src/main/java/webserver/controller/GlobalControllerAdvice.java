package webserver.controller;

import webserver.enums.MediaType;
import webserver.exception.BadRequestException;
import webserver.exception.ConflictException;
import webserver.exception.ForbiddenException;
import webserver.exception.NotFoundException;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.util.UUID;

import static webserver.common.Cookie.JAVA_SESSION_KEY;

public final class GlobalControllerAdvice implements ControllerAdvice {
    @Override
    public void accept(HttpRequest req, HttpResponse res, Controller controller) {
        try {
            if (req.requiresInitialCookie(req.pathValue())) {
                req.putCookie(JAVA_SESSION_KEY, UUID.randomUUID().toString());
            }
            controller.doService(req, res);
            res.setCookie(req.cookie());
        } catch (BadRequestException ex) {
            res.setBadRequest(ex.getMessage().getBytes(), MediaType.TEXT_PLAIN.value());
        } catch (ForbiddenException ex) {
            res.setForbidden(ex.getMessage().getBytes(), MediaType.TEXT_PLAIN.value());
        } catch (NotFoundException ex) {
            res.setNotFound();
        } catch (ConflictException ex) {
            res.setConflict(ex.getMessage().getBytes(), MediaType.TEXT_PLAIN.value());
        } catch (Exception ex) {
            res.setNotFound();
        }
    }
}
