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
class CustomerCommand_RemoveTagCommandTest {

        @Test
    void testSettersAndGetters() {
        CustomerCommand.RemoveTagCommand dto = new CustomerCommand.RemoveTagCommand();
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setTag("val-tag");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-tag", dto.getTag());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerCommand.RemoveTagCommand dto1 = new CustomerCommand.RemoveTagCommand();
        CustomerCommand.RemoveTagCommand dto2 = new CustomerCommand.RemoveTagCommand();
        dto1.setTenantId("test");
        dto1.setCustomerId("test");
        dto1.setTag("test");
        dto2.setTenantId("test");
        dto2.setCustomerId("test");
        dto2.setTag("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CustomerCommand.RemoveTagCommand dto = new CustomerCommand.RemoveTagCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setTag("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CustomerCommand.RemoveTagCommand dto = new CustomerCommand.RemoveTagCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setTag("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}