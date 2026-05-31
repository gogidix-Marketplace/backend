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
class BatchConversionResponseDtoTest {

        @Test
    void testBuilder() {
        BatchConversionResponseDto dto = BatchConversionResponseDto.builder()
                        .batchId("test-batchId")
            .correlationId("test-correlationId")
            .totalCount(42)
            .successCount(42)
            .failureCount(42)
            .processedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .conversions(Collections.emptyList())
            .errors(Collections.emptyList())
            .totalOriginalAmount(BigDecimal.TEN)
            .totalConvertedAmount(BigDecimal.TEN)
            .status("test-status")
            .build();
        assertNotNull(dto);
        assertEquals("test-batchId", dto.getBatchId());
        assertEquals("test-correlationId", dto.getCorrelationId());
        assertEquals(42, dto.getTotalCount());
        assertEquals(42, dto.getSuccessCount());
        assertEquals(42, dto.getFailureCount());
        assertEquals(BigDecimal.TEN, dto.getTotalOriginalAmount());
        assertEquals(BigDecimal.TEN, dto.getTotalConvertedAmount());
        assertEquals("test-status", dto.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        BatchConversionResponseDto dto = new BatchConversionResponseDto();
        dto.setBatchId("val-batchId");
        dto.setCorrelationId("val-correlationId");
        dto.setTotalCount(99);
        dto.setSuccessCount(99);
        dto.setFailureCount(99);
        dto.setTotalOriginalAmount(BigDecimal.ONE);
        dto.setTotalConvertedAmount(BigDecimal.ONE);
        dto.setStatus("val-status");
        assertEquals("val-batchId", dto.getBatchId());
        assertEquals("val-correlationId", dto.getCorrelationId());
        assertEquals(99, dto.getTotalCount());
        assertEquals(99, dto.getSuccessCount());
        assertEquals(99, dto.getFailureCount());
        assertEquals(BigDecimal.ONE, dto.getTotalOriginalAmount());
        assertEquals(BigDecimal.ONE, dto.getTotalConvertedAmount());
        assertEquals("val-status", dto.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        BatchConversionResponseDto dto1 = BatchConversionResponseDto.builder()
                        .batchId("test-batchId")
            .correlationId("test-correlationId")
            .totalCount(42)
            .successCount(42)
            .failureCount(42)
            .processedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .conversions(Collections.emptyList())
            .errors(Collections.emptyList())
            .totalOriginalAmount(BigDecimal.TEN)
            .totalConvertedAmount(BigDecimal.TEN)
            .status("test-status")
            .build();
        BatchConversionResponseDto dto2 = BatchConversionResponseDto.builder()
                        .batchId("test-batchId")
            .correlationId("test-correlationId")
            .totalCount(42)
            .successCount(42)
            .failureCount(42)
            .processedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .conversions(Collections.emptyList())
            .errors(Collections.emptyList())
            .totalOriginalAmount(BigDecimal.TEN)
            .totalConvertedAmount(BigDecimal.TEN)
            .status("test-status")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BatchConversionResponseDto dto = BatchConversionResponseDto.builder()
                        .batchId("test-batchId")
            .correlationId("test-correlationId")
            .totalCount(42)
            .successCount(42)
            .failureCount(42)
            .processedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .conversions(Collections.emptyList())
            .errors(Collections.emptyList())
            .totalOriginalAmount(BigDecimal.TEN)
            .totalConvertedAmount(BigDecimal.TEN)
            .status("test-status")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}