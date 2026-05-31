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
class CustomerQuery_GetCustomerQueryTest {

        @Test
    void testSettersAndGetters() {
        CustomerQuery.GetCustomerQuery dto = new CustomerQuery.GetCustomerQuery();
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerQuery.GetCustomerQuery dto1 = new CustomerQuery.GetCustomerQuery();
        CustomerQuery.GetCustomerQuery dto2 = new CustomerQuery.GetCustomerQuery();
        dto1.setTenantId("test");
        dto1.setCustomerId("test");
        dto2.setTenantId("test");
        dto2.setCustomerId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CustomerQuery.GetCustomerQuery dto = new CustomerQuery.GetCustomerQuery();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CustomerQuery.GetCustomerQuery dto = new CustomerQuery.GetCustomerQuery();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}