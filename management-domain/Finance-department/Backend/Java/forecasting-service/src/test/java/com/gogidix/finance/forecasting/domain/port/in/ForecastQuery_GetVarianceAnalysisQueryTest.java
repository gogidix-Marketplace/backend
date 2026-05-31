package com.gogidix.finance.forecasting.domain.port.in;

import com.gogidix.finance.forecasting.domain.port.in.ForecastQuery;
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
class ForecastQuery_GetVarianceAnalysisQueryTest {

        @Test
    void testSettersAndGetters() {
        ForecastQuery.GetVarianceAnalysisQuery dto = new ForecastQuery.GetVarianceAnalysisQuery();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        dto.setComparisonForecastId("val-comparisonForecastId");
        dto.setIncludeMetrics(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals("val-comparisonForecastId", dto.getComparisonForecastId());
        assertTrue(dto.isIncludeMetrics());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastQuery.GetVarianceAnalysisQuery dto1 = new ForecastQuery.GetVarianceAnalysisQuery();
        ForecastQuery.GetVarianceAnalysisQuery dto2 = new ForecastQuery.GetVarianceAnalysisQuery();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto1.setComparisonForecastId("test");
        dto1.setIncludeMetrics(true);
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        dto2.setComparisonForecastId("test");
        dto2.setIncludeMetrics(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastQuery.GetVarianceAnalysisQuery dto = new ForecastQuery.GetVarianceAnalysisQuery();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setComparisonForecastId("test");
        dto.setIncludeMetrics(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastQuery.GetVarianceAnalysisQuery dto = new ForecastQuery.GetVarianceAnalysisQuery();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setComparisonForecastId("test");
        dto.setIncludeMetrics(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}