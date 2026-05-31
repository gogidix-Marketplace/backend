package com.gogidix.hr.payroll.interfaces.rest;

import com.gogidix.hr.payroll.interfaces.rest.GlobalExceptionHandler;
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
class GlobalExceptionHandler_ErrorResponseTest {

        @Test
    void testBuilder() {
        GlobalExceptionHandler.ErrorResponse dto = GlobalExceptionHandler.ErrorResponse.builder()
                        .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .status(42)
            .error("test-error")
            .message("test-message")
            .path("test-path")
            .validationErrors(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getStatus());
        assertEquals("test-error", dto.getError());
        assertEquals("test-message", dto.getMessage());
        assertEquals("test-path", dto.getPath());
    }

    @Test
    void testBuilderWithValues() {
        GlobalExceptionHandler.ErrorResponse dto = GlobalExceptionHandler.ErrorResponse.builder()
            .status(99)
            .error("val-error")
            .message("val-message")
            .path("val-path")
            .build();
        assertNotNull(dto);
    }

    @Test
    void testEqualsAndHashCode() {
        GlobalExceptionHandler.ErrorResponse dto1 = GlobalExceptionHandler.ErrorResponse.builder()
                        .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .status(42)
            .error("test-error")
            .message("test-message")
            .path("test-path")
            .validationErrors(Collections.emptyMap())
            .build();
        GlobalExceptionHandler.ErrorResponse dto2 = GlobalExceptionHandler.ErrorResponse.builder()
                        .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .status(42)
            .error("test-error")
            .message("test-message")
            .path("test-path")
            .validationErrors(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GlobalExceptionHandler.ErrorResponse dto = GlobalExceptionHandler.ErrorResponse.builder()
                        .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .status(42)
            .error("test-error")
            .message("test-message")
            .path("test-path")
            .validationErrors(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}