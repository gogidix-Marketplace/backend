package com.gogidix.finance.consolidation.domain.model;

import com.gogidix.finance.consolidation.domain.model.ConsolidationReport;
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
class ConsolidationReport_AdjustmentReportEntryTest {

        @Test
    void testBuilder() {
        ConsolidationReport.AdjustmentReportEntry dto = ConsolidationReport.AdjustmentReportEntry.builder()
                        .adjustmentId("test-adjustmentId")
            .accountCode("test-accountCode")
            .accountName("test-accountName")
            .adjustmentType("test-adjustmentType")
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .description("test-description")
            .reason("test-reason")
            .adjustedBy("test-adjustedBy")
            .adjustedOn(LocalDate.of(2025,1,15))
            .referenceDocument("test-referenceDocument")
            .build();
        assertNotNull(dto);
        assertEquals("test-adjustmentId", dto.getAdjustmentId());
        assertEquals("test-accountCode", dto.getAccountCode());
        assertEquals("test-accountName", dto.getAccountName());
        assertEquals("test-adjustmentType", dto.getAdjustmentType());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-reason", dto.getReason());
        assertEquals("test-adjustedBy", dto.getAdjustedBy());
        assertEquals(LocalDate.of(2025,1,15), dto.getAdjustedOn());
        assertEquals("test-referenceDocument", dto.getReferenceDocument());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidationReport.AdjustmentReportEntry dto = new ConsolidationReport.AdjustmentReportEntry();
        dto.setAdjustmentId("val-adjustmentId");
        dto.setAccountCode("val-accountCode");
        dto.setAccountName("val-accountName");
        dto.setAdjustmentType("val-adjustmentType");
        dto.setAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setDescription("val-description");
        dto.setReason("val-reason");
        dto.setAdjustedBy("val-adjustedBy");
        dto.setAdjustedOn(LocalDate.of(2025,6,1));
        dto.setReferenceDocument("val-referenceDocument");
        assertEquals("val-adjustmentId", dto.getAdjustmentId());
        assertEquals("val-accountCode", dto.getAccountCode());
        assertEquals("val-accountName", dto.getAccountName());
        assertEquals("val-adjustmentType", dto.getAdjustmentType());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-reason", dto.getReason());
        assertEquals("val-adjustedBy", dto.getAdjustedBy());
        assertEquals(LocalDate.of(2025,6,1), dto.getAdjustedOn());
        assertEquals("val-referenceDocument", dto.getReferenceDocument());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationReport.AdjustmentReportEntry dto1 = ConsolidationReport.AdjustmentReportEntry.builder()
                        .adjustmentId("test-adjustmentId")
            .accountCode("test-accountCode")
            .accountName("test-accountName")
            .adjustmentType("test-adjustmentType")
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .description("test-description")
            .reason("test-reason")
            .adjustedBy("test-adjustedBy")
            .adjustedOn(LocalDate.of(2025,1,15))
            .referenceDocument("test-referenceDocument")
            .build();
        ConsolidationReport.AdjustmentReportEntry dto2 = ConsolidationReport.AdjustmentReportEntry.builder()
                        .adjustmentId("test-adjustmentId")
            .accountCode("test-accountCode")
            .accountName("test-accountName")
            .adjustmentType("test-adjustmentType")
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .description("test-description")
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
        ConsolidationReport.AdjustmentReportEntry dto = ConsolidationReport.AdjustmentReportEntry.builder()
                        .adjustmentId("test-adjustmentId")
            .accountCode("test-accountCode")
            .accountName("test-accountName")
            .adjustmentType("test-adjustmentType")
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .description("test-description")
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