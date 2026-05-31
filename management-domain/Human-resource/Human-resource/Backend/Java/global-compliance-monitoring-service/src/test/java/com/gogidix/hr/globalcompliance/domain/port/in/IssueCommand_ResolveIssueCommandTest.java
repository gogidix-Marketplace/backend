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
class IssueCommand_ResolveIssueCommandTest {

        @Test
    void testSettersAndGetters() {
        IssueCommand.ResolveIssueCommand dto = new IssueCommand.ResolveIssueCommand();
        dto.setTenantId("val-tenantId");
        dto.setIssueId("val-issueId");
        dto.setResolution("val-resolution");
        dto.setRootCause("val-rootCause");
        dto.setResolvedBy("val-resolvedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-issueId", dto.getIssueId());
        assertEquals("val-resolution", dto.getResolution());
        assertEquals("val-rootCause", dto.getRootCause());
        assertEquals("val-resolvedBy", dto.getResolvedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        IssueCommand.ResolveIssueCommand dto1 = new IssueCommand.ResolveIssueCommand();
        IssueCommand.ResolveIssueCommand dto2 = new IssueCommand.ResolveIssueCommand();
        dto1.setTenantId("test");
        dto1.setIssueId("test");
        dto1.setResolution("test");
        dto1.setRootCause("test");
        dto1.setResolvedBy("test");
        dto2.setTenantId("test");
        dto2.setIssueId("test");
        dto2.setResolution("test");
        dto2.setRootCause("test");
        dto2.setResolvedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        IssueCommand.ResolveIssueCommand dto = new IssueCommand.ResolveIssueCommand();
        dto.setTenantId("test");
        dto.setIssueId("test");
        dto.setResolution("test");
        dto.setRootCause("test");
        dto.setResolvedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        IssueCommand.ResolveIssueCommand dto = new IssueCommand.ResolveIssueCommand();
        dto.setTenantId("test");
        dto.setIssueId("test");
        dto.setResolution("test");
        dto.setRootCause("test");
        dto.setResolvedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}