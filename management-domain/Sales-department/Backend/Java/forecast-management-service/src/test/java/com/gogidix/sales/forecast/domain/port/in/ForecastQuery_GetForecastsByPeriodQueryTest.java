package com.gogidix.sales.forecast.domain.port.in;

import com.gogidix.sales.forecast.domain.port.in.ForecastQuery;
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
class ForecastQuery_GetForecastsByPeriodQueryTest {

        @Test
    void testSettersAndGetters() {
        ForecastQuery.GetForecastsByPeriodQuery dto = new ForecastQuery.GetForecastsByPeriodQuery();
        dto.setPeriod("val-period");
        dto.setYear(99);
        dto.setMonthOrQuarter(99);
        assertEquals("val-period", dto.getPeriod());
        assertEquals(99, dto.getYear());
        assertEquals(99, dto.getMonthOrQuarter());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastQuery.GetForecastsByPeriodQuery dto1 = new ForecastQuery.GetForecastsByPeriodQuery();
        ForecastQuery.GetForecastsByPeriodQuery dto2 = new ForecastQuery.GetForecastsByPeriodQuery();
        dto1.setPeriod("test");
        dto1.setYear(42);
        dto1.setMonthOrQuarter(42);
        dto2.setPeriod("test");
        dto2.setYear(42);
        dto2.setMonthOrQuarter(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setPeriod(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastQuery.GetForecastsByPeriodQuery dto = new ForecastQuery.GetForecastsByPeriodQuery();
        dto.setPeriod("test");
        dto.setYear(42);
        dto.setMonthOrQuarter(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastQuery.GetForecastsByPeriodQuery dto = new ForecastQuery.GetForecastsByPeriodQuery();
        dto.setPeriod("test");
        dto.setYear(42);
        dto.setMonthOrQuarter(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}