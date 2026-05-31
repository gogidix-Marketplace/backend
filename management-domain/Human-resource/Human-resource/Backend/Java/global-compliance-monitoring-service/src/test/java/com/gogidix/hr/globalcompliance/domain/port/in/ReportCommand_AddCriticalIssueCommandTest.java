package com.gogidix.hr.globalcompliance.domain.port.in;

import com.gogidix.hr.globalcompliance.domain.port.in.ReportCommand;
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
class ReportCommand_AddCriticalIssueCommandTest {

        @Test
    void testSettersAndGetters() {
        ReportCommand.AddCriticalIssueCommand dto = new ReportCommand.AddCriticalIssueCommand();
        dto.setTenantId("val-tenantId");
        dto.setReportId("val-reportId");
        dto.setCriticalIssue("val-criticalIssue");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reportId", dto.getReportId());
        assertEquals("val-criticalIssue", dto.getCriticalIssue());
    }

    @Test
    void testEqualsAndHashCode() {
        ReportCommand.AddCriticalIssueCommand dto1 = new ReportCommand.AddCriticalIssueCommand();
        ReportCommand.AddCriticalIssueCommand dto2 = new ReportCommand.AddCriticalIssueCommand();
        dto1.setTenantId("test");
        dto1.setReportId("test");
        dto1.setCriticalIssue("test");
        dto2.setTenantId("test");
        dto2.setReportId("test");
        dto2.setCriticalIssue("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReportCommand.AddCriticalIssueCommand dto = new ReportCommand.AddCriticalIssueCommand();
        dto.setTenantId("test");
        dto.setReportId("test");
        dto.setCriticalIssue("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReportCommand.AddCriticalIssueCommand dto = new ReportCommand.AddCriticalIssueCommand();
        dto.setTenantId("test");
        dto.setReportId("test");
        dto.setCriticalIssue("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}