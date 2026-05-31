package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.model.MetricRollup;
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
class MetricRollup_TimePeriodTest {

        @Test
    void testBuilder() {
        MetricRollup.TimePeriod dto = MetricRollup.TimePeriod.builder()
                        .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .periodType("test-periodType")
            .periodValue(42)
            .year(42)
            .quarter("test-quarter")
            .build();
        assertNotNull(dto);
        assertEquals(LocalDate.of(2025,1,15), dto.getStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertEquals("test-periodType", dto.getPeriodType());
        assertEquals(42, dto.getPeriodValue());
        assertEquals(42, dto.getYear());
        assertEquals("test-quarter", dto.getQuarter());
    }

    @Test
    void testSettersAndGetters() {
        MetricRollup.TimePeriod dto = new MetricRollup.TimePeriod();
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setPeriodType("val-periodType");
        dto.setPeriodValue(99);
        dto.setYear(99);
        dto.setQuarter("val-quarter");
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals("val-periodType", dto.getPeriodType());
        assertEquals(99, dto.getPeriodValue());
        assertEquals(99, dto.getYear());
        assertEquals("val-quarter", dto.getQuarter());
    }

    @Test
    void testEqualsAndHashCode() {
        MetricRollup.TimePeriod dto1 = MetricRollup.TimePeriod.builder()
                        .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .periodType("test-periodType")
            .periodValue(42)
            .year(42)
            .quarter("test-quarter")
            .build();
        MetricRollup.TimePeriod dto2 = MetricRollup.TimePeriod.builder()
                        .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .periodType("test-periodType")
            .periodValue(42)
            .year(42)
            .quarter("test-quarter")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MetricRollup.TimePeriod dto = MetricRollup.TimePeriod.builder()
                        .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .periodType("test-periodType")
            .periodValue(42)
            .year(42)
            .quarter("test-quarter")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}