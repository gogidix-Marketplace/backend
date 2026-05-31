package com.gogidix.hr.globalworkforceanalytics.domain.model;

import com.gogidix.hr.globalworkforceanalytics.domain.model.TrendAnalysis;
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
class TrendAnalysis_ForecastDataPointTest {

        @Test
    void testSettersAndGetters() {
        TrendAnalysis.ForecastDataPoint dto = new TrendAnalysis.ForecastDataPoint();
        dto.setDate(LocalDate.of(2025,6,1));
        dto.setForecastValue(BigDecimal.ONE);
        dto.setLowerBound(BigDecimal.ONE);
        dto.setUpperBound(BigDecimal.ONE);
        dto.setConfidenceInterval(BigDecimal.ONE);
        dto.setForecastMethod("val-forecastMethod");
        assertEquals(LocalDate.of(2025,6,1), dto.getDate());
        assertEquals(BigDecimal.ONE, dto.getForecastValue());
        assertEquals(BigDecimal.ONE, dto.getLowerBound());
        assertEquals(BigDecimal.ONE, dto.getUpperBound());
        assertEquals(BigDecimal.ONE, dto.getConfidenceInterval());
        assertEquals("val-forecastMethod", dto.getForecastMethod());
    }

    @Test
    void testEqualsAndHashCode() {
        TrendAnalysis.ForecastDataPoint dto1 = new TrendAnalysis.ForecastDataPoint();
        TrendAnalysis.ForecastDataPoint dto2 = new TrendAnalysis.ForecastDataPoint();
        dto1.setPeriod(null);
        dto1.setDate(LocalDate.of(2025,1,1));
        dto1.setForecastValue(BigDecimal.TEN);
        dto1.setLowerBound(BigDecimal.TEN);
        dto1.setUpperBound(BigDecimal.TEN);
        dto1.setConfidenceInterval(BigDecimal.TEN);
        dto1.setForecastMethod("test");
        dto2.setPeriod(null);
        dto2.setDate(LocalDate.of(2025,1,1));
        dto2.setForecastValue(BigDecimal.TEN);
        dto2.setLowerBound(BigDecimal.TEN);
        dto2.setUpperBound(BigDecimal.TEN);
        dto2.setConfidenceInterval(BigDecimal.TEN);
        dto2.setForecastMethod("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setDate(LocalDate.of(2099,12,31));
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TrendAnalysis.ForecastDataPoint dto = new TrendAnalysis.ForecastDataPoint();
        dto.setPeriod(null);
        dto.setDate(LocalDate.of(2025,1,1));
        dto.setForecastValue(BigDecimal.TEN);
        dto.setLowerBound(BigDecimal.TEN);
        dto.setUpperBound(BigDecimal.TEN);
        dto.setConfidenceInterval(BigDecimal.TEN);
        dto.setForecastMethod("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TrendAnalysis.ForecastDataPoint dto = new TrendAnalysis.ForecastDataPoint();
        dto.setPeriod(null);
        dto.setDate(LocalDate.of(2025,1,1));
        dto.setForecastValue(BigDecimal.TEN);
        dto.setLowerBound(BigDecimal.TEN);
        dto.setUpperBound(BigDecimal.TEN);
        dto.setConfidenceInterval(BigDecimal.TEN);
        dto.setForecastMethod("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}