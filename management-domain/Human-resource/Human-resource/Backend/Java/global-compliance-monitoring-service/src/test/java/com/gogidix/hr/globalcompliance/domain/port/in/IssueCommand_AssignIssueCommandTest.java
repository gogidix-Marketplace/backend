package com.gogidix.hr.globalcompliance.domain.port.in;

import com.gogidix.hr.globalcompliance.domain.port.in.IssueCommand;
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
class IssueCommand_AssignIssueCommandTest {

        @Test
    void testSettersAndGetters() {
        IssueCommand.AssignIssueCommand dto = new IssueCommand.AssignIssueCommand();
        dto.setTenantId("val-tenantId");
        dto.setIssueId("val-issueId");
        dto.setAssignedTo("val-assignedTo");
        dto.setAssignedToName("val-assignedToName");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-issueId", dto.getIssueId());
        assertEquals("val-assignedTo", dto.getAssignedTo());
        assertEquals("val-assignedToName", dto.getAssignedToName());
    }

    @Test
    void testEqualsAndHashCode() {
        IssueCommand.AssignIssueCommand dto1 = new IssueCommand.AssignIssueCommand();
        IssueCommand.AssignIssueCommand dto2 = new IssueCommand.AssignIssueCommand();
        dto1.setTenantId("test");
        dto1.setIssueId("test");
        dto1.setAssignedTo("test");
        dto1.setAssignedToName("test");
        dto2.setTenantId("test");
        dto2.setIssueId("test");
        dto2.setAssignedTo("test");
        dto2.setAssignedToName("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        IssueCommand.AssignIssueCommand dto = new IssueCommand.AssignIssueCommand();
        dto.setTenantId("test");
        dto.setIssueId("test");
        dto.setAssignedTo("test");
        dto.setAssignedToName("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        IssueCommand.AssignIssueCommand dto = new IssueCommand.AssignIssueCommand();
        dto.setTenantId("test");
        dto.setIssueId("test");
        dto.setAssignedTo("test");
        dto.setAssignedToName("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}