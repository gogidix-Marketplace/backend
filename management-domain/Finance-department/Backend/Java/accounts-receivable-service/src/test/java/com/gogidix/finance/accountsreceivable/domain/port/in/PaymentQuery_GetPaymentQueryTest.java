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
class PaymentQuery_GetPaymentQueryTest {

        @Test
    void testSettersAndGetters() {
        PaymentQuery.GetPaymentQuery dto = new PaymentQuery.GetPaymentQuery();
        dto.setTenantId("val-tenantId");
        dto.setPaymentId("val-paymentId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-paymentId", dto.getPaymentId());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentQuery.GetPaymentQuery dto1 = new PaymentQuery.GetPaymentQuery();
        PaymentQuery.GetPaymentQuery dto2 = new PaymentQuery.GetPaymentQuery();
        dto1.setTenantId("test");
        dto1.setPaymentId("test");
        dto2.setTenantId("test");
        dto2.setPaymentId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        PaymentQuery.GetPaymentQuery dto = new PaymentQuery.GetPaymentQuery();
        dto.setTenantId("test");
        dto.setPaymentId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        PaymentQuery.GetPaymentQuery dto = new PaymentQuery.GetPaymentQuery();
        dto.setTenantId("test");
        dto.setPaymentId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}