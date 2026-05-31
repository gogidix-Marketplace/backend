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
class ForecastQuery_GetByDepartmentQueryTest {

        @Test
    void testSettersAndGetters() {
        ForecastQuery.GetByDepartmentQuery dto = new ForecastQuery.GetByDepartmentQuery();
        dto.setTenantId("val-tenantId");
        dto.setDepartment("val-department");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-department", dto.getDepartment());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastQuery.GetByDepartmentQuery dto1 = new ForecastQuery.GetByDepartmentQuery();
        ForecastQuery.GetByDepartmentQuery dto2 = new ForecastQuery.GetByDepartmentQuery();
        dto1.setTenantId("test");
        dto1.setDepartment("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto1.setStatus(Forecast.ForecastStatus.DRAFT);
        dto2.setTenantId("test");
        dto2.setDepartment("test");
        dto2.setPage(42);
        dto2.setSize(42);
        dto2.setStatus(Forecast.ForecastStatus.DRAFT);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastQuery.GetByDepartmentQuery dto = new ForecastQuery.GetByDepartmentQuery();
        dto.setTenantId("test");
        dto.setDepartment("test");
        dto.setPage(42);
        dto.setSize(42);
        dto.setStatus(Forecast.ForecastStatus.DRAFT);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastQuery.GetByDepartmentQuery dto = new ForecastQuery.GetByDepartmentQuery();
        dto.setTenantId("test");
        dto.setDepartment("test");
        dto.setPage(42);
        dto.setSize(42);
        dto.setStatus(Forecast.ForecastStatus.DRAFT);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}