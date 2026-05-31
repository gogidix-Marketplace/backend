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
class IssueCommand_DeleteIssueCommandTest {

        @Test
    void testSettersAndGetters() {
        IssueCommand.DeleteIssueCommand dto = new IssueCommand.DeleteIssueCommand();
        dto.setTenantId("val-tenantId");
        dto.setIssueId("val-issueId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-issueId", dto.getIssueId());
    }

    @Test
    void testEqualsAndHashCode() {
        IssueCommand.DeleteIssueCommand dto1 = new IssueCommand.DeleteIssueCommand();
        IssueCommand.DeleteIssueCommand dto2 = new IssueCommand.DeleteIssueCommand();
        dto1.setTenantId("test");
        dto1.setIssueId("test");
        dto2.setTenantId("test");
        dto2.setIssueId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        IssueCommand.DeleteIssueCommand dto = new IssueCommand.DeleteIssueCommand();
        dto.setTenantId("test");
        dto.setIssueId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        IssueCommand.DeleteIssueCommand dto = new IssueCommand.DeleteIssueCommand();
        dto.setTenantId("test");
        dto.setIssueId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}