package com.gogidix.finance.accountsreceivable.domain.port.in;

import com.gogidix.finance.accountsreceivable.domain.port.in.PaymentCommand;
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
class PaymentCommand_ReversePaymentCommandTest {

        @Test
    void testSettersAndGetters() {
        PaymentCommand.ReversePaymentCommand dto = new PaymentCommand.ReversePaymentCommand();
        dto.setTenantId("val-tenantId");
        dto.setPaymentId("val-paymentId");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-paymentId", dto.getPaymentId());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentCommand.ReversePaymentCommand dto1 = new PaymentCommand.ReversePaymentCommand();
        PaymentCommand.ReversePaymentCommand dto2 = new PaymentCommand.ReversePaymentCommand();
        dto1.setTenantId("test");
        dto1.setPaymentId("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setPaymentId("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        PaymentCommand.ReversePaymentCommand dto = new PaymentCommand.ReversePaymentCommand();
        dto.setTenantId("test");
        dto.setPaymentId("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        PaymentCommand.ReversePaymentCommand dto = new PaymentCommand.ReversePaymentCommand();
        dto.setTenantId("test");
        dto.setPaymentId("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}