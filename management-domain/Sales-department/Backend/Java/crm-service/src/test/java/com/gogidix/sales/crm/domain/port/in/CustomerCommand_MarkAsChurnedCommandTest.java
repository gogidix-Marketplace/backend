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
class CustomerCommand_MarkAsChurnedCommandTest {

        @Test
    void testSettersAndGetters() {
        CustomerCommand.MarkAsChurnedCommand dto = new CustomerCommand.MarkAsChurnedCommand();
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setReason("val-reason");
        dto.setUpdatedBy("val-updatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-reason", dto.getReason());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerCommand.MarkAsChurnedCommand dto1 = new CustomerCommand.MarkAsChurnedCommand();
        CustomerCommand.MarkAsChurnedCommand dto2 = new CustomerCommand.MarkAsChurnedCommand();
        dto1.setTenantId("test");
        dto1.setCustomerId("test");
        dto1.setReason("test");
        dto1.setUpdatedBy("test");
        dto2.setTenantId("test");
        dto2.setCustomerId("test");
        dto2.setReason("test");
        dto2.setUpdatedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CustomerCommand.MarkAsChurnedCommand dto = new CustomerCommand.MarkAsChurnedCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setReason("test");
        dto.setUpdatedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CustomerCommand.MarkAsChurnedCommand dto = new CustomerCommand.MarkAsChurnedCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setReason("test");
        dto.setUpdatedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}