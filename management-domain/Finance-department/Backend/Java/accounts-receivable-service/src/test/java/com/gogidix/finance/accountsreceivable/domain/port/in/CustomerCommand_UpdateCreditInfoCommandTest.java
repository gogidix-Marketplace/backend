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
class CustomerCommand_UpdateCreditInfoCommandTest {

        @Test
    void testSettersAndGetters() {
        CustomerCommand.UpdateCreditInfoCommand dto = new CustomerCommand.UpdateCreditInfoCommand();
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setCreditLimit(99);
        dto.setCreditDays(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals(99, dto.getCreditLimit());
        assertEquals(99, dto.getCreditDays());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerCommand.UpdateCreditInfoCommand dto1 = new CustomerCommand.UpdateCreditInfoCommand();
        CustomerCommand.UpdateCreditInfoCommand dto2 = new CustomerCommand.UpdateCreditInfoCommand();
        dto1.setTenantId("test");
        dto1.setCustomerId("test");
        dto1.setCreditLimit(42);
        dto1.setCreditDays(42);
        dto2.setTenantId("test");
        dto2.setCustomerId("test");
        dto2.setCreditLimit(42);
        dto2.setCreditDays(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CustomerCommand.UpdateCreditInfoCommand dto = new CustomerCommand.UpdateCreditInfoCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setCreditLimit(42);
        dto.setCreditDays(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CustomerCommand.UpdateCreditInfoCommand dto = new CustomerCommand.UpdateCreditInfoCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setCreditLimit(42);
        dto.setCreditDays(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}