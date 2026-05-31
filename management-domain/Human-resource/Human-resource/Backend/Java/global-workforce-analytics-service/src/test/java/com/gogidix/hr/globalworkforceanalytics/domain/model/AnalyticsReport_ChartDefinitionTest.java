package com.gogidix.hr.globalworkforceanalytics.domain.model;

import com.gogidix.hr.globalworkforceanalytics.domain.model.AnalyticsReport;
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
class AnalyticsReport_ChartDefinitionTest {

        @Test
    void testSettersAndGetters() {
        AnalyticsReport.ChartDefinition dto = new AnalyticsReport.ChartDefinition();
        dto.setChartId("val-chartId");
        dto.setTitle("val-title");
        dto.setChartType("val-chartType");
        dto.setXAxis("val-xAxis");
        dto.setYAxis("val-yAxis");
        dto.setOrder(99);
        dto.setSectionId("val-sectionId");
        assertEquals("val-chartId", dto.getChartId());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-chartType", dto.getChartType());
        assertEquals("val-xAxis", dto.getXAxis());
        assertEquals("val-yAxis", dto.getYAxis());
        assertEquals(99, dto.getOrder());
        assertEquals("val-sectionId", dto.getSectionId());
    }

    @Test
    void testEqualsAndHashCode() {
        AnalyticsReport.ChartDefinition dto1 = new AnalyticsReport.ChartDefinition();
        AnalyticsReport.ChartDefinition dto2 = new AnalyticsReport.ChartDefinition();
        dto1.setChartId("test");
        dto1.setTitle("test");
        dto1.setChartType("test");
        dto1.setXAxis("test");
        dto1.setYAxis("test");
        dto1.setSeries(Collections.emptyList());
        dto1.setConfig(Collections.emptyMap());
        dto1.setData(Collections.emptyMap());
        dto1.setOrder(42);
        dto1.setSectionId("test");
        dto2.setChartId("test");
        dto2.setTitle("test");
        dto2.setChartType("test");
        dto2.setXAxis("test");
        dto2.setYAxis("test");
        dto2.setSeries(Collections.emptyList());
        dto2.setConfig(Collections.emptyMap());
        dto2.setData(Collections.emptyMap());
        dto2.setOrder(42);
        dto2.setSectionId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setChartId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        AnalyticsReport.ChartDefinition dto = new AnalyticsReport.ChartDefinition();
        dto.setChartId("test");
        dto.setTitle("test");
        dto.setChartType("test");
        dto.setXAxis("test");
        dto.setYAxis("test");
        dto.setSeries(Collections.emptyList());
        dto.setConfig(Collections.emptyMap());
        dto.setData(Collections.emptyMap());
        dto.setOrder(42);
        dto.setSectionId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        AnalyticsReport.ChartDefinition dto = new AnalyticsReport.ChartDefinition();
        dto.setChartId("test");
        dto.setTitle("test");
        dto.setChartType("test");
        dto.setXAxis("test");
        dto.setYAxis("test");
        dto.setSeries(Collections.emptyList());
        dto.setConfig(Collections.emptyMap());
        dto.setData(Collections.emptyMap());
        dto.setOrder(42);
        dto.setSectionId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}