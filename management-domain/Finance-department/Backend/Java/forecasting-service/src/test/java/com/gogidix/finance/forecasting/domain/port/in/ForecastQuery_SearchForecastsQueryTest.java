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
class ForecastQuery_SearchForecastsQueryTest {

        @Test
    void testSettersAndGetters() {
        ForecastQuery.SearchForecastsQuery dto = new ForecastQuery.SearchForecastsQuery();
        dto.setTenantId("val-tenantId");
        dto.setSearchTerm("val-searchTerm");
        dto.setDepartment("val-department");
        dto.setCategory("val-category");
        dto.setPage(99);
        dto.setSize(99);
        dto.setSortBy("val-sortBy");
        dto.setSortDirection("val-sortDirection");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-searchTerm", dto.getSearchTerm());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-category", dto.getCategory());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
        assertEquals("val-sortBy", dto.getSortBy());
        assertEquals("val-sortDirection", dto.getSortDirection());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastQuery.SearchForecastsQuery dto1 = new ForecastQuery.SearchForecastsQuery();
        ForecastQuery.SearchForecastsQuery dto2 = new ForecastQuery.SearchForecastsQuery();
        dto1.setTenantId("test");
        dto1.setSearchTerm("test");
        dto1.setForecastType(Forecast.ForecastType.REVENUE);
        dto1.setStatus(Forecast.ForecastStatus.DRAFT);
        dto1.setForecastHorizon(Forecast.ForecastHorizon.MONTHLY);
        dto1.setDepartment("test");
        dto1.setCategory("test");
        dto1.setStartDate(null);
        dto1.setEndDate(null);
        dto1.setPage(42);
        dto1.setSize(42);
        dto1.setSortBy("test");
        dto1.setSortDirection("test");
        dto2.setTenantId("test");
        dto2.setSearchTerm("test");
        dto2.setForecastType(Forecast.ForecastType.REVENUE);
        dto2.setStatus(Forecast.ForecastStatus.DRAFT);
        dto2.setForecastHorizon(Forecast.ForecastHorizon.MONTHLY);
        dto2.setDepartment("test");
        dto2.setCategory("test");
        dto2.setStartDate(null);
        dto2.setEndDate(null);
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
        ForecastQuery.SearchForecastsQuery dto = new ForecastQuery.SearchForecastsQuery();
        dto.setTenantId("test");
        dto.setSearchTerm("test");
        dto.setForecastType(Forecast.ForecastType.REVENUE);
        dto.setStatus(Forecast.ForecastStatus.DRAFT);
        dto.setForecastHorizon(Forecast.ForecastHorizon.MONTHLY);
        dto.setDepartment("test");
        dto.setCategory("test");
        dto.setStartDate(null);
        dto.setEndDate(null);
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
        ForecastQuery.SearchForecastsQuery dto = new ForecastQuery.SearchForecastsQuery();
        dto.setTenantId("test");
        dto.setSearchTerm("test");
        dto.setForecastType(Forecast.ForecastType.REVENUE);
        dto.setStatus(Forecast.ForecastStatus.DRAFT);
        dto.setForecastHorizon(Forecast.ForecastHorizon.MONTHLY);
        dto.setDepartment("test");
        dto.setCategory("test");
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setPage(42);
        dto.setSize(42);
        dto.setSortBy("test");
        dto.setSortDirection("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}