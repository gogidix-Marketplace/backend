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
class CustomerCommand_AssignOwnerCommandTest {

        @Test
    void testSettersAndGetters() {
        CustomerCommand.AssignOwnerCommand dto = new CustomerCommand.AssignOwnerCommand();
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setOwnerId("val-ownerId");
        dto.setOwnerName("val-ownerName");
        dto.setTerritory("val-territory");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-ownerId", dto.getOwnerId());
        assertEquals("val-ownerName", dto.getOwnerName());
        assertEquals("val-territory", dto.getTerritory());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerCommand.AssignOwnerCommand dto1 = new CustomerCommand.AssignOwnerCommand();
        CustomerCommand.AssignOwnerCommand dto2 = new CustomerCommand.AssignOwnerCommand();
        dto1.setTenantId("test");
        dto1.setCustomerId("test");
        dto1.setOwnerId("test");
        dto1.setOwnerName("test");
        dto1.setTerritory("test");
        dto2.setTenantId("test");
        dto2.setCustomerId("test");
        dto2.setOwnerId("test");
        dto2.setOwnerName("test");
        dto2.setTerritory("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CustomerCommand.AssignOwnerCommand dto = new CustomerCommand.AssignOwnerCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setOwnerId("test");
        dto.setOwnerName("test");
        dto.setTerritory("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CustomerCommand.AssignOwnerCommand dto = new CustomerCommand.AssignOwnerCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setOwnerId("test");
        dto.setOwnerName("test");
        dto.setTerritory("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}