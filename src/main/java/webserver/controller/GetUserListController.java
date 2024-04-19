package webserver.controller;

import db.DataBase;
import model.User;
import webserver.common.Session;
import webserver.common.SessionManager;
import webserver.common.Uri;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static webserver.controller.RequestMapper.Entry.LOGIN_ENTRY;
import static webserver.controller.RequestMapper.Entry.USER_LIST_ENTRY;

public final class GetUserListController implements Controller {
    @Override
    public void doService(HttpRequest req, HttpResponse res) throws IOException {
        if (isLoggedIn(req)) {
            req.redirectUri(new Uri(USER_LIST_ENTRY));
            req.setAttribute(new ArrayList<>(DataBase.findAll()));
            RequestMapper.staticController().doService(req, res);
            return;
        }
        res.setMovedTemporarily(LOGIN_ENTRY);
    }

    private boolean isLoggedIn(HttpRequest req) {
        Optional<Session> wrappedSession = SessionManager.findSession(req.sessionId());
        if (wrappedSession.isEmpty()) {
            return false;
        }
        Optional<Object> wrappedUser = wrappedSession.get().getAttribute(Session.USER_FIELD);
        if (wrappedUser.isEmpty()) {
            return false;
        }
        User user = (User) wrappedUser.get();
        return user.isLegitimate();
    }
}
