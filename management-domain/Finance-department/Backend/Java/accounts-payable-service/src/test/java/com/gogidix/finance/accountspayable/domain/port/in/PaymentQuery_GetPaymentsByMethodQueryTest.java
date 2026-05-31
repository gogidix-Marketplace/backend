package com.gogidix.finance.accountspayable.domain.port.in;

import com.gogidix.finance.accountspayable.domain.model.Payment;
import com.gogidix.finance.accountspayable.domain.port.in.PaymentQuery;
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
class PaymentQuery_GetPaymentsByMethodQueryTest {

        @Test
    void testSettersAndGetters() {
        PaymentQuery.GetPaymentsByMethodQuery dto = new PaymentQuery.GetPaymentsByMethodQuery();
        dto.setTenantId("val-tenantId");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentQuery.GetPaymentsByMethodQuery dto1 = new PaymentQuery.GetPaymentsByMethodQuery();
        PaymentQuery.GetPaymentsByMethodQuery dto2 = new PaymentQuery.GetPaymentsByMethodQuery();
        dto1.setTenantId("test");
        dto1.setPaymentMethod(Payment.PaymentMethod.BANK_TRANSFER);
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setPaymentMethod(Payment.PaymentMethod.BANK_TRANSFER);
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        PaymentQuery.GetPaymentsByMethodQuery dto = new PaymentQuery.GetPaymentsByMethodQuery();
        dto.setTenantId("test");
        dto.setPaymentMethod(Payment.PaymentMethod.BANK_TRANSFER);
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        PaymentQuery.GetPaymentsByMethodQuery dto = new PaymentQuery.GetPaymentsByMethodQuery();
        dto.setTenantId("test");
        dto.setPaymentMethod(Payment.PaymentMethod.BANK_TRANSFER);
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}