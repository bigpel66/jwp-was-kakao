package webserver.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Session 관련 테스트")
class SessionTest {
    @Test
    void Session_객체를_생성한다() {
        String uuid = UUID.randomUUID().toString();
        Session session = new Session(uuid);
        assertThat(session).isNotNull();
    }

    @Test
    void Session의_ID를_얻을_수_있다() {
        String uuid = UUID.randomUUID().toString();
        Session session = new Session(uuid);
        assertThat(session.getId()).isEqualTo(uuid);
    }

    @Test
    void Session의_값을_지정할_수_있다() {
        String uuid = UUID.randomUUID().toString();
        Session session = new Session(uuid);
        session.setAttribute("test", "testing");
        assertThat(session.attributeSize()).isEqualTo(1);
    }

    @Test
    void Session의_값을_읽을_수_있다() {
        String uuid = UUID.randomUUID().toString();
        Session session = new Session(uuid);
        String key = "test";
        String val = "testing";
        session.setAttribute(key, val);
        assertThat(session.getAttribute(key).isPresent()).isTrue();
        assertThat(session.getAttribute(key).get()).isInstanceOf(String.class);
        assertThat((String) session.getAttribute(key).get()).isEqualTo(val);
    }

    @Test
    void Session의_값을_삭제할_수_있다() {
        String uuid = UUID.randomUUID().toString();
        Session session = new Session(uuid);
        String key = "test";
        String val = "testing";
        session.setAttribute(key, val);
        session.removeAttribute(key);
        assertThat(session.attributeSize()).isEqualTo(0);
    }

    @Test
    void Session의_모든_값을_초기화_할_수_있다() {
        String uuid = UUID.randomUUID().toString();
        Session session = new Session(uuid);
        session.setAttribute("1", 1);
        session.setAttribute("2", 2);
        session.setAttribute("3", 3);
        session.setAttribute("4", 4);
        session.invalidate();
        assertThat(session.attributeSize()).isEqualTo(0);
    }
}