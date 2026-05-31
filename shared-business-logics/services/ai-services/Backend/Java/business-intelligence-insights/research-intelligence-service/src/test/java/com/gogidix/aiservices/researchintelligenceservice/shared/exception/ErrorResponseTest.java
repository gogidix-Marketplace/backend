package com.gogidix.aiservices.researchintelligenceservice.shared.exception;
import org.junit.jupiter.api.*; import java.util.Date; import static org.assertj.core.api.Assertions.*;
class ErrorResponseTest {
    @Test void create() {
        var d = new Date();
        var e = new shared.exception.ErrorResponse.ErrorResponse(400, d, "msg", "details");
        assertThat(e.getStatus()).isEqualTo(400);
        assertThat(e.getTimestamp()).isEqualTo(d);
        assertThat(e.getMessage()).isEqualTo("msg");
        assertThat(e.getDetails()).isEqualTo("details");
    }
    @Test void defaultConstructor() {
        var e = new shared.exception.ErrorResponse.ErrorResponse();
        assertThat(e.getStatus()).isEqualTo(0);
        assertThat(e.getMessage()).isNull();
    }
}
