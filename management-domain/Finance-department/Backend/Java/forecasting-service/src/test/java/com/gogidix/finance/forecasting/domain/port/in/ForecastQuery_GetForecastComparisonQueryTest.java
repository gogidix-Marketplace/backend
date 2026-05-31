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
class ForecastQuery_GetForecastComparisonQueryTest {

        @Test
    void testSettersAndGetters() {
        ForecastQuery.GetForecastComparisonQuery dto = new ForecastQuery.GetForecastComparisonQuery();
        dto.setTenantId("val-tenantId");
        dto.setIncludeMetrics(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertTrue(dto.isIncludeMetrics());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastQuery.GetForecastComparisonQuery dto1 = new ForecastQuery.GetForecastComparisonQuery();
        ForecastQuery.GetForecastComparisonQuery dto2 = new ForecastQuery.GetForecastComparisonQuery();
        dto1.setTenantId("test");
        dto1.setForecastIds(Collections.emptyList());
        dto1.setIncludeMetrics(true);
        dto2.setTenantId("test");
        dto2.setForecastIds(Collections.emptyList());
        dto2.setIncludeMetrics(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastQuery.GetForecastComparisonQuery dto = new ForecastQuery.GetForecastComparisonQuery();
        dto.setTenantId("test");
        dto.setForecastIds(Collections.emptyList());
        dto.setIncludeMetrics(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastQuery.GetForecastComparisonQuery dto = new ForecastQuery.GetForecastComparisonQuery();
        dto.setTenantId("test");
        dto.setForecastIds(Collections.emptyList());
        dto.setIncludeMetrics(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}