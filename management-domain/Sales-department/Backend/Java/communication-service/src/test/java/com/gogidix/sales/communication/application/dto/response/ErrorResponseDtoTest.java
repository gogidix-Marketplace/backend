package com.gogidix.sales.communication.application.dto.response;

import com.gogidix.sales.communication.application.dto.response.ErrorResponseDto;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ErrorResponseDtoTest {

        @Test
    void testBuilder() {
        ErrorResponseDto dto = ErrorResponseDto.builder()
                        .status(42)
            .error("test-error")
            .message("test-message")
            .path("test-path")
            .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .errors(Collections.emptyList())
            .requestId("test-requestId")
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getStatus());
        assertEquals("test-error", dto.getError());
        assertEquals("test-message", dto.getMessage());
        assertEquals("test-path", dto.getPath());
        assertEquals("test-requestId", dto.getRequestId());
    }

    @Test
    void testSettersAndGetters() {
        ErrorResponseDto dto = new ErrorResponseDto();
        dto.setStatus(99);
        dto.setError("val-error");
        dto.setMessage("val-message");
        dto.setPath("val-path");
        dto.setRequestId("val-requestId");
        assertEquals(99, dto.getStatus());
        assertEquals("val-error", dto.getError());
        assertEquals("val-message", dto.getMessage());
        assertEquals("val-path", dto.getPath());
        assertEquals("val-requestId", dto.getRequestId());
    }

    @Test
    void testEqualsAndHashCode() {
        ErrorResponseDto dto1 = ErrorResponseDto.builder()
                        .status(42)
            .error("test-error")
            .message("test-message")
            .path("test-path")
            .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .errors(Collections.emptyList())
            .requestId("test-requestId")
            .build();
        ErrorResponseDto dto2 = ErrorResponseDto.builder()
                        .status(42)
            .error("test-error")
            .message("test-message")
            .path("test-path")
            .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .errors(Collections.emptyList())
            .requestId("test-requestId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ErrorResponseDto dto = ErrorResponseDto.builder()
                        .status(42)
            .error("test-error")
            .message("test-message")
            .path("test-path")
            .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .errors(Collections.emptyList())
            .requestId("test-requestId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}