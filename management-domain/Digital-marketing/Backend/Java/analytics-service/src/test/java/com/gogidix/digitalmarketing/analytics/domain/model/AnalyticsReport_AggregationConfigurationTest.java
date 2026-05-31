package com.gogidix.digitalmarketing.analytics.domain.model;

import com.gogidix.digitalmarketing.analytics.domain.model.AnalyticsReport;
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
class AnalyticsReport_AggregationConfigurationTest {

        @Test
    void testBuilder() {
        AnalyticsReport.AggregationConfiguration dto = AnalyticsReport.AggregationConfiguration.builder()
                        .type("test-type")
            .field("test-field")
            .groupBy("test-groupBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-type", dto.getType());
        assertEquals("test-field", dto.getField());
        assertEquals("test-groupBy", dto.getGroupBy());
    }

    @Test
    void testSettersAndGetters() {
        AnalyticsReport.AggregationConfiguration dto = new AnalyticsReport.AggregationConfiguration();
        dto.setType("val-type");
        dto.setField("val-field");
        dto.setGroupBy("val-groupBy");
        assertEquals("val-type", dto.getType());
        assertEquals("val-field", dto.getField());
        assertEquals("val-groupBy", dto.getGroupBy());
    }

    @Test
    void testEqualsAndHashCode() {
        AnalyticsReport.AggregationConfiguration dto1 = AnalyticsReport.AggregationConfiguration.builder()
                        .type("test-type")
            .field("test-field")
            .groupBy("test-groupBy")
            .build();
        AnalyticsReport.AggregationConfiguration dto2 = AnalyticsReport.AggregationConfiguration.builder()
                        .type("test-type")
            .field("test-field")
            .groupBy("test-groupBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AnalyticsReport.AggregationConfiguration dto = AnalyticsReport.AggregationConfiguration.builder()
                        .type("test-type")
            .field("test-field")
            .groupBy("test-groupBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}