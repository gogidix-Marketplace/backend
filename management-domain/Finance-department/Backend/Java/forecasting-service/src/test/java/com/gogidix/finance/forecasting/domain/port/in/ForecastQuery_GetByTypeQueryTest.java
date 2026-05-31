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
class ForecastQuery_GetByTypeQueryTest {

        @Test
    void testSettersAndGetters() {
        ForecastQuery.GetByTypeQuery dto = new ForecastQuery.GetByTypeQuery();
        dto.setTenantId("val-tenantId");
        dto.setPage(99);
        dto.setSize(99);
        dto.setSortBy("val-sortBy");
        dto.setSortDirection("val-sortDirection");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
        assertEquals("val-sortBy", dto.getSortBy());
        assertEquals("val-sortDirection", dto.getSortDirection());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastQuery.GetByTypeQuery dto1 = new ForecastQuery.GetByTypeQuery();
        ForecastQuery.GetByTypeQuery dto2 = new ForecastQuery.GetByTypeQuery();
        dto1.setTenantId("test");
        dto1.setForecastType(Forecast.ForecastType.REVENUE);
        dto1.setPage(42);
        dto1.setSize(42);
        dto1.setSortBy("test");
        dto1.setSortDirection("test");
        dto2.setTenantId("test");
        dto2.setForecastType(Forecast.ForecastType.REVENUE);
        dto2.setPage(42);
        dto2.setSize(42);
        dto2.setSortBy("test");
        dto2.setSortDirection("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastQuery.GetByTypeQuery dto = new ForecastQuery.GetByTypeQuery();
        dto.setTenantId("test");
        dto.setForecastType(Forecast.ForecastType.REVENUE);
        dto.setPage(42);
        dto.setSize(42);
        dto.setSortBy("test");
        dto.setSortDirection("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastQuery.GetByTypeQuery dto = new ForecastQuery.GetByTypeQuery();
        dto.setTenantId("test");
        dto.setForecastType(Forecast.ForecastType.REVENUE);
        dto.setPage(42);
        dto.setSize(42);
        dto.setSortBy("test");
        dto.setSortDirection("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}