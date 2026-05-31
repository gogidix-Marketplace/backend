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
class ReportCommand_SetMetricsCommandTest {

        @Test
    void testSettersAndGetters() {
        ReportCommand.SetMetricsCommand dto = new ReportCommand.SetMetricsCommand();
        dto.setTenantId("val-tenantId");
        dto.setReportId("val-reportId");
        dto.setTotalRequirements(99);
        dto.setPassedChecks(99);
        dto.setFailedChecks(99);
        dto.setPendingChecks(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reportId", dto.getReportId());
        assertEquals(99, dto.getTotalRequirements());
        assertEquals(99, dto.getPassedChecks());
        assertEquals(99, dto.getFailedChecks());
        assertEquals(99, dto.getPendingChecks());
    }

    @Test
    void testEqualsAndHashCode() {
        ReportCommand.SetMetricsCommand dto1 = new ReportCommand.SetMetricsCommand();
        ReportCommand.SetMetricsCommand dto2 = new ReportCommand.SetMetricsCommand();
        dto1.setTenantId("test");
        dto1.setReportId("test");
        dto1.setTotalRequirements(42);
        dto1.setPassedChecks(42);
        dto1.setFailedChecks(42);
        dto1.setPendingChecks(42);
        dto2.setTenantId("test");
        dto2.setReportId("test");
        dto2.setTotalRequirements(42);
        dto2.setPassedChecks(42);
        dto2.setFailedChecks(42);
        dto2.setPendingChecks(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReportCommand.SetMetricsCommand dto = new ReportCommand.SetMetricsCommand();
        dto.setTenantId("test");
        dto.setReportId("test");
        dto.setTotalRequirements(42);
        dto.setPassedChecks(42);
        dto.setFailedChecks(42);
        dto.setPendingChecks(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReportCommand.SetMetricsCommand dto = new ReportCommand.SetMetricsCommand();
        dto.setTenantId("test");
        dto.setReportId("test");
        dto.setTotalRequirements(42);
        dto.setPassedChecks(42);
        dto.setFailedChecks(42);
        dto.setPendingChecks(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}