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
class IssueCommand_CloseIssueCommandTest {

        @Test
    void testSettersAndGetters() {
        IssueCommand.CloseIssueCommand dto = new IssueCommand.CloseIssueCommand();
        dto.setTenantId("val-tenantId");
        dto.setIssueId("val-issueId");
        dto.setClosedBy("val-closedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-issueId", dto.getIssueId());
        assertEquals("val-closedBy", dto.getClosedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        IssueCommand.CloseIssueCommand dto1 = new IssueCommand.CloseIssueCommand();
        IssueCommand.CloseIssueCommand dto2 = new IssueCommand.CloseIssueCommand();
        dto1.setTenantId("test");
        dto1.setIssueId("test");
        dto1.setClosedBy("test");
        dto2.setTenantId("test");
        dto2.setIssueId("test");
        dto2.setClosedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        IssueCommand.CloseIssueCommand dto = new IssueCommand.CloseIssueCommand();
        dto.setTenantId("test");
        dto.setIssueId("test");
        dto.setClosedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        IssueCommand.CloseIssueCommand dto = new IssueCommand.CloseIssueCommand();
        dto.setTenantId("test");
        dto.setIssueId("test");
        dto.setClosedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}