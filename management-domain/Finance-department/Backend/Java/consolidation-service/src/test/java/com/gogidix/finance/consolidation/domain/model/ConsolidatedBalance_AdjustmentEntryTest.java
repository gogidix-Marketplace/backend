package com.gogidix.finance.consolidation.domain.model;

import com.gogidix.finance.consolidation.domain.model.ConsolidatedBalance;
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
class ConsolidatedBalance_AdjustmentEntryTest {

        @Test
    void testBuilder() {
        ConsolidatedBalance.AdjustmentEntry dto = ConsolidatedBalance.AdjustmentEntry.builder()
                        .adjustmentId("test-adjustmentId")
            .adjustmentType("test-adjustmentType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .reason("test-reason")
            .adjustedBy("test-adjustedBy")
            .adjustedOn(LocalDate.of(2025,1,15))
            .referenceDocument("test-referenceDocument")
            .build();
        assertNotNull(dto);
        assertEquals("test-adjustmentId", dto.getAdjustmentId());
        assertEquals("test-adjustmentType", dto.getAdjustmentType());
        assertEquals("test-description", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-reason", dto.getReason());
        assertEquals("test-adjustedBy", dto.getAdjustedBy());
        assertEquals(LocalDate.of(2025,1,15), dto.getAdjustedOn());
        assertEquals("test-referenceDocument", dto.getReferenceDocument());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidatedBalance.AdjustmentEntry dto = new ConsolidatedBalance.AdjustmentEntry();
        dto.setAdjustmentId("val-adjustmentId");
        dto.setAdjustmentType("val-adjustmentType");
        dto.setDescription("val-description");
        dto.setAmount(BigDecimal.ONE);
        dto.setReason("val-reason");
        dto.setAdjustedBy("val-adjustedBy");
        dto.setAdjustedOn(LocalDate.of(2025,6,1));
        dto.setReferenceDocument("val-referenceDocument");
        assertEquals("val-adjustmentId", dto.getAdjustmentId());
        assertEquals("val-adjustmentType", dto.getAdjustmentType());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-reason", dto.getReason());
        assertEquals("val-adjustedBy", dto.getAdjustedBy());
        assertEquals(LocalDate.of(2025,6,1), dto.getAdjustedOn());
        assertEquals("val-referenceDocument", dto.getReferenceDocument());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidatedBalance.AdjustmentEntry dto1 = ConsolidatedBalance.AdjustmentEntry.builder()
                        .adjustmentId("test-adjustmentId")
            .adjustmentType("test-adjustmentType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .reason("test-reason")
            .adjustedBy("test-adjustedBy")
            .adjustedOn(LocalDate.of(2025,1,15))
            .referenceDocument("test-referenceDocument")
            .build();
        ConsolidatedBalance.AdjustmentEntry dto2 = ConsolidatedBalance.AdjustmentEntry.builder()
                        .adjustmentId("test-adjustmentId")
            .adjustmentType("test-adjustmentType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .reason("test-reason")
            .adjustedBy("test-adjustedBy")
            .adjustedOn(LocalDate.of(2025,1,15))
            .referenceDocument("test-referenceDocument")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidatedBalance.AdjustmentEntry dto = ConsolidatedBalance.AdjustmentEntry.builder()
                        .adjustmentId("test-adjustmentId")
            .adjustmentType("test-adjustmentType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .reason("test-reason")
            .adjustedBy("test-adjustedBy")
            .adjustedOn(LocalDate.of(2025,1,15))
            .referenceDocument("test-referenceDocument")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}