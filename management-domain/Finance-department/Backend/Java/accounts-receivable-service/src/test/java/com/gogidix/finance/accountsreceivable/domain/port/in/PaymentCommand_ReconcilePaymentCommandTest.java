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
class PaymentCommand_ReconcilePaymentCommandTest {

        @Test
    void testSettersAndGetters() {
        PaymentCommand.ReconcilePaymentCommand dto = new PaymentCommand.ReconcilePaymentCommand();
        dto.setTenantId("val-tenantId");
        dto.setPaymentId("val-paymentId");
        dto.setReconciledBy("val-reconciledBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-paymentId", dto.getPaymentId());
        assertEquals("val-reconciledBy", dto.getReconciledBy());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentCommand.ReconcilePaymentCommand dto1 = new PaymentCommand.ReconcilePaymentCommand();
        PaymentCommand.ReconcilePaymentCommand dto2 = new PaymentCommand.ReconcilePaymentCommand();
        dto1.setTenantId("test");
        dto1.setPaymentId("test");
        dto1.setReconciledBy("test");
        dto2.setTenantId("test");
        dto2.setPaymentId("test");
        dto2.setReconciledBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        PaymentCommand.ReconcilePaymentCommand dto = new PaymentCommand.ReconcilePaymentCommand();
        dto.setTenantId("test");
        dto.setPaymentId("test");
        dto.setReconciledBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        PaymentCommand.ReconcilePaymentCommand dto = new PaymentCommand.ReconcilePaymentCommand();
        dto.setTenantId("test");
        dto.setPaymentId("test");
        dto.setReconciledBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}