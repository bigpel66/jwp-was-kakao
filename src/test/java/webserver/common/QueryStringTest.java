package webserver.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("QueryString 관련 테스트")
class QueryStringTest {
    @Test
    void QueryString을_생성한다() {
        QueryString qs = new QueryString();
        assertThat(qs).isNotNull();
    }

    @Test
    void QueryString의_key_value를_지정할_수_있다() {
        QueryString qs = new QueryString();
        assertThatCode(() -> qs.put("a", "b")).doesNotThrowAnyException();
    }

    @Test
    void QueryString의_key를_통해_얻어낼_수_있다() {
        QueryString qs = new QueryString();
        qs.put("a", "b");
        assertThat(qs.get("a").orElse("")).isEqualTo("b");
    }
}