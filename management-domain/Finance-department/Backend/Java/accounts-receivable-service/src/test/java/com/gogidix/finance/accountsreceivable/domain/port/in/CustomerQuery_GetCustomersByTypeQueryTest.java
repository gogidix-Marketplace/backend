package com.gogidix.finance.accountsreceivable.domain.port.in;

import com.gogidix.finance.accountsreceivable.domain.port.in.CustomerQuery;
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
class CustomerQuery_GetCustomersByTypeQueryTest {

        @Test
    void testSettersAndGetters() {
        CustomerQuery.GetCustomersByTypeQuery dto = new CustomerQuery.GetCustomersByTypeQuery();
        dto.setTenantId("val-tenantId");
        dto.setCustomerType("val-customerType");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerType", dto.getCustomerType());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerQuery.GetCustomersByTypeQuery dto1 = new CustomerQuery.GetCustomersByTypeQuery();
        CustomerQuery.GetCustomersByTypeQuery dto2 = new CustomerQuery.GetCustomersByTypeQuery();
        dto1.setTenantId("test");
        dto1.setCustomerType("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setCustomerType("test");
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CustomerQuery.GetCustomersByTypeQuery dto = new CustomerQuery.GetCustomersByTypeQuery();
        dto.setTenantId("test");
        dto.setCustomerType("test");
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CustomerQuery.GetCustomersByTypeQuery dto = new CustomerQuery.GetCustomersByTypeQuery();
        dto.setTenantId("test");
        dto.setCustomerType("test");
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}