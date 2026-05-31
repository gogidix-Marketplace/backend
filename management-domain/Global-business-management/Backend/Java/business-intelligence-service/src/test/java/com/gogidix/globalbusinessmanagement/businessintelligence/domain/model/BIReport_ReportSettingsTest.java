package com.gogidix.globalbusinessmanagement.businessintelligence.domain.model;

import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.BIReport;
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
class BIReport_ReportSettingsTest {

        @Test
    void testBuilder() {
        BIReport.ReportSettings dto = BIReport.ReportSettings.builder()
                        .includeCharts(true)
            .includeTables(true)
            .includeRawData(true)
            .includeInsights(true)
            .chartType("test-chartType")
            .dateFormat("test-dateFormat")
            .numberFormat("test-numberFormat")
            .currencyFormat("test-currencyFormat")
            .build();
        assertNotNull(dto);
        assertTrue(dto.getIncludeCharts());
        assertTrue(dto.getIncludeTables());
        assertTrue(dto.getIncludeRawData());
        assertTrue(dto.getIncludeInsights());
        assertEquals("test-chartType", dto.getChartType());
        assertEquals("test-dateFormat", dto.getDateFormat());
        assertEquals("test-numberFormat", dto.getNumberFormat());
        assertEquals("test-currencyFormat", dto.getCurrencyFormat());
    }

    @Test
    void testSettersAndGetters() {
        BIReport.ReportSettings dto = new BIReport.ReportSettings();
        dto.setIncludeCharts(true);
        dto.setIncludeTables(true);
        dto.setIncludeRawData(true);
        dto.setIncludeInsights(true);
        dto.setChartType("val-chartType");
        dto.setDateFormat("val-dateFormat");
        dto.setNumberFormat("val-numberFormat");
        dto.setCurrencyFormat("val-currencyFormat");
        assertTrue(dto.getIncludeCharts());
        assertTrue(dto.getIncludeTables());
        assertTrue(dto.getIncludeRawData());
        assertTrue(dto.getIncludeInsights());
        assertEquals("val-chartType", dto.getChartType());
        assertEquals("val-dateFormat", dto.getDateFormat());
        assertEquals("val-numberFormat", dto.getNumberFormat());
        assertEquals("val-currencyFormat", dto.getCurrencyFormat());
    }

    @Test
    void testEqualsAndHashCode() {
        BIReport.ReportSettings dto1 = BIReport.ReportSettings.builder()
                        .includeCharts(true)
            .includeTables(true)
            .includeRawData(true)
            .includeInsights(true)
            .chartType("test-chartType")
            .dateFormat("test-dateFormat")
            .numberFormat("test-numberFormat")
            .currencyFormat("test-currencyFormat")
            .build();
        BIReport.ReportSettings dto2 = BIReport.ReportSettings.builder()
                        .includeCharts(true)
            .includeTables(true)
            .includeRawData(true)
            .includeInsights(true)
            .chartType("test-chartType")
            .dateFormat("test-dateFormat")
            .numberFormat("test-numberFormat")
            .currencyFormat("test-currencyFormat")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BIReport.ReportSettings dto = BIReport.ReportSettings.builder()
                        .includeCharts(true)
            .includeTables(true)
            .includeRawData(true)
            .includeInsights(true)
            .chartType("test-chartType")
            .dateFormat("test-dateFormat")
            .numberFormat("test-numberFormat")
            .currencyFormat("test-currencyFormat")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}