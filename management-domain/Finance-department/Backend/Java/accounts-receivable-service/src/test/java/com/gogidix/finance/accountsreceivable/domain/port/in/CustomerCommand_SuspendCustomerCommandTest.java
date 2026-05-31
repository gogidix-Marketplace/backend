package com.gogidix.finance.accountsreceivable.domain.port.in;

import com.gogidix.finance.accountsreceivable.domain.port.in.CustomerCommand;
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
class CustomerCommand_SuspendCustomerCommandTest {

        @Test
    void testSettersAndGetters() {
        CustomerCommand.SuspendCustomerCommand dto = new CustomerCommand.SuspendCustomerCommand();
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerCommand.SuspendCustomerCommand dto1 = new CustomerCommand.SuspendCustomerCommand();
        CustomerCommand.SuspendCustomerCommand dto2 = new CustomerCommand.SuspendCustomerCommand();
        dto1.setTenantId("test");
        dto1.setCustomerId("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setCustomerId("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CustomerCommand.SuspendCustomerCommand dto = new CustomerCommand.SuspendCustomerCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CustomerCommand.SuspendCustomerCommand dto = new CustomerCommand.SuspendCustomerCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}