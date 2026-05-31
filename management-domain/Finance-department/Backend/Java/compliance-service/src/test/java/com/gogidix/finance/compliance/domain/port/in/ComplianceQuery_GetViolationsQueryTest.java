package com.gogidix.finance.compliance.domain.port.in;

import com.gogidix.finance.compliance.domain.model.ComplianceCheck;
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
class ComplianceQuery_GetViolationsQueryTest {

        @Test
    void testSettersAndGetters() {
        ComplianceQuery.GetViolationsQuery dto = new ComplianceQuery.GetViolationsQuery();
        dto.setTenantId("val-tenantId");
        dto.setRuleId("val-ruleId");
        dto.setIncludeWaived(true);
        dto.setIncludeRemediated(true);
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-ruleId", dto.getRuleId());
        assertTrue(dto.getIncludeWaived());
        assertTrue(dto.getIncludeRemediated());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceQuery.GetViolationsQuery dto1 = new ComplianceQuery.GetViolationsQuery();
        ComplianceQuery.GetViolationsQuery dto2 = new ComplianceQuery.GetViolationsQuery();
        dto1.setTenantId("test");
        dto1.setRuleId("test");
        dto1.setMinSeverity(ComplianceCheck.SeverityLevel.INFO);
        dto1.setIncludeWaived(true);
        dto1.setIncludeRemediated(true);
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setRuleId("test");
        dto2.setMinSeverity(ComplianceCheck.SeverityLevel.INFO);
        dto2.setIncludeWaived(true);
        dto2.setIncludeRemediated(true);
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ComplianceQuery.GetViolationsQuery dto = new ComplianceQuery.GetViolationsQuery();
        dto.setTenantId("test");
        dto.setRuleId("test");
        dto.setMinSeverity(ComplianceCheck.SeverityLevel.INFO);
        dto.setIncludeWaived(true);
        dto.setIncludeRemediated(true);
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ComplianceQuery.GetViolationsQuery dto = new ComplianceQuery.GetViolationsQuery();
        dto.setTenantId("test");
        dto.setRuleId("test");
        dto.setMinSeverity(ComplianceCheck.SeverityLevel.INFO);
        dto.setIncludeWaived(true);
        dto.setIncludeRemediated(true);
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}