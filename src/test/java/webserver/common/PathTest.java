package webserver.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Path 관련 테스트")
class PathTest {
    @Test
    void Path를_생성할_수_있다() {
        Path path = Path.of("new path");
        assertThat(path).isNotNull();
    }
}