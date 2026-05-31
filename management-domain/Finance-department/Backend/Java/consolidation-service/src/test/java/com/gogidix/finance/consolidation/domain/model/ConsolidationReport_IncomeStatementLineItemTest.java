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
class ConsolidationReport_IncomeStatementLineItemTest {

        @Test
    void testBuilder() {
        ConsolidationReport.IncomeStatementLineItem dto = ConsolidationReport.IncomeStatementLineItem.builder()
                        .lineCode("test-lineCode")
            .lineName("test-lineName")
            .amount(BigDecimal.TEN)
            .priorPeriodAmount(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .percentageOfRevenue(BigDecimal.TEN)
            .hierarchyLevel(42)
            .isHeader(true)
            .isTotal(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-lineCode", dto.getLineCode());
        assertEquals("test-lineName", dto.getLineName());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals(BigDecimal.TEN, dto.getPriorPeriodAmount());
        assertEquals(BigDecimal.TEN, dto.getVariance());
        assertEquals(BigDecimal.TEN, dto.getPercentageOfRevenue());
        assertEquals(42, dto.getHierarchyLevel());
        assertTrue(dto.getIsHeader());
        assertTrue(dto.getIsTotal());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidationReport.IncomeStatementLineItem dto = new ConsolidationReport.IncomeStatementLineItem();
        dto.setLineCode("val-lineCode");
        dto.setLineName("val-lineName");
        dto.setAmount(BigDecimal.ONE);
        dto.setPriorPeriodAmount(BigDecimal.ONE);
        dto.setVariance(BigDecimal.ONE);
        dto.setPercentageOfRevenue(BigDecimal.ONE);
        dto.setHierarchyLevel(99);
        dto.setIsHeader(true);
        dto.setIsTotal(true);
        assertEquals("val-lineCode", dto.getLineCode());
        assertEquals("val-lineName", dto.getLineName());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(BigDecimal.ONE, dto.getPriorPeriodAmount());
        assertEquals(BigDecimal.ONE, dto.getVariance());
        assertEquals(BigDecimal.ONE, dto.getPercentageOfRevenue());
        assertEquals(99, dto.getHierarchyLevel());
        assertTrue(dto.getIsHeader());
        assertTrue(dto.getIsTotal());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationReport.IncomeStatementLineItem dto1 = ConsolidationReport.IncomeStatementLineItem.builder()
                        .lineCode("test-lineCode")
            .lineName("test-lineName")
            .amount(BigDecimal.TEN)
            .priorPeriodAmount(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .percentageOfRevenue(BigDecimal.TEN)
            .hierarchyLevel(42)
            .isHeader(true)
            .isTotal(true)
            .build();
        ConsolidationReport.IncomeStatementLineItem dto2 = ConsolidationReport.IncomeStatementLineItem.builder()
                        .lineCode("test-lineCode")
            .lineName("test-lineName")
            .amount(BigDecimal.TEN)
            .priorPeriodAmount(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .percentageOfRevenue(BigDecimal.TEN)
            .hierarchyLevel(42)
            .isHeader(true)
            .isTotal(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidationReport.IncomeStatementLineItem dto = ConsolidationReport.IncomeStatementLineItem.builder()
                        .lineCode("test-lineCode")
            .lineName("test-lineName")
            .amount(BigDecimal.TEN)
            .priorPeriodAmount(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .percentageOfRevenue(BigDecimal.TEN)
            .hierarchyLevel(42)
            .isHeader(true)
            .isTotal(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}