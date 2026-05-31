package com.gogidix.aiservices.aisalesforecastingservice.shared.exception;

import org.junit.jupiter.api.*;
import java.time.Instant;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void shouldBuildWithAllFields() {
        var err = ErrorResponse.builder()
            .timestamp(Instant.now())
            .status(400)
            .error("Bad Request")
            .message("Invalid input")
            .path("/api/v1/forecasts")
            .correlationId("corr1")
            .tenantId("t1")
            .details(List.of("field1 is required"))
            .build();
        assertThat(err.getStatus()).isEqualTo(400);
        assertThat(err.getError()).isEqualTo("Bad Request");
        assertThat(err.getMessage()).isEqualTo("Invalid input");
        assertThat(err.getPath()).isEqualTo("/api/v1/forecasts");
        assertThat(err.getCorrelationId()).isEqualTo("corr1");
        assertThat(err.getTenantId()).isEqualTo("t1");
        assertThat(err.getDetails()).hasSize(1);
        assertThat(err.getTimestamp()).isNotNull();
    }

    @Test
    void shouldUseDefaultsWhenNotSet() {
        var err = ErrorResponse.builder()
            .status(500)
            .error("Error")
            .message("msg")
            .path("/test")
            .build();
        assertThat(err.getTimestamp()).isNotNull();
        assertThat(err.getDetails()).isNull();
    }

    @Test
    void shouldBuildFromException() {
        var ex = new ValidationException("validation failed");
        var err = ErrorResponse.fromException(ex, "/test", 400).correlationId("c").tenantId("t").build();
        assertThat(err.getStatus()).isEqualTo(400);
        assertThat(err.getPath()).isEqualTo("/test");
        assertThat(err.getMessage()).contains("validation failed");
    }

    @Test
    void shouldBuildFromBaseDomainException() {
        var ex = new BusinessException("biz error", "BIZ_CODE");
        var err = ErrorResponse.fromException(ex, "/test", 409).correlationId("c").tenantId("t").build();
        assertThat(err.getStatus()).isEqualTo(409);
        assertThat(err.getError()).isEqualTo("BIZ_CODE");
    }

    @Test
    void shouldBuildFromNullException() {
        Exception ex = null;
        assertThatThrownBy(() -> ErrorResponse.fromException(null, "/test", 500))
            .isInstanceOf(NullPointerException.class);
    }
}
