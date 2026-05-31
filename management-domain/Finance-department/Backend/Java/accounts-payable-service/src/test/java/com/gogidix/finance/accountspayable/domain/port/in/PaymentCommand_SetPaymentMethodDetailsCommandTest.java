package com.gogidix.finance.accountspayable.domain.port.in;

import com.gogidix.finance.accountspayable.domain.port.in.PaymentCommand;
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
class PaymentCommand_SetPaymentMethodDetailsCommandTest {

        @Test
    void testSettersAndGetters() {
        PaymentCommand.SetPaymentMethodDetailsCommand dto = new PaymentCommand.SetPaymentMethodDetailsCommand();
        dto.setTenantId("val-tenantId");
        dto.setPaymentId("val-paymentId");
        dto.setBankAccountNumber("val-bankAccountNumber");
        dto.setBankRoutingNumber("val-bankRoutingNumber");
        dto.setCheckNumber("val-checkNumber");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-paymentId", dto.getPaymentId());
        assertEquals("val-bankAccountNumber", dto.getBankAccountNumber());
        assertEquals("val-bankRoutingNumber", dto.getBankRoutingNumber());
        assertEquals("val-checkNumber", dto.getCheckNumber());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentCommand.SetPaymentMethodDetailsCommand dto1 = new PaymentCommand.SetPaymentMethodDetailsCommand();
        PaymentCommand.SetPaymentMethodDetailsCommand dto2 = new PaymentCommand.SetPaymentMethodDetailsCommand();
        dto1.setTenantId("test");
        dto1.setPaymentId("test");
        dto1.setBankAccountNumber("test");
        dto1.setBankRoutingNumber("test");
        dto1.setCheckNumber("test");
        dto2.setTenantId("test");
        dto2.setPaymentId("test");
        dto2.setBankAccountNumber("test");
        dto2.setBankRoutingNumber("test");
        dto2.setCheckNumber("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        PaymentCommand.SetPaymentMethodDetailsCommand dto = new PaymentCommand.SetPaymentMethodDetailsCommand();
        dto.setTenantId("test");
        dto.setPaymentId("test");
        dto.setBankAccountNumber("test");
        dto.setBankRoutingNumber("test");
        dto.setCheckNumber("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        PaymentCommand.SetPaymentMethodDetailsCommand dto = new PaymentCommand.SetPaymentMethodDetailsCommand();
        dto.setTenantId("test");
        dto.setPaymentId("test");
        dto.setBankAccountNumber("test");
        dto.setBankRoutingNumber("test");
        dto.setCheckNumber("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}