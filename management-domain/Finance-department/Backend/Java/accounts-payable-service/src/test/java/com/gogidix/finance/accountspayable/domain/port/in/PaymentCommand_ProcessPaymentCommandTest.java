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
class PaymentCommand_ProcessPaymentCommandTest {

        @Test
    void testSettersAndGetters() {
        PaymentCommand.ProcessPaymentCommand dto = new PaymentCommand.ProcessPaymentCommand();
        dto.setTenantId("val-tenantId");
        dto.setPaymentId("val-paymentId");
        dto.setProcessedBy("val-processedBy");
        dto.setPaymentReference("val-paymentReference");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-paymentId", dto.getPaymentId());
        assertEquals("val-processedBy", dto.getProcessedBy());
        assertEquals("val-paymentReference", dto.getPaymentReference());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentCommand.ProcessPaymentCommand dto1 = new PaymentCommand.ProcessPaymentCommand();
        PaymentCommand.ProcessPaymentCommand dto2 = new PaymentCommand.ProcessPaymentCommand();
        dto1.setTenantId("test");
        dto1.setPaymentId("test");
        dto1.setProcessedBy("test");
        dto1.setPaymentReference("test");
        dto2.setTenantId("test");
        dto2.setPaymentId("test");
        dto2.setProcessedBy("test");
        dto2.setPaymentReference("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        PaymentCommand.ProcessPaymentCommand dto = new PaymentCommand.ProcessPaymentCommand();
        dto.setTenantId("test");
        dto.setPaymentId("test");
        dto.setProcessedBy("test");
        dto.setPaymentReference("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        PaymentCommand.ProcessPaymentCommand dto = new PaymentCommand.ProcessPaymentCommand();
        dto.setTenantId("test");
        dto.setPaymentId("test");
        dto.setProcessedBy("test");
        dto.setPaymentReference("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}