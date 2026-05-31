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
class RevenueQuery_RevenueByCustomerTest {

        @Test
    void testSettersAndGetters() {
        RevenueQuery.RevenueByCustomer dto = new RevenueQuery.RevenueByCustomer();
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setTotalRevenue(BigDecimal.ONE);
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals(BigDecimal.ONE, dto.getTotalRevenue());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueQuery.RevenueByCustomer dto1 = new RevenueQuery.RevenueByCustomer();
        RevenueQuery.RevenueByCustomer dto2 = new RevenueQuery.RevenueByCustomer();
        dto1.setCustomerId("test");
        dto1.setCustomerName("test");
        dto1.setTotalRevenue(BigDecimal.TEN);
        dto1.setTransactionCount(42L);
        dto2.setCustomerId("test");
        dto2.setCustomerName("test");
        dto2.setTotalRevenue(BigDecimal.TEN);
        dto2.setTransactionCount(42L);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setCustomerId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RevenueQuery.RevenueByCustomer dto = new RevenueQuery.RevenueByCustomer();
        dto.setCustomerId("test");
        dto.setCustomerName("test");
        dto.setTotalRevenue(BigDecimal.TEN);
        dto.setTransactionCount(42L);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RevenueQuery.RevenueByCustomer dto = new RevenueQuery.RevenueByCustomer();
        dto.setCustomerId("test");
        dto.setCustomerName("test");
        dto.setTotalRevenue(BigDecimal.TEN);
        dto.setTransactionCount(42L);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}