package com.gogidix.aiservices.aiuserprofilingservice.shared.exception;

import org.junit.jupiter.api.*;
import java.time.Instant;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class ErrorResponseTest {
    @Test
    void shouldBuildWithAllFields() {
        var err = ErrorResponse.builder()
            .timestamp(Instant.now()).status(400).error("Bad Request")
            .message("Invalid").path("/api").correlationId("c1").tenantId("t1")
            .details(List.of("field required")).build();
        assertThat(err.getStatus()).isEqualTo(400);
        assertThat(err.getDetails()).hasSize(1);
    }
    @Test
    void shouldUseDefaults() {
        var err = ErrorResponse.builder().status(500).error("E").message("m").path("/").build();
        assertThat(err.getTimestamp()).isNotNull();
        assertThat(err.getDetails()).isNull();
    }
    @Test
    void shouldBuildFromException() {
        var ex = new ValidationException("val failed");
        var err = ErrorResponse.fromException(ex, "/test", 400).correlationId("c").tenantId("t").build();
        assertThat(err.getStatus()).isEqualTo(400);
    }
}
