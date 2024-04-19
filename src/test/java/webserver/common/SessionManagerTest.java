package webserver.common;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class SessionManagerTest {
    @Test
    void Session객체를_추가할_수_있다() {
        String uuid = UUID.randomUUID().toString();
        Session session = new Session(uuid);
        assertThatCode(() -> SessionManager.add(session)).doesNotThrowAnyException();
    }

    @Test
    void findSession() {
        String uuid = UUID.randomUUID().toString();
        Session session = new Session(uuid);
        assertThatCode(() -> SessionManager.add(session)).doesNotThrowAnyException();
        Optional<Session> sessionFromManager = SessionManager.findSession(session.getId());
        assertThat(sessionFromManager.isPresent()).isTrue();
        assertThat(sessionFromManager.get().getId()).isEqualTo(session.getId());
    }

    @Test
    void remove() {
        String uuid = UUID.randomUUID().toString();
        Session session = new Session(uuid);
        assertThatCode(() -> SessionManager.add(session)).doesNotThrowAnyException();
        SessionManager.remove(session.getId());
        assertThat(SessionManager.findSession(session.getId()).isEmpty()).isTrue();
    }
}