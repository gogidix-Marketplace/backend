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
class ForecastQuery_GetByScenarioQueryTest {

        @Test
    void testSettersAndGetters() {
        ForecastQuery.GetByScenarioQuery dto = new ForecastQuery.GetByScenarioQuery();
        dto.setTenantId("val-tenantId");
        dto.setScenario("val-scenario");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-scenario", dto.getScenario());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastQuery.GetByScenarioQuery dto1 = new ForecastQuery.GetByScenarioQuery();
        ForecastQuery.GetByScenarioQuery dto2 = new ForecastQuery.GetByScenarioQuery();
        dto1.setTenantId("test");
        dto1.setScenario("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setScenario("test");
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastQuery.GetByScenarioQuery dto = new ForecastQuery.GetByScenarioQuery();
        dto.setTenantId("test");
        dto.setScenario("test");
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastQuery.GetByScenarioQuery dto = new ForecastQuery.GetByScenarioQuery();
        dto.setTenantId("test");
        dto.setScenario("test");
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}