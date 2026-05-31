package com.gogidix.sales.crm.application.dto.response;

import com.gogidix.sales.crm.application.dto.response.ErrorResponseDto;
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
class ErrorResponseDto_ValidationErrorTest {

        @Test
    void testBuilder() {
        ErrorResponseDto.ValidationError dto = ErrorResponseDto.ValidationError.builder()
                        .field("test-field")
            .message("test-message")
            .build();
        assertNotNull(dto);
        assertEquals("test-field", dto.getField());
        assertEquals("test-message", dto.getMessage());
    }

    @Test
    void testSettersAndGetters() {
        ErrorResponseDto.ValidationError dto = new ErrorResponseDto.ValidationError();
        dto.setField("val-field");
        dto.setMessage("val-message");
        assertEquals("val-field", dto.getField());
        assertEquals("val-message", dto.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        ErrorResponseDto.ValidationError dto1 = ErrorResponseDto.ValidationError.builder()
                        .field("test-field")
            .message("test-message")
            .build();
        ErrorResponseDto.ValidationError dto2 = ErrorResponseDto.ValidationError.builder()
                        .field("test-field")
            .message("test-message")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ErrorResponseDto.ValidationError dto = ErrorResponseDto.ValidationError.builder()
                        .field("test-field")
            .message("test-message")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}