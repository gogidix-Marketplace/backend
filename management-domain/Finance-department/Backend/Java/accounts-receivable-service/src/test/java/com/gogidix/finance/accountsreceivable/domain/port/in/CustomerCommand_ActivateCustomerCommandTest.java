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
class CustomerCommand_ActivateCustomerCommandTest {

        @Test
    void testSettersAndGetters() {
        CustomerCommand.ActivateCustomerCommand dto = new CustomerCommand.ActivateCustomerCommand();
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerCommand.ActivateCustomerCommand dto1 = new CustomerCommand.ActivateCustomerCommand();
        CustomerCommand.ActivateCustomerCommand dto2 = new CustomerCommand.ActivateCustomerCommand();
        dto1.setTenantId("test");
        dto1.setCustomerId("test");
        dto2.setTenantId("test");
        dto2.setCustomerId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CustomerCommand.ActivateCustomerCommand dto = new CustomerCommand.ActivateCustomerCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CustomerCommand.ActivateCustomerCommand dto = new CustomerCommand.ActivateCustomerCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}