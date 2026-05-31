package com.gogidix.finance.accountsreceivable.domain.port.in;

import com.gogidix.finance.accountsreceivable.domain.port.in.PaymentQuery;
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
class PaymentQuery_GetPaymentsByCustomerQueryTest {

        @Test
    void testSettersAndGetters() {
        PaymentQuery.GetPaymentsByCustomerQuery dto = new PaymentQuery.GetPaymentsByCustomerQuery();
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentQuery.GetPaymentsByCustomerQuery dto1 = new PaymentQuery.GetPaymentsByCustomerQuery();
        PaymentQuery.GetPaymentsByCustomerQuery dto2 = new PaymentQuery.GetPaymentsByCustomerQuery();
        dto1.setTenantId("test");
        dto1.setCustomerId("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setCustomerId("test");
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        PaymentQuery.GetPaymentsByCustomerQuery dto = new PaymentQuery.GetPaymentsByCustomerQuery();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        PaymentQuery.GetPaymentsByCustomerQuery dto = new PaymentQuery.GetPaymentsByCustomerQuery();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}