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
class BIReport_ReportMetadataTest {

        @Test
    void testBuilder() {
        BIReport.ReportMetadata dto = BIReport.ReportMetadata.builder()
                        .totalMetrics(42)
            .totalCharts(42)
            .totalTables(42)
            .dataSource("test-dataSource")
            .dataFreshness("test-dataFreshness")
            .generationTime("test-generationTime")
            .currency("test-currency")
            .locale("test-locale")
            .timeZone("test-timeZone")
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getTotalMetrics());
        assertEquals(42, dto.getTotalCharts());
        assertEquals(42, dto.getTotalTables());
        assertEquals("test-dataSource", dto.getDataSource());
        assertEquals("test-dataFreshness", dto.getDataFreshness());
        assertEquals("test-generationTime", dto.getGenerationTime());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-locale", dto.getLocale());
        assertEquals("test-timeZone", dto.getTimeZone());
    }

    @Test
    void testSettersAndGetters() {
        BIReport.ReportMetadata dto = new BIReport.ReportMetadata();
        dto.setTotalMetrics(99);
        dto.setTotalCharts(99);
        dto.setTotalTables(99);
        dto.setDataSource("val-dataSource");
        dto.setDataFreshness("val-dataFreshness");
        dto.setGenerationTime("val-generationTime");
        dto.setCurrency("val-currency");
        dto.setLocale("val-locale");
        dto.setTimeZone("val-timeZone");
        assertEquals(99, dto.getTotalMetrics());
        assertEquals(99, dto.getTotalCharts());
        assertEquals(99, dto.getTotalTables());
        assertEquals("val-dataSource", dto.getDataSource());
        assertEquals("val-dataFreshness", dto.getDataFreshness());
        assertEquals("val-generationTime", dto.getGenerationTime());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-locale", dto.getLocale());
        assertEquals("val-timeZone", dto.getTimeZone());
    }

    @Test
    void testEqualsAndHashCode() {
        BIReport.ReportMetadata dto1 = BIReport.ReportMetadata.builder()
                        .totalMetrics(42)
            .totalCharts(42)
            .totalTables(42)
            .dataSource("test-dataSource")
            .dataFreshness("test-dataFreshness")
            .generationTime("test-generationTime")
            .currency("test-currency")
            .locale("test-locale")
            .timeZone("test-timeZone")
            .build();
        BIReport.ReportMetadata dto2 = BIReport.ReportMetadata.builder()
                        .totalMetrics(42)
            .totalCharts(42)
            .totalTables(42)
            .dataSource("test-dataSource")
            .dataFreshness("test-dataFreshness")
            .generationTime("test-generationTime")
            .currency("test-currency")
            .locale("test-locale")
            .timeZone("test-timeZone")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BIReport.ReportMetadata dto = BIReport.ReportMetadata.builder()
                        .totalMetrics(42)
            .totalCharts(42)
            .totalTables(42)
            .dataSource("test-dataSource")
            .dataFreshness("test-dataFreshness")
            .generationTime("test-generationTime")
            .currency("test-currency")
            .locale("test-locale")
            .timeZone("test-timeZone")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}