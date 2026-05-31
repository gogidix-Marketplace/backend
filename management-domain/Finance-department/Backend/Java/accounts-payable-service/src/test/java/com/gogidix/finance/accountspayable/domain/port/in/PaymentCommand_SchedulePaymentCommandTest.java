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
class PaymentCommand_SchedulePaymentCommandTest {

        @Test
    void testSettersAndGetters() {
        PaymentCommand.SchedulePaymentCommand dto = new PaymentCommand.SchedulePaymentCommand();
        dto.setTenantId("val-tenantId");
        dto.setPaymentId("val-paymentId");
        dto.setScheduledDate(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-paymentId", dto.getPaymentId());
        assertEquals(LocalDate.of(2025,6,1), dto.getScheduledDate());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentCommand.SchedulePaymentCommand dto1 = new PaymentCommand.SchedulePaymentCommand();
        PaymentCommand.SchedulePaymentCommand dto2 = new PaymentCommand.SchedulePaymentCommand();
        dto1.setTenantId("test");
        dto1.setPaymentId("test");
        dto1.setScheduledDate(LocalDate.of(2025,1,1));
        dto2.setTenantId("test");
        dto2.setPaymentId("test");
        dto2.setScheduledDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        PaymentCommand.SchedulePaymentCommand dto = new PaymentCommand.SchedulePaymentCommand();
        dto.setTenantId("test");
        dto.setPaymentId("test");
        dto.setScheduledDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        PaymentCommand.SchedulePaymentCommand dto = new PaymentCommand.SchedulePaymentCommand();
        dto.setTenantId("test");
        dto.setPaymentId("test");
        dto.setScheduledDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}