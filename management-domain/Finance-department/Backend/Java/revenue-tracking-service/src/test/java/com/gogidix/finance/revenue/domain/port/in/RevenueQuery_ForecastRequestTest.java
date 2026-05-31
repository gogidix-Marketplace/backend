package com.gogidix.finance.revenue.domain.port.in;

import com.gogidix.finance.revenue.domain.port.in.RevenueQuery;
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
class RevenueQuery_ForecastRequestTest {

        @Test
    void testSettersAndGetters() {
        RevenueQuery.ForecastRequest dto = new RevenueQuery.ForecastRequest();
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setHorizonMonths(99);
        dto.setMethod("val-method");
        dto.setIncludeDeferred(true);
        dto.setIncludeRecurring(true);
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(99, dto.getHorizonMonths());
        assertEquals("val-method", dto.getMethod());
        assertTrue(dto.getIncludeDeferred());
        assertTrue(dto.getIncludeRecurring());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueQuery.ForecastRequest dto1 = new RevenueQuery.ForecastRequest();
        RevenueQuery.ForecastRequest dto2 = new RevenueQuery.ForecastRequest();
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setHorizonMonths(42);
        dto1.setMethod("test");
        dto1.setCustomerIds(Collections.emptyList());
        dto1.setTypes(Collections.emptyList());
        dto1.setIncludeDeferred(true);
        dto1.setIncludeRecurring(true);
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setHorizonMonths(42);
        dto2.setMethod("test");
        dto2.setCustomerIds(Collections.emptyList());
        dto2.setTypes(Collections.emptyList());
        dto2.setIncludeDeferred(true);
        dto2.setIncludeRecurring(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setStartDate(LocalDate.of(2099,12,31));
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RevenueQuery.ForecastRequest dto = new RevenueQuery.ForecastRequest();
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setHorizonMonths(42);
        dto.setMethod("test");
        dto.setCustomerIds(Collections.emptyList());
        dto.setTypes(Collections.emptyList());
        dto.setIncludeDeferred(true);
        dto.setIncludeRecurring(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RevenueQuery.ForecastRequest dto = new RevenueQuery.ForecastRequest();
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setHorizonMonths(42);
        dto.setMethod("test");
        dto.setCustomerIds(Collections.emptyList());
        dto.setTypes(Collections.emptyList());
        dto.setIncludeDeferred(true);
        dto.setIncludeRecurring(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}