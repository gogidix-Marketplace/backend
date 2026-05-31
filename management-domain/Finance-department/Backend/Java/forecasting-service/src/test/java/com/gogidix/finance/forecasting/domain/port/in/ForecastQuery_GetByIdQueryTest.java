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
class ForecastQuery_GetByIdQueryTest {

        @Test
    void testSettersAndGetters() {
        ForecastQuery.GetByIdQuery dto = new ForecastQuery.GetByIdQuery();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastQuery.GetByIdQuery dto1 = new ForecastQuery.GetByIdQuery();
        ForecastQuery.GetByIdQuery dto2 = new ForecastQuery.GetByIdQuery();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastQuery.GetByIdQuery dto = new ForecastQuery.GetByIdQuery();
        dto.setTenantId("test");
        dto.setForecastId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastQuery.GetByIdQuery dto = new ForecastQuery.GetByIdQuery();
        dto.setTenantId("test");
        dto.setForecastId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}