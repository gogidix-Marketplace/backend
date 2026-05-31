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
class IssueCommand_UpdateDueDateCommandTest {

        @Test
    void testSettersAndGetters() {
        IssueCommand.UpdateDueDateCommand dto = new IssueCommand.UpdateDueDateCommand();
        dto.setTenantId("val-tenantId");
        dto.setIssueId("val-issueId");
        dto.setNewDueDate(LocalDate.of(2025,6,1));
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-issueId", dto.getIssueId());
        assertEquals(LocalDate.of(2025,6,1), dto.getNewDueDate());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        IssueCommand.UpdateDueDateCommand dto1 = new IssueCommand.UpdateDueDateCommand();
        IssueCommand.UpdateDueDateCommand dto2 = new IssueCommand.UpdateDueDateCommand();
        dto1.setTenantId("test");
        dto1.setIssueId("test");
        dto1.setNewDueDate(LocalDate.of(2025,1,1));
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setIssueId("test");
        dto2.setNewDueDate(LocalDate.of(2025,1,1));
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        IssueCommand.UpdateDueDateCommand dto = new IssueCommand.UpdateDueDateCommand();
        dto.setTenantId("test");
        dto.setIssueId("test");
        dto.setNewDueDate(LocalDate.of(2025,1,1));
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        IssueCommand.UpdateDueDateCommand dto = new IssueCommand.UpdateDueDateCommand();
        dto.setTenantId("test");
        dto.setIssueId("test");
        dto.setNewDueDate(LocalDate.of(2025,1,1));
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}