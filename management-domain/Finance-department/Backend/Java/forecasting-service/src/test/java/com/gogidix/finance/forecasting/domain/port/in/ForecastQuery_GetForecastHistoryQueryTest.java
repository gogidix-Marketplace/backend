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
class ForecastQuery_GetForecastHistoryQueryTest {

        @Test
    void testSettersAndGetters() {
        ForecastQuery.GetForecastHistoryQuery dto = new ForecastQuery.GetForecastHistoryQuery();
        dto.setTenantId("val-tenantId");
        dto.setBaseForecastId("val-baseForecastId");
        dto.setLimit(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-baseForecastId", dto.getBaseForecastId());
        assertEquals(99, dto.getLimit());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastQuery.GetForecastHistoryQuery dto1 = new ForecastQuery.GetForecastHistoryQuery();
        ForecastQuery.GetForecastHistoryQuery dto2 = new ForecastQuery.GetForecastHistoryQuery();
        dto1.setTenantId("test");
        dto1.setBaseForecastId("test");
        dto1.setLimit(42);
        dto2.setTenantId("test");
        dto2.setBaseForecastId("test");
        dto2.setLimit(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastQuery.GetForecastHistoryQuery dto = new ForecastQuery.GetForecastHistoryQuery();
        dto.setTenantId("test");
        dto.setBaseForecastId("test");
        dto.setLimit(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastQuery.GetForecastHistoryQuery dto = new ForecastQuery.GetForecastHistoryQuery();
        dto.setTenantId("test");
        dto.setBaseForecastId("test");
        dto.setLimit(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}