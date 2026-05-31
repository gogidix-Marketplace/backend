package com.gogidix.shared.infrastructure.services.security.tenantmanagement.shared.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ErrorResponse Tests")
class ErrorResponseTest {

    @Test
    void shouldBuildWithError() {
        ErrorResponse err = ErrorResponse.builder()
            .status(404).error("NOT_FOUND").message("Not found").path("/api/test").tenantId("t1")
            .build();
        assertEquals(404, err.getStatus());
        assertEquals("NOT_FOUND", err.getError());
        assertEquals("Not found", err.getMessage());
    }

    @Test
    void shouldGenerateTimestampWhenNull() {
        ErrorResponse err = ErrorResponse.builder().build();
        assertNotNull(err.getTimestamp());
    }

    @Test
    void shouldUseProvidedTimestamp() {
        ErrorResponse err = ErrorResponse.builder().timestamp("2025-01-01T00:00:00").build();
        assertEquals("2025-01-01T00:00:00", err.getTimestamp());
    }

    @Test
    void shouldBeEqualWithSameFields() {
        ErrorResponse e1 = ErrorResponse.builder().timestamp("2025-01-01T00:00:00").status(404).error("NOT_FOUND").build();
        ErrorResponse e2 = ErrorResponse.builder().timestamp("2025-01-01T00:00:00").status(404).error("NOT_FOUND").build();
        assertEquals(e1, e2);
    }
}
