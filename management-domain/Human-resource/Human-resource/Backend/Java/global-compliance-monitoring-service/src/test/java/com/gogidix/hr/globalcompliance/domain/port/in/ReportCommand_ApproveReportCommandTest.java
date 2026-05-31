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
class ReportCommand_ApproveReportCommandTest {

        @Test
    void testSettersAndGetters() {
        ReportCommand.ApproveReportCommand dto = new ReportCommand.ApproveReportCommand();
        dto.setTenantId("val-tenantId");
        dto.setReportId("val-reportId");
        dto.setApprovedBy("val-approvedBy");
        dto.setApprovedByName("val-approvedByName");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reportId", dto.getReportId());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals("val-approvedByName", dto.getApprovedByName());
    }

    @Test
    void testEqualsAndHashCode() {
        ReportCommand.ApproveReportCommand dto1 = new ReportCommand.ApproveReportCommand();
        ReportCommand.ApproveReportCommand dto2 = new ReportCommand.ApproveReportCommand();
        dto1.setTenantId("test");
        dto1.setReportId("test");
        dto1.setApprovedBy("test");
        dto1.setApprovedByName("test");
        dto2.setTenantId("test");
        dto2.setReportId("test");
        dto2.setApprovedBy("test");
        dto2.setApprovedByName("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReportCommand.ApproveReportCommand dto = new ReportCommand.ApproveReportCommand();
        dto.setTenantId("test");
        dto.setReportId("test");
        dto.setApprovedBy("test");
        dto.setApprovedByName("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReportCommand.ApproveReportCommand dto = new ReportCommand.ApproveReportCommand();
        dto.setTenantId("test");
        dto.setReportId("test");
        dto.setApprovedBy("test");
        dto.setApprovedByName("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}