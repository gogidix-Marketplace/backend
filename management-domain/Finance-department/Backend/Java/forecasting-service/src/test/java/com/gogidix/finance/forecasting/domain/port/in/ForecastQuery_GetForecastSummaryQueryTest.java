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
class ForecastQuery_GetForecastSummaryQueryTest {

        @Test
    void testSettersAndGetters() {
        ForecastQuery.GetForecastSummaryQuery dto = new ForecastQuery.GetForecastSummaryQuery();
        dto.setTenantId("val-tenantId");
        dto.setDepartment("val-department");
        dto.setScenario("val-scenario");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-scenario", dto.getScenario());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastQuery.GetForecastSummaryQuery dto1 = new ForecastQuery.GetForecastSummaryQuery();
        ForecastQuery.GetForecastSummaryQuery dto2 = new ForecastQuery.GetForecastSummaryQuery();
        dto1.setTenantId("test");
        dto1.setStartDate(null);
        dto1.setEndDate(null);
        dto1.setDepartment("test");
        dto1.setForecastType(Forecast.ForecastType.REVENUE);
        dto1.setScenario("test");
        dto2.setTenantId("test");
        dto2.setStartDate(null);
        dto2.setEndDate(null);
        dto2.setDepartment("test");
        dto2.setForecastType(Forecast.ForecastType.REVENUE);
        dto2.setScenario("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastQuery.GetForecastSummaryQuery dto = new ForecastQuery.GetForecastSummaryQuery();
        dto.setTenantId("test");
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setDepartment("test");
        dto.setForecastType(Forecast.ForecastType.REVENUE);
        dto.setScenario("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastQuery.GetForecastSummaryQuery dto = new ForecastQuery.GetForecastSummaryQuery();
        dto.setTenantId("test");
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setDepartment("test");
        dto.setForecastType(Forecast.ForecastType.REVENUE);
        dto.setScenario("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}