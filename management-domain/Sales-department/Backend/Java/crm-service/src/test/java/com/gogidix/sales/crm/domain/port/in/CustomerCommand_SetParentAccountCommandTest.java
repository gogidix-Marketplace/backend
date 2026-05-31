package com.gogidix.sales.crm.domain.port.in;

import com.gogidix.sales.crm.domain.port.in.CustomerCommand;
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
class CustomerCommand_SetParentAccountCommandTest {

        @Test
    void testSettersAndGetters() {
        CustomerCommand.SetParentAccountCommand dto = new CustomerCommand.SetParentAccountCommand();
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setParentAccountId("val-parentAccountId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-parentAccountId", dto.getParentAccountId());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerCommand.SetParentAccountCommand dto1 = new CustomerCommand.SetParentAccountCommand();
        CustomerCommand.SetParentAccountCommand dto2 = new CustomerCommand.SetParentAccountCommand();
        dto1.setTenantId("test");
        dto1.setCustomerId("test");
        dto1.setParentAccountId("test");
        dto2.setTenantId("test");
        dto2.setCustomerId("test");
        dto2.setParentAccountId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CustomerCommand.SetParentAccountCommand dto = new CustomerCommand.SetParentAccountCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setParentAccountId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CustomerCommand.SetParentAccountCommand dto = new CustomerCommand.SetParentAccountCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setParentAccountId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}