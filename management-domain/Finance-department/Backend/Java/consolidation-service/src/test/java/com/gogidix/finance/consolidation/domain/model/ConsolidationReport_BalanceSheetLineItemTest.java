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
class ConsolidationReport_BalanceSheetLineItemTest {

        @Test
    void testBuilder() {
        ConsolidationReport.BalanceSheetLineItem dto = ConsolidationReport.BalanceSheetLineItem.builder()
                        .lineCode("test-lineCode")
            .lineName("test-lineName")
            .amount(BigDecimal.TEN)
            .priorPeriodAmount(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .accountType("test-accountType")
            .hierarchyLevel(42)
            .isHeader(true)
            .isTotal(true)
            .subItems(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-lineCode", dto.getLineCode());
        assertEquals("test-lineName", dto.getLineName());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals(BigDecimal.TEN, dto.getPriorPeriodAmount());
        assertEquals(BigDecimal.TEN, dto.getVariance());
        assertEquals("test-accountType", dto.getAccountType());
        assertEquals(42, dto.getHierarchyLevel());
        assertTrue(dto.getIsHeader());
        assertTrue(dto.getIsTotal());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidationReport.BalanceSheetLineItem dto = new ConsolidationReport.BalanceSheetLineItem();
        dto.setLineCode("val-lineCode");
        dto.setLineName("val-lineName");
        dto.setAmount(BigDecimal.ONE);
        dto.setPriorPeriodAmount(BigDecimal.ONE);
        dto.setVariance(BigDecimal.ONE);
        dto.setAccountType("val-accountType");
        dto.setHierarchyLevel(99);
        dto.setIsHeader(true);
        dto.setIsTotal(true);
        assertEquals("val-lineCode", dto.getLineCode());
        assertEquals("val-lineName", dto.getLineName());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(BigDecimal.ONE, dto.getPriorPeriodAmount());
        assertEquals(BigDecimal.ONE, dto.getVariance());
        assertEquals("val-accountType", dto.getAccountType());
        assertEquals(99, dto.getHierarchyLevel());
        assertTrue(dto.getIsHeader());
        assertTrue(dto.getIsTotal());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationReport.BalanceSheetLineItem dto1 = ConsolidationReport.BalanceSheetLineItem.builder()
                        .lineCode("test-lineCode")
            .lineName("test-lineName")
            .amount(BigDecimal.TEN)
            .priorPeriodAmount(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .accountType("test-accountType")
            .hierarchyLevel(42)
            .isHeader(true)
            .isTotal(true)
            .subItems(Collections.emptyList())
            .build();
        ConsolidationReport.BalanceSheetLineItem dto2 = ConsolidationReport.BalanceSheetLineItem.builder()
                        .lineCode("test-lineCode")
            .lineName("test-lineName")
            .amount(BigDecimal.TEN)
            .priorPeriodAmount(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .accountType("test-accountType")
            .hierarchyLevel(42)
            .isHeader(true)
            .isTotal(true)
            .subItems(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidationReport.BalanceSheetLineItem dto = ConsolidationReport.BalanceSheetLineItem.builder()
                        .lineCode("test-lineCode")
            .lineName("test-lineName")
            .amount(BigDecimal.TEN)
            .priorPeriodAmount(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .accountType("test-accountType")
            .hierarchyLevel(42)
            .isHeader(true)
            .isTotal(true)
            .subItems(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}