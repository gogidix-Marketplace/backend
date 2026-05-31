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
class IssueCommand_AddActionCommandTest {

        @Test
    void testSettersAndGetters() {
        IssueCommand.AddActionCommand dto = new IssueCommand.AddActionCommand();
        dto.setTenantId("val-tenantId");
        dto.setIssueId("val-issueId");
        dto.setAction("val-action");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-issueId", dto.getIssueId());
        assertEquals("val-action", dto.getAction());
    }

    @Test
    void testEqualsAndHashCode() {
        IssueCommand.AddActionCommand dto1 = new IssueCommand.AddActionCommand();
        IssueCommand.AddActionCommand dto2 = new IssueCommand.AddActionCommand();
        dto1.setTenantId("test");
        dto1.setIssueId("test");
        dto1.setAction("test");
        dto2.setTenantId("test");
        dto2.setIssueId("test");
        dto2.setAction("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        IssueCommand.AddActionCommand dto = new IssueCommand.AddActionCommand();
        dto.setTenantId("test");
        dto.setIssueId("test");
        dto.setAction("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        IssueCommand.AddActionCommand dto = new IssueCommand.AddActionCommand();
        dto.setTenantId("test");
        dto.setIssueId("test");
        dto.setAction("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}