package com.gogidix.finance.tax.domain.model;

import com.gogidix.finance.tax.domain.model.TaxFiling;
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
class TaxFiling_AdjustmentTest {

        @Test
    void testBuilder() {
        TaxFiling.Adjustment dto = TaxFiling.Adjustment.builder()
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
        TaxFiling.Adjustment dto = new TaxFiling.Adjustment();
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
        TaxFiling.Adjustment dto1 = TaxFiling.Adjustment.builder()
                        .adjustmentId("test-adjustmentId")
            .adjustmentType("test-adjustmentType")
            .amount(BigDecimal.TEN)
            .reason("test-reason")
            .reference("test-reference")
            .adjustmentDate(LocalDate.of(2025,1,15))
            .build();
        TaxFiling.Adjustment dto2 = TaxFiling.Adjustment.builder()
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
        TaxFiling.Adjustment dto = TaxFiling.Adjustment.builder()
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