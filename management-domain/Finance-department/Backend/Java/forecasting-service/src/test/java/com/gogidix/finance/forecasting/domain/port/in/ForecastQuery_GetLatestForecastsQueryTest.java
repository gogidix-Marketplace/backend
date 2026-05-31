package com.gogidix.finance.forecasting.domain.port.in;

import com.gogidix.finance.forecasting.domain.model.Forecast;
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
class ForecastQuery_GetLatestForecastsQueryTest {

        @Test
    void testSettersAndGetters() {
        ForecastQuery.GetLatestForecastsQuery dto = new ForecastQuery.GetLatestForecastsQuery();
        dto.setTenantId("val-tenantId");
        dto.setLimit(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(99, dto.getLimit());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastQuery.GetLatestForecastsQuery dto1 = new ForecastQuery.GetLatestForecastsQuery();
        ForecastQuery.GetLatestForecastsQuery dto2 = new ForecastQuery.GetLatestForecastsQuery();
        dto1.setTenantId("test");
        dto1.setForecastType(Forecast.ForecastType.REVENUE);
        dto1.setLimit(42);
        dto1.setStatus(Forecast.ForecastStatus.DRAFT);
        dto2.setTenantId("test");
        dto2.setForecastType(Forecast.ForecastType.REVENUE);
        dto2.setLimit(42);
        dto2.setStatus(Forecast.ForecastStatus.DRAFT);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastQuery.GetLatestForecastsQuery dto = new ForecastQuery.GetLatestForecastsQuery();
        dto.setTenantId("test");
        dto.setForecastType(Forecast.ForecastType.REVENUE);
        dto.setLimit(42);
        dto.setStatus(Forecast.ForecastStatus.DRAFT);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastQuery.GetLatestForecastsQuery dto = new ForecastQuery.GetLatestForecastsQuery();
        dto.setTenantId("test");
        dto.setForecastType(Forecast.ForecastType.REVENUE);
        dto.setLimit(42);
        dto.setStatus(Forecast.ForecastStatus.DRAFT);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}