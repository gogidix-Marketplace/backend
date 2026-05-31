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
class IssueCommand_AddAffectedEmployeeCommandTest {

        @Test
    void testSettersAndGetters() {
        IssueCommand.AddAffectedEmployeeCommand dto = new IssueCommand.AddAffectedEmployeeCommand();
        dto.setTenantId("val-tenantId");
        dto.setIssueId("val-issueId");
        dto.setEmployeeId("val-employeeId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-issueId", dto.getIssueId());
        assertEquals("val-employeeId", dto.getEmployeeId());
    }

    @Test
    void testEqualsAndHashCode() {
        IssueCommand.AddAffectedEmployeeCommand dto1 = new IssueCommand.AddAffectedEmployeeCommand();
        IssueCommand.AddAffectedEmployeeCommand dto2 = new IssueCommand.AddAffectedEmployeeCommand();
        dto1.setTenantId("test");
        dto1.setIssueId("test");
        dto1.setEmployeeId("test");
        dto2.setTenantId("test");
        dto2.setIssueId("test");
        dto2.setEmployeeId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        IssueCommand.AddAffectedEmployeeCommand dto = new IssueCommand.AddAffectedEmployeeCommand();
        dto.setTenantId("test");
        dto.setIssueId("test");
        dto.setEmployeeId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        IssueCommand.AddAffectedEmployeeCommand dto = new IssueCommand.AddAffectedEmployeeCommand();
        dto.setTenantId("test");
        dto.setIssueId("test");
        dto.setEmployeeId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}