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
class IssueCommand_EscalateIssueCommandTest {

        @Test
    void testSettersAndGetters() {
        IssueCommand.EscalateIssueCommand dto = new IssueCommand.EscalateIssueCommand();
        dto.setTenantId("val-tenantId");
        dto.setIssueId("val-issueId");
        dto.setReason("val-reason");
        dto.setEscalatedBy("val-escalatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-issueId", dto.getIssueId());
        assertEquals("val-reason", dto.getReason());
        assertEquals("val-escalatedBy", dto.getEscalatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        IssueCommand.EscalateIssueCommand dto1 = new IssueCommand.EscalateIssueCommand();
        IssueCommand.EscalateIssueCommand dto2 = new IssueCommand.EscalateIssueCommand();
        dto1.setTenantId("test");
        dto1.setIssueId("test");
        dto1.setReason("test");
        dto1.setEscalatedBy("test");
        dto2.setTenantId("test");
        dto2.setIssueId("test");
        dto2.setReason("test");
        dto2.setEscalatedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        IssueCommand.EscalateIssueCommand dto = new IssueCommand.EscalateIssueCommand();
        dto.setTenantId("test");
        dto.setIssueId("test");
        dto.setReason("test");
        dto.setEscalatedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        IssueCommand.EscalateIssueCommand dto = new IssueCommand.EscalateIssueCommand();
        dto.setTenantId("test");
        dto.setIssueId("test");
        dto.setReason("test");
        dto.setEscalatedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}