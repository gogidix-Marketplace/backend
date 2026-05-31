package com.gogidix.finance.tax.application.dto.response;

import com.gogidix.finance.tax.application.dto.response.TaxFilingResponseDto;
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
class TaxFilingResponseDto_AdjustmentDtoTest {

        @Test
    void testBuilder() {
        TaxFilingResponseDto.AdjustmentDto dto = TaxFilingResponseDto.AdjustmentDto.builder()
                        .adjustmentId("test-adjustmentId")
            .adjustmentType("test-adjustmentType")
            .amount(BigDecimal.TEN)
            .reason("test-reason")
            .reference("test-reference")
            .adjustmentDate(LocalDate.of(2025,1,15))
            .build();
        assertNotNull(dto);
        assertEquals("test-adjustmentId", dto.getAdjustmentId());
        assertEquals("test-adjustmentType", dto.getAdjustmentType());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-reason", dto.getReason());
        assertEquals("test-reference", dto.getReference());
        assertEquals(LocalDate.of(2025,1,15), dto.getAdjustmentDate());
    }

    @Test
    void testSettersAndGetters() {
        TaxFilingResponseDto.AdjustmentDto dto = new TaxFilingResponseDto.AdjustmentDto();
        dto.setAdjustmentId("val-adjustmentId");
        dto.setAdjustmentType("val-adjustmentType");
        dto.setAmount(BigDecimal.ONE);
        dto.setReason("val-reason");
        dto.setReference("val-reference");
        dto.setAdjustmentDate(LocalDate.of(2025,6,1));
        assertEquals("val-adjustmentId", dto.getAdjustmentId());
        assertEquals("val-adjustmentType", dto.getAdjustmentType());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-reason", dto.getReason());
        assertEquals("val-reference", dto.getReference());
        assertEquals(LocalDate.of(2025,6,1), dto.getAdjustmentDate());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxFilingResponseDto.AdjustmentDto dto1 = TaxFilingResponseDto.AdjustmentDto.builder()
                        .adjustmentId("test-adjustmentId")
            .adjustmentType("test-adjustmentType")
            .amount(BigDecimal.TEN)
            .reason("test-reason")
            .reference("test-reference")
            .adjustmentDate(LocalDate.of(2025,1,15))
            .build();
        TaxFilingResponseDto.AdjustmentDto dto2 = TaxFilingResponseDto.AdjustmentDto.builder()
                        .adjustmentId("test-adjustmentId")
            .adjustmentType("test-adjustmentType")
            .amount(BigDecimal.TEN)
            .reason("test-reason")
            .reference("test-reference")
            .adjustmentDate(LocalDate.of(2025,1,15))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TaxFilingResponseDto.AdjustmentDto dto = TaxFilingResponseDto.AdjustmentDto.builder()
                        .adjustmentId("test-adjustmentId")
            .adjustmentType("test-adjustmentType")
            .amount(BigDecimal.TEN)
            .reason("test-reason")
            .reference("test-reference")
            .adjustmentDate(LocalDate.of(2025,1,15))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}