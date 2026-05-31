package com.gogidix.finance.compliance.domain.model;

import com.gogidix.finance.compliance.domain.model.ComplianceReport;
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
class ComplianceReport_ReportViolationSummaryTest {

        @Test
    void testBuilder() {
        ComplianceReport.ReportViolationSummary dto = ComplianceReport.ReportViolationSummary.builder()
                        .ruleId("test-ruleId")
            .ruleName("test-ruleName")
            .violationCount(42L)
            .severity("test-severity")
            .topDepartment("test-topDepartment")
            .avgVariance(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-ruleId", dto.getRuleId());
        assertEquals("test-ruleName", dto.getRuleName());
        assertEquals(42L, dto.getViolationCount());
        assertEquals("test-severity", dto.getSeverity());
        assertEquals("test-topDepartment", dto.getTopDepartment());
    }

    @Test
    void testSettersAndGetters() {
        ComplianceReport.ReportViolationSummary dto = new ComplianceReport.ReportViolationSummary();
        dto.setRuleId("val-ruleId");
        dto.setRuleName("val-ruleName");
        dto.setSeverity("val-severity");
        dto.setTopDepartment("val-topDepartment");
        assertEquals("val-ruleId", dto.getRuleId());
        assertEquals("val-ruleName", dto.getRuleName());
        assertEquals("val-severity", dto.getSeverity());
        assertEquals("val-topDepartment", dto.getTopDepartment());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceReport.ReportViolationSummary dto1 = ComplianceReport.ReportViolationSummary.builder()
                        .ruleId("test-ruleId")
            .ruleName("test-ruleName")
            .violationCount(42L)
            .severity("test-severity")
            .topDepartment("test-topDepartment")
            .avgVariance(null)
            .build();
        ComplianceReport.ReportViolationSummary dto2 = ComplianceReport.ReportViolationSummary.builder()
                        .ruleId("test-ruleId")
            .ruleName("test-ruleName")
            .violationCount(42L)
            .severity("test-severity")
            .topDepartment("test-topDepartment")
            .avgVariance(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ComplianceReport.ReportViolationSummary dto = ComplianceReport.ReportViolationSummary.builder()
                        .ruleId("test-ruleId")
            .ruleName("test-ruleName")
            .violationCount(42L)
            .severity("test-severity")
            .topDepartment("test-topDepartment")
            .avgVariance(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}