package com.gogidix.aiservices.aiproductrecommendationservice.shared.exception;

import com.gogidix.aiservices.aiproductrecommendationservice.shared.context.RequestContext;
import com.gogidix.aiservices.aiproductrecommendationservice.shared.context.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ErrorResponse Tests")
class ErrorResponseTest {

    @AfterEach
    void cleanup() {
        RequestContextHolder.clear();
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {
        @Test
        void shouldBuildWithAllFields() {
            RequestContextHolder.setContext(RequestContext.create("tenant-1", "user-1"));
            ErrorResponse err = ErrorResponse.builder()
                    .timestamp(Instant.now())
                    .status(400)
                    .error("Bad Request")
                    .message("Invalid")
                    .path("/api/test")
                    .correlationId("corr-1")
                    .tenantId("t-1")
                    .details(List.of("field required"))
                    .build();
            assertThat(err.getStatus()).isEqualTo(400);
            assertThat(err.getDetails()).containsExactly("field required");
        }

        @Test
        void shouldUseContextWhenNotProvided() {
            RequestContextHolder.setContext(RequestContext.createWithCorrelationId("ctx-tenant", "ctx-user", "ctx-corr"));
            ErrorResponse err = ErrorResponse.builder()
                    .status(500).error("E").message("m").path("/p").build();
            assertThat(err.getTenantId()).isEqualTo("ctx-tenant");
            assertThat(err.getCorrelationId()).isEqualTo("ctx-corr");
        }
    }

    @Nested
    @DisplayName("fromException Tests")
    class FromExceptionTests {
        @Test
        void shouldCreateFromGenericException() {
            RequestContextHolder.setContext(RequestContext.create("t", "u"));
            ErrorResponse err = ErrorResponse.fromException(new RuntimeException("boom"), "/api", 500).build();
            assertThat(err.getError()).isEqualTo("INTERNAL_SERVER_ERROR");
            assertThat(err.getMessage()).isEqualTo("boom");
        }

        @Test
        void shouldHandleNullExceptionMessage() {
            RequestContextHolder.setContext(RequestContext.create("t", "u"));
            ErrorResponse err = ErrorResponse.fromException(new RuntimeException(), "/api", 500).build();
            assertThat(err.getMessage()).isEqualTo("An unexpected error occurred");
        }
    }
}
