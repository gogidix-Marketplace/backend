package com.gogidix.sales.territory.application.dto.response;

import com.gogidix.sales.territory.application.dto.response.ErrorResponseDto;
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
class ErrorResponseDto_FieldErrorTest {

        @Test
    void testBuilder() {
        ErrorResponseDto.FieldError dto = ErrorResponseDto.FieldError.builder()
                        .field("test-field")
            .message("test-message")
            .rejectedValue(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-field", dto.getField());
        assertEquals("test-message", dto.getMessage());
    }

    @Test
    void testSettersAndGetters() {
        ErrorResponseDto.FieldError dto = new ErrorResponseDto.FieldError();
        dto.setField("val-field");
        dto.setMessage("val-message");
        assertEquals("val-field", dto.getField());
        assertEquals("val-message", dto.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        ErrorResponseDto.FieldError dto1 = ErrorResponseDto.FieldError.builder()
                        .field("test-field")
            .message("test-message")
            .rejectedValue(null)
            .build();
        ErrorResponseDto.FieldError dto2 = ErrorResponseDto.FieldError.builder()
                        .field("test-field")
            .message("test-message")
            .rejectedValue(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ErrorResponseDto.FieldError dto = ErrorResponseDto.FieldError.builder()
                        .field("test-field")
            .message("test-message")
            .rejectedValue(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}