package com.gogidix.sales.leadmanagement.domain.port.in;

import com.gogidix.sales.leadmanagement.domain.port.in.LeadCommand;
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
class LeadCommand_AssignLeadCommandTest {

        @Test
    void testSettersAndGetters() {
        LeadCommand.AssignLeadCommand dto = new LeadCommand.AssignLeadCommand();
        dto.setTenantId("val-tenantId");
        dto.setLeadId("val-leadId");
        dto.setOwnerId("val-ownerId");
        dto.setOwnerName("val-ownerName");
        dto.setReason("val-reason");
        dto.setAssignmentStrategy("val-assignmentStrategy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-leadId", dto.getLeadId());
        assertEquals("val-ownerId", dto.getOwnerId());
        assertEquals("val-ownerName", dto.getOwnerName());
        assertEquals("val-reason", dto.getReason());
        assertEquals("val-assignmentStrategy", dto.getAssignmentStrategy());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadCommand.AssignLeadCommand dto1 = new LeadCommand.AssignLeadCommand();
        LeadCommand.AssignLeadCommand dto2 = new LeadCommand.AssignLeadCommand();
        dto1.setTenantId("test");
        dto1.setLeadId("test");
        dto1.setOwnerId("test");
        dto1.setOwnerName("test");
        dto1.setReason("test");
        dto1.setAssignmentStrategy("test");
        dto2.setTenantId("test");
        dto2.setLeadId("test");
        dto2.setOwnerId("test");
        dto2.setOwnerName("test");
        dto2.setReason("test");
        dto2.setAssignmentStrategy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LeadCommand.AssignLeadCommand dto = new LeadCommand.AssignLeadCommand();
        dto.setTenantId("test");
        dto.setLeadId("test");
        dto.setOwnerId("test");
        dto.setOwnerName("test");
        dto.setReason("test");
        dto.setAssignmentStrategy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LeadCommand.AssignLeadCommand dto = new LeadCommand.AssignLeadCommand();
        dto.setTenantId("test");
        dto.setLeadId("test");
        dto.setOwnerId("test");
        dto.setOwnerName("test");
        dto.setReason("test");
        dto.setAssignmentStrategy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}