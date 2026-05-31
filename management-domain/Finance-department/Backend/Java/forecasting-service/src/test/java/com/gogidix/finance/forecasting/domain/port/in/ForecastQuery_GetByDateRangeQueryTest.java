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
class ForecastQuery_GetByDateRangeQueryTest {

        @Test
    void testSettersAndGetters() {
        ForecastQuery.GetByDateRangeQuery dto = new ForecastQuery.GetByDateRangeQuery();
        dto.setTenantId("val-tenantId");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastQuery.GetByDateRangeQuery dto1 = new ForecastQuery.GetByDateRangeQuery();
        ForecastQuery.GetByDateRangeQuery dto2 = new ForecastQuery.GetByDateRangeQuery();
        dto1.setTenantId("test");
        dto1.setStartDate(null);
        dto1.setEndDate(null);
        dto1.setStatuses(Collections.emptyList());
        dto1.setTypes(Collections.emptyList());
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setStartDate(null);
        dto2.setEndDate(null);
        dto2.setStatuses(Collections.emptyList());
        dto2.setTypes(Collections.emptyList());
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastQuery.GetByDateRangeQuery dto = new ForecastQuery.GetByDateRangeQuery();
        dto.setTenantId("test");
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setStatuses(Collections.emptyList());
        dto.setTypes(Collections.emptyList());
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastQuery.GetByDateRangeQuery dto = new ForecastQuery.GetByDateRangeQuery();
        dto.setTenantId("test");
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setStatuses(Collections.emptyList());
        dto.setTypes(Collections.emptyList());
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}