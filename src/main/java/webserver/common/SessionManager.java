package webserver.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class SessionManager {
    private static final Map<String, Session> SESSIONS = new HashMap<>();

    public static void add(final Session session) {
        SESSIONS.putIfAbsent(session.getId(), session);
    }

    public static Optional<Session> findSession(final String id) {
        return Optional.ofNullable(SESSIONS.get(id));
    }

    public static void remove(final String id) {
        SESSIONS.remove(id);
    }

    private SessionManager() {
        throw new AssertionError("SessionManager 객체는 유틸리티 객체입니다.");
    }
}
