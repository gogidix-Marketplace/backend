package com.gogidix.finance.accountspayable.domain.port.in;

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
class PaymentQuery_GetPaymentsByInvoiceQueryTest {

        @Test
    void testSettersAndGetters() {
        PaymentQuery.GetPaymentsByInvoiceQuery dto = new PaymentQuery.GetPaymentsByInvoiceQuery();
        dto.setTenantId("val-tenantId");
        dto.setInvoiceId("val-invoiceId");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentQuery.GetPaymentsByInvoiceQuery dto1 = new PaymentQuery.GetPaymentsByInvoiceQuery();
        PaymentQuery.GetPaymentsByInvoiceQuery dto2 = new PaymentQuery.GetPaymentsByInvoiceQuery();
        dto1.setTenantId("test");
        dto1.setInvoiceId("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setInvoiceId("test");
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        PaymentQuery.GetPaymentsByInvoiceQuery dto = new PaymentQuery.GetPaymentsByInvoiceQuery();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        PaymentQuery.GetPaymentsByInvoiceQuery dto = new PaymentQuery.GetPaymentsByInvoiceQuery();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}