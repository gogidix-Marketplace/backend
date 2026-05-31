package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.model.KPIWidget;
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
class KPIWidget_WidgetConfigurationTest {

        @Test
    void testBuilder() {
        KPIWidget.WidgetConfiguration dto = KPIWidget.WidgetConfiguration.builder()
                        .unit("test-unit")
            .decimalPlaces(42)
            .prefix("test-prefix")
            .suffix("test-suffix")
            .showTrend(true)
            .showTarget(true)
            .comparisonPeriod("test-comparisonPeriod")
            .showSparkline(true)
            .sparklinePoints(42)
            .aggregationType("test-aggregationType")
            .customConfig(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-unit", dto.getUnit());
        assertEquals(42, dto.getDecimalPlaces());
        assertEquals("test-prefix", dto.getPrefix());
        assertEquals("test-suffix", dto.getSuffix());
        assertTrue(dto.getShowTrend());
        assertTrue(dto.getShowTarget());
        assertEquals("test-comparisonPeriod", dto.getComparisonPeriod());
        assertTrue(dto.getShowSparkline());
        assertEquals(42, dto.getSparklinePoints());
        assertEquals("test-aggregationType", dto.getAggregationType());
    }

    @Test
    void testSettersAndGetters() {
        KPIWidget.WidgetConfiguration dto = new KPIWidget.WidgetConfiguration();
        dto.setUnit("val-unit");
        dto.setDecimalPlaces(99);
        dto.setPrefix("val-prefix");
        dto.setSuffix("val-suffix");
        dto.setShowTrend(true);
        dto.setShowTarget(true);
        dto.setComparisonPeriod("val-comparisonPeriod");
        dto.setShowSparkline(true);
        dto.setSparklinePoints(99);
        dto.setAggregationType("val-aggregationType");
        assertEquals("val-unit", dto.getUnit());
        assertEquals(99, dto.getDecimalPlaces());
        assertEquals("val-prefix", dto.getPrefix());
        assertEquals("val-suffix", dto.getSuffix());
        assertTrue(dto.getShowTrend());
        assertTrue(dto.getShowTarget());
        assertEquals("val-comparisonPeriod", dto.getComparisonPeriod());
        assertTrue(dto.getShowSparkline());
        assertEquals(99, dto.getSparklinePoints());
        assertEquals("val-aggregationType", dto.getAggregationType());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIWidget.WidgetConfiguration dto1 = KPIWidget.WidgetConfiguration.builder()
                        .unit("test-unit")
            .decimalPlaces(42)
            .prefix("test-prefix")
            .suffix("test-suffix")
            .showTrend(true)
            .showTarget(true)
            .comparisonPeriod("test-comparisonPeriod")
            .showSparkline(true)
            .sparklinePoints(42)
            .aggregationType("test-aggregationType")
            .customConfig(Collections.emptyMap())
            .build();
        KPIWidget.WidgetConfiguration dto2 = KPIWidget.WidgetConfiguration.builder()
                        .unit("test-unit")
            .decimalPlaces(42)
            .prefix("test-prefix")
            .suffix("test-suffix")
            .showTrend(true)
            .showTarget(true)
            .comparisonPeriod("test-comparisonPeriod")
            .showSparkline(true)
            .sparklinePoints(42)
            .aggregationType("test-aggregationType")
            .customConfig(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIWidget.WidgetConfiguration dto = KPIWidget.WidgetConfiguration.builder()
                        .unit("test-unit")
            .decimalPlaces(42)
            .prefix("test-prefix")
            .suffix("test-suffix")
            .showTrend(true)
            .showTarget(true)
            .comparisonPeriod("test-comparisonPeriod")
            .showSparkline(true)
            .sparklinePoints(42)
            .aggregationType("test-aggregationType")
            .customConfig(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}