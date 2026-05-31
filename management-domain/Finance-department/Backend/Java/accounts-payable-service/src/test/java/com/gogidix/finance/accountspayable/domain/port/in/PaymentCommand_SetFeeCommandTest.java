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
class PaymentCommand_SetFeeCommandTest {

        @Test
    void testSettersAndGetters() {
        PaymentCommand.SetFeeCommand dto = new PaymentCommand.SetFeeCommand();
        dto.setTenantId("val-tenantId");
        dto.setPaymentId("val-paymentId");
        dto.setFeeAmount(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-paymentId", dto.getPaymentId());
        assertEquals(BigDecimal.ONE, dto.getFeeAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentCommand.SetFeeCommand dto1 = new PaymentCommand.SetFeeCommand();
        PaymentCommand.SetFeeCommand dto2 = new PaymentCommand.SetFeeCommand();
        dto1.setTenantId("test");
        dto1.setPaymentId("test");
        dto1.setFeeAmount(BigDecimal.TEN);
        dto2.setTenantId("test");
        dto2.setPaymentId("test");
        dto2.setFeeAmount(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        PaymentCommand.SetFeeCommand dto = new PaymentCommand.SetFeeCommand();
        dto.setTenantId("test");
        dto.setPaymentId("test");
        dto.setFeeAmount(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        PaymentCommand.SetFeeCommand dto = new PaymentCommand.SetFeeCommand();
        dto.setTenantId("test");
        dto.setPaymentId("test");
        dto.setFeeAmount(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}