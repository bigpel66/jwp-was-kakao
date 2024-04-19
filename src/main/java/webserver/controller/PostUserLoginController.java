package webserver.controller;

import db.DataBase;
import model.User;
import webserver.common.Session;
import webserver.common.SessionManager;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.util.Optional;

import static webserver.controller.RequestMapper.Entry.DEFAULT_ENTRY;
import static webserver.controller.RequestMapper.Entry.LOGIN_FAILED_ENTRY;
import static webserver.dto.UserDto.passwordField;
import static webserver.dto.UserDto.userIdField;

public final class PostUserLoginController implements Controller {
    @Override
    public void doService(HttpRequest req, HttpResponse res) {
        if (SessionManager.findSession(req.sessionId()).isPresent()) {
            res.setMovedTemporarily(DEFAULT_ENTRY);
            return;
        }
        Optional<User> user = login(req, res);
        user.ifPresent(value -> recordLoginOnSession(req, res, value));
    }

    private Optional<User> login(HttpRequest req, HttpResponse res) {
        if (loginFieldNotExists(req)) {
            res.setMovedTemporarily(LOGIN_FAILED_ENTRY);
            return Optional.empty();
        }
        User user = DataBase.findUserById((String) req.body().get(userIdField));
        if (user == null || !user.getPassword().equals(req.body().get(passwordField))) {
            res.setMovedTemporarily(LOGIN_FAILED_ENTRY);
            return Optional.empty();
        }
        return Optional.of(user);
    }

    private boolean loginFieldNotExists(HttpRequest req) {
        return !req.body().contains(userIdField)
                || !req.body().contains(passwordField);
    }

    private void recordLoginOnSession(HttpRequest req, HttpResponse res, User user) {
        Session session = new Session(req.sessionId());
        session.setAttribute(Session.USER_FIELD, user);
        SessionManager.add(session);
        res.setMovedTemporarily(DEFAULT_ENTRY);
    }
}
