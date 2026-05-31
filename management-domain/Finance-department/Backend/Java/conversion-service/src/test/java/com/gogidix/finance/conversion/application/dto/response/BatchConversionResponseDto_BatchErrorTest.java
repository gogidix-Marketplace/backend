package com.gogidix.finance.conversion.application.dto.response;

import com.gogidix.finance.conversion.application.dto.response.BatchConversionResponseDto;
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
class BatchConversionResponseDto_BatchErrorTest {

        @Test
    void testBuilder() {
        BatchConversionResponseDto.BatchError dto = BatchConversionResponseDto.BatchError.builder()
                        .reference("test-reference")
            .amount(BigDecimal.TEN)
            .fromCurrency("test-fromCurrency")
            .toCurrency("test-toCurrency")
            .errorMessage("test-errorMessage")
            .build();
        assertNotNull(dto);
        assertEquals("test-reference", dto.getReference());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-fromCurrency", dto.getFromCurrency());
        assertEquals("test-toCurrency", dto.getToCurrency());
        assertEquals("test-errorMessage", dto.getErrorMessage());
    }

    @Test
    void testSettersAndGetters() {
        BatchConversionResponseDto.BatchError dto = new BatchConversionResponseDto.BatchError();
        dto.setReference("val-reference");
        dto.setAmount(BigDecimal.ONE);
        dto.setFromCurrency("val-fromCurrency");
        dto.setToCurrency("val-toCurrency");
        dto.setErrorMessage("val-errorMessage");
        assertEquals("val-reference", dto.getReference());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-fromCurrency", dto.getFromCurrency());
        assertEquals("val-toCurrency", dto.getToCurrency());
        assertEquals("val-errorMessage", dto.getErrorMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        BatchConversionResponseDto.BatchError dto1 = BatchConversionResponseDto.BatchError.builder()
                        .reference("test-reference")
            .amount(BigDecimal.TEN)
            .fromCurrency("test-fromCurrency")
            .toCurrency("test-toCurrency")
            .errorMessage("test-errorMessage")
            .build();
        BatchConversionResponseDto.BatchError dto2 = BatchConversionResponseDto.BatchError.builder()
                        .reference("test-reference")
            .amount(BigDecimal.TEN)
            .fromCurrency("test-fromCurrency")
            .toCurrency("test-toCurrency")
            .errorMessage("test-errorMessage")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BatchConversionResponseDto.BatchError dto = BatchConversionResponseDto.BatchError.builder()
                        .reference("test-reference")
            .amount(BigDecimal.TEN)
            .fromCurrency("test-fromCurrency")
            .toCurrency("test-toCurrency")
            .errorMessage("test-errorMessage")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}