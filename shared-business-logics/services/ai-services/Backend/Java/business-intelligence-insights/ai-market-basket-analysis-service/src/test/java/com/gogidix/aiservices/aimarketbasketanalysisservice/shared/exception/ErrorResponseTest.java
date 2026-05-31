package com.gogidix.aiservices.aimarketbasketanalysisservice.shared.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ErrorResponse Tests")
class ErrorResponseTest {

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            Instant ts = Instant.now();
            ErrorResponse err = ErrorResponse.builder()
                    .timestamp(ts)
                    .status(400)
                    .error("Bad Request")
                    .message("Invalid input")
                    .path("/api/test")
                    .correlationId("corr-123")
                    .tenantId("tenant-1")
                    .details(List.of("field is required"))
                    .build();

            assertThat(err.getTimestamp()).isEqualTo(ts);
            assertThat(err.getStatus()).isEqualTo(400);
            assertThat(err.getError()).isEqualTo("Bad Request");
            assertThat(err.getMessage()).isEqualTo("Invalid input");
            assertThat(err.getPath()).isEqualTo("/api/test");
            assertThat(err.getCorrelationId()).isEqualTo("corr-123");
            assertThat(err.getTenantId()).isEqualTo("tenant-1");
            assertThat(err.getDetails()).containsExactly("field is required");
        }

        @Test
        @DisplayName("Should default timestamp to now")
        void shouldDefaultTimestamp() {
            ErrorResponse err = ErrorResponse.builder()
                    .status(500)
                    .error("Error")
                    .message("msg")
                    .path("/path")
                    .build();

            assertThat(err.getTimestamp()).isNotNull();
        }

        @Test
        @DisplayName("Should have null details when not set")
        void shouldHaveNullDetails() {
            ErrorResponse err = ErrorResponse.builder()
                    .status(500)
                    .error("Error")
                    .message("msg")
                    .path("/path")
                    .correlationId("c")
                    .tenantId("t")
                    .build();

            assertThat(err.getDetails()).isNull();
        }
    }

    @Nested
    @DisplayName("fromException Tests")
    class FromExceptionTests {

        @Test
        @DisplayName("Should create from generic exception")
        void shouldCreateFromGenericException() {
            Exception ex = new RuntimeException("Something went wrong");
            ErrorResponse.Builder builder = ErrorResponse.fromException(ex, "/api/test", 500);

            ErrorResponse err = builder.build();
            assertThat(err.getStatus()).isEqualTo(500);
            assertThat(err.getError()).isEqualTo("INTERNAL_SERVER_ERROR");
            assertThat(err.getMessage()).isEqualTo("Something went wrong");
            assertThat(err.getPath()).isEqualTo("/api/test");
        }

        @Test
        @DisplayName("Should create from domain exception with error code")
        void shouldCreateFromDomainException() {
            Exception ex = new ValidationException("Invalid field");
            ErrorResponse.Builder builder = ErrorResponse.fromException(ex, "/api/test", 400);

            ErrorResponse err = builder.build();
            assertThat(err.getStatus()).isEqualTo(400);
            assertThat(err.getMessage()).isEqualTo("Invalid field");
        }

        @Test
        @DisplayName("Should handle null exception message")
        void shouldHandleNullExceptionMessage() {
            Exception ex = new RuntimeException();
            ErrorResponse.Builder builder = ErrorResponse.fromException(ex, "/api/test", 500);

            ErrorResponse err = builder.build();
            assertThat(err.getMessage()).isEqualTo("An unexpected error occurred");
        }
    }
}
