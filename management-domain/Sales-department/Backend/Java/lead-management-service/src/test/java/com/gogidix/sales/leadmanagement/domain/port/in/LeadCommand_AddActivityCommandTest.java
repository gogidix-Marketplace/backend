package com.gogidix.sales.leadmanagement.domain.port.in;

import com.gogidix.sales.leadmanagement.domain.model.LeadActivity;
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
class LeadCommand_AddActivityCommandTest {

        @Test
    void testSettersAndGetters() {
        LeadCommand.AddActivityCommand dto = new LeadCommand.AddActivityCommand();
        dto.setTenantId("val-tenantId");
        dto.setLeadId("val-leadId");
        dto.setSubject("val-subject");
        dto.setDescription("val-description");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-leadId", dto.getLeadId());
        assertEquals("val-subject", dto.getSubject());
        assertEquals("val-description", dto.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadCommand.AddActivityCommand dto1 = new LeadCommand.AddActivityCommand();
        LeadCommand.AddActivityCommand dto2 = new LeadCommand.AddActivityCommand();
        dto1.setTenantId("test");
        dto1.setLeadId("test");
        dto1.setActivityType(LeadActivity.ActivityType.EMAIL);
        dto1.setSubject("test");
        dto1.setDescription("test");
        dto1.setDueDate(null);
        dto1.setPriority(LeadActivity.Priority.LOW);
        dto2.setTenantId("test");
        dto2.setLeadId("test");
        dto2.setActivityType(LeadActivity.ActivityType.EMAIL);
        dto2.setSubject("test");
        dto2.setDescription("test");
        dto2.setDueDate(null);
        dto2.setPriority(LeadActivity.Priority.LOW);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LeadCommand.AddActivityCommand dto = new LeadCommand.AddActivityCommand();
        dto.setTenantId("test");
        dto.setLeadId("test");
        dto.setActivityType(LeadActivity.ActivityType.EMAIL);
        dto.setSubject("test");
        dto.setDescription("test");
        dto.setDueDate(null);
        dto.setPriority(LeadActivity.Priority.LOW);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LeadCommand.AddActivityCommand dto = new LeadCommand.AddActivityCommand();
        dto.setTenantId("test");
        dto.setLeadId("test");
        dto.setActivityType(LeadActivity.ActivityType.EMAIL);
        dto.setSubject("test");
        dto.setDescription("test");
        dto.setDueDate(null);
        dto.setPriority(LeadActivity.Priority.LOW);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}