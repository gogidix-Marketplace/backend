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
class AnalyticsReport_SortConfigurationTest {

        @Test
    void testBuilder() {
        AnalyticsReport.SortConfiguration dto = AnalyticsReport.SortConfiguration.builder()
                        .field("test-field")
            .direction("test-direction")
            .build();
        assertNotNull(dto);
        assertEquals("test-field", dto.getField());
        assertEquals("test-direction", dto.getDirection());
    }

    @Test
    void testSettersAndGetters() {
        AnalyticsReport.SortConfiguration dto = new AnalyticsReport.SortConfiguration();
        dto.setField("val-field");
        dto.setDirection("val-direction");
        assertEquals("val-field", dto.getField());
        assertEquals("val-direction", dto.getDirection());
    }

    @Test
    void testEqualsAndHashCode() {
        AnalyticsReport.SortConfiguration dto1 = AnalyticsReport.SortConfiguration.builder()
                        .field("test-field")
            .direction("test-direction")
            .build();
        AnalyticsReport.SortConfiguration dto2 = AnalyticsReport.SortConfiguration.builder()
                        .field("test-field")
            .direction("test-direction")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AnalyticsReport.SortConfiguration dto = AnalyticsReport.SortConfiguration.builder()
                        .field("test-field")
            .direction("test-direction")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}