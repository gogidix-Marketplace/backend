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
class CustomerQuery_GetCustomerByCodeQueryTest {

        @Test
    void testSettersAndGetters() {
        CustomerQuery.GetCustomerByCodeQuery dto = new CustomerQuery.GetCustomerByCodeQuery();
        dto.setTenantId("val-tenantId");
        dto.setCustomerCode("val-customerCode");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerCode", dto.getCustomerCode());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerQuery.GetCustomerByCodeQuery dto1 = new CustomerQuery.GetCustomerByCodeQuery();
        CustomerQuery.GetCustomerByCodeQuery dto2 = new CustomerQuery.GetCustomerByCodeQuery();
        dto1.setTenantId("test");
        dto1.setCustomerCode("test");
        dto2.setTenantId("test");
        dto2.setCustomerCode("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CustomerQuery.GetCustomerByCodeQuery dto = new CustomerQuery.GetCustomerByCodeQuery();
        dto.setTenantId("test");
        dto.setCustomerCode("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CustomerQuery.GetCustomerByCodeQuery dto = new CustomerQuery.GetCustomerByCodeQuery();
        dto.setTenantId("test");
        dto.setCustomerCode("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}