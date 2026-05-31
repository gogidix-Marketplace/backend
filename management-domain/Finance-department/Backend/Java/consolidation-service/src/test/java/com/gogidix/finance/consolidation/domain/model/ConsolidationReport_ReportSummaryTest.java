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
class ConsolidationReport_ReportSummaryTest {

        @Test
    void testBuilder() {
        ConsolidationReport.ReportSummary dto = ConsolidationReport.ReportSummary.builder()
                        .totalAssets(BigDecimal.TEN)
            .totalLiabilities(BigDecimal.TEN)
            .totalEquity(BigDecimal.TEN)
            .totalRevenue(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .netIncome(BigDecimal.TEN)
            .grossProfit(BigDecimal.TEN)
            .operatingIncome(BigDecimal.TEN)
            .ebitda(BigDecimal.TEN)
            .subsidiaryCount(42)
            .adjustmentCount(42)
            .eliminationCount(42)
            .intercompanyEliminations(BigDecimal.TEN)
            .currencyCode("test-currencyCode")
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getTotalAssets());
        assertEquals(BigDecimal.TEN, dto.getTotalLiabilities());
        assertEquals(BigDecimal.TEN, dto.getTotalEquity());
        assertEquals(BigDecimal.TEN, dto.getTotalRevenue());
        assertEquals(BigDecimal.TEN, dto.getTotalExpenses());
        assertEquals(BigDecimal.TEN, dto.getNetIncome());
        assertEquals(BigDecimal.TEN, dto.getGrossProfit());
        assertEquals(BigDecimal.TEN, dto.getOperatingIncome());
        assertEquals(BigDecimal.TEN, dto.getEbitda());
        assertEquals(42, dto.getSubsidiaryCount());
        assertEquals(42, dto.getAdjustmentCount());
        assertEquals(42, dto.getEliminationCount());
        assertEquals(BigDecimal.TEN, dto.getIntercompanyEliminations());
        assertEquals("test-currencyCode", dto.getCurrencyCode());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidationReport.ReportSummary dto = new ConsolidationReport.ReportSummary();
        dto.setTotalAssets(BigDecimal.ONE);
        dto.setTotalLiabilities(BigDecimal.ONE);
        dto.setTotalEquity(BigDecimal.ONE);
        dto.setTotalRevenue(BigDecimal.ONE);
        dto.setTotalExpenses(BigDecimal.ONE);
        dto.setNetIncome(BigDecimal.ONE);
        dto.setGrossProfit(BigDecimal.ONE);
        dto.setOperatingIncome(BigDecimal.ONE);
        dto.setEbitda(BigDecimal.ONE);
        dto.setSubsidiaryCount(99);
        dto.setAdjustmentCount(99);
        dto.setEliminationCount(99);
        dto.setIntercompanyEliminations(BigDecimal.ONE);
        dto.setCurrencyCode("val-currencyCode");
        assertEquals(BigDecimal.ONE, dto.getTotalAssets());
        assertEquals(BigDecimal.ONE, dto.getTotalLiabilities());
        assertEquals(BigDecimal.ONE, dto.getTotalEquity());
        assertEquals(BigDecimal.ONE, dto.getTotalRevenue());
        assertEquals(BigDecimal.ONE, dto.getTotalExpenses());
        assertEquals(BigDecimal.ONE, dto.getNetIncome());
        assertEquals(BigDecimal.ONE, dto.getGrossProfit());
        assertEquals(BigDecimal.ONE, dto.getOperatingIncome());
        assertEquals(BigDecimal.ONE, dto.getEbitda());
        assertEquals(99, dto.getSubsidiaryCount());
        assertEquals(99, dto.getAdjustmentCount());
        assertEquals(99, dto.getEliminationCount());
        assertEquals(BigDecimal.ONE, dto.getIntercompanyEliminations());
        assertEquals("val-currencyCode", dto.getCurrencyCode());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationReport.ReportSummary dto1 = ConsolidationReport.ReportSummary.builder()
                        .totalAssets(BigDecimal.TEN)
            .totalLiabilities(BigDecimal.TEN)
            .totalEquity(BigDecimal.TEN)
            .totalRevenue(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .netIncome(BigDecimal.TEN)
            .grossProfit(BigDecimal.TEN)
            .operatingIncome(BigDecimal.TEN)
            .ebitda(BigDecimal.TEN)
            .subsidiaryCount(42)
            .adjustmentCount(42)
            .eliminationCount(42)
            .intercompanyEliminations(BigDecimal.TEN)
            .currencyCode("test-currencyCode")
            .build();
        ConsolidationReport.ReportSummary dto2 = ConsolidationReport.ReportSummary.builder()
                        .totalAssets(BigDecimal.TEN)
            .totalLiabilities(BigDecimal.TEN)
            .totalEquity(BigDecimal.TEN)
            .totalRevenue(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .netIncome(BigDecimal.TEN)
            .grossProfit(BigDecimal.TEN)
            .operatingIncome(BigDecimal.TEN)
            .ebitda(BigDecimal.TEN)
            .subsidiaryCount(42)
            .adjustmentCount(42)
            .eliminationCount(42)
            .intercompanyEliminations(BigDecimal.TEN)
            .currencyCode("test-currencyCode")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidationReport.ReportSummary dto = ConsolidationReport.ReportSummary.builder()
                        .totalAssets(BigDecimal.TEN)
            .totalLiabilities(BigDecimal.TEN)
            .totalEquity(BigDecimal.TEN)
            .totalRevenue(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .netIncome(BigDecimal.TEN)
            .grossProfit(BigDecimal.TEN)
            .operatingIncome(BigDecimal.TEN)
            .ebitda(BigDecimal.TEN)
            .subsidiaryCount(42)
            .adjustmentCount(42)
            .eliminationCount(42)
            .intercompanyEliminations(BigDecimal.TEN)
            .currencyCode("test-currencyCode")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}