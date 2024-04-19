package webserver.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Cookie 관련 테스트")
class CookieTest {
    @Test
    void Cookie의_빈_객체를_생성한다() {
        Cookie cookie = new Cookie();
        assertThat(cookie).isNotNull();
    }

    @Test
    void Cookie의_기존_엔트리와_새로운_key_value로_생성한다() {
        Cookie cookie = new Cookie("A=b; C=d ; E=f");
        cookie.put("n", "m");
        assertThat(cookie).isNotNull();
    }

    @Test
    void Cookie의_Map을_반환환다() {
        Cookie cookie = new Cookie("A=b; C=d ; Path=/");
        assertThat(cookie.all().size()).isEqualTo(3);
    }

    @Test
    void Cookie의_빈_값_여부를_확인한다() {
        Cookie cookie = new Cookie();
        assertThat(cookie.isEmpty()).isTrue();
    }
}