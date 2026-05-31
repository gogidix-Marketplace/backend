package com.gogidix.finance.compliance.domain.port.in;

import com.gogidix.finance.compliance.domain.model.ComplianceReport;
import com.gogidix.finance.compliance.domain.port.in.ComplianceQuery;
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
class ComplianceQuery_GetComplianceReportsQueryTest {

        @Test
    void testSettersAndGetters() {
        ComplianceQuery.GetComplianceReportsQuery dto = new ComplianceQuery.GetComplianceReportsQuery();
        dto.setTenantId("val-tenantId");
        dto.setPeriodStart(LocalDate.of(2025,6,1));
        dto.setPeriodEnd(LocalDate.of(2025,6,1));
        dto.setIsArchived(true);
        dto.setPage(99);
        dto.setSize(99);
        dto.setSortBy("val-sortBy");
        dto.setSortDirection("val-sortDirection");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodStart());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodEnd());
        assertTrue(dto.getIsArchived());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
        assertEquals("val-sortBy", dto.getSortBy());
        assertEquals("val-sortDirection", dto.getSortDirection());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceQuery.GetComplianceReportsQuery dto1 = new ComplianceQuery.GetComplianceReportsQuery();
        ComplianceQuery.GetComplianceReportsQuery dto2 = new ComplianceQuery.GetComplianceReportsQuery();
        dto1.setTenantId("test");
        dto1.setReportType(ComplianceReport.ReportType.DAILY_SUMMARY);
        dto1.setStatus(ComplianceReport.ReportStatus.GENERATING);
        dto1.setPeriodStart(LocalDate.of(2025,1,1));
        dto1.setPeriodEnd(LocalDate.of(2025,1,1));
        dto1.setIsArchived(true);
        dto1.setPage(42);
        dto1.setSize(42);
        dto1.setSortBy("test");
        dto1.setSortDirection("test");
        dto2.setTenantId("test");
        dto2.setReportType(ComplianceReport.ReportType.DAILY_SUMMARY);
        dto2.setStatus(ComplianceReport.ReportStatus.GENERATING);
        dto2.setPeriodStart(LocalDate.of(2025,1,1));
        dto2.setPeriodEnd(LocalDate.of(2025,1,1));
        dto2.setIsArchived(true);
        dto2.setPage(42);
        dto2.setSize(42);
        dto2.setSortBy("test");
        dto2.setSortDirection("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ComplianceQuery.GetComplianceReportsQuery dto = new ComplianceQuery.GetComplianceReportsQuery();
        dto.setTenantId("test");
        dto.setReportType(ComplianceReport.ReportType.DAILY_SUMMARY);
        dto.setStatus(ComplianceReport.ReportStatus.GENERATING);
        dto.setPeriodStart(LocalDate.of(2025,1,1));
        dto.setPeriodEnd(LocalDate.of(2025,1,1));
        dto.setIsArchived(true);
        dto.setPage(42);
        dto.setSize(42);
        dto.setSortBy("test");
        dto.setSortDirection("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ComplianceQuery.GetComplianceReportsQuery dto = new ComplianceQuery.GetComplianceReportsQuery();
        dto.setTenantId("test");
        dto.setReportType(ComplianceReport.ReportType.DAILY_SUMMARY);
        dto.setStatus(ComplianceReport.ReportStatus.GENERATING);
        dto.setPeriodStart(LocalDate.of(2025,1,1));
        dto.setPeriodEnd(LocalDate.of(2025,1,1));
        dto.setIsArchived(true);
        dto.setPage(42);
        dto.setSize(42);
        dto.setSortBy("test");
        dto.setSortDirection("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}