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
class ComplianceQuery_GetComplianceChecksQueryTest {

        @Test
    void testSettersAndGetters() {
        ComplianceQuery.GetComplianceChecksQuery dto = new ComplianceQuery.GetComplianceChecksQuery();
        dto.setTenantId("val-tenantId");
        dto.setRuleId("val-ruleId");
        dto.setEntityType("val-entityType");
        dto.setEntityId("val-entityId");
        dto.setRequiresAction(true);
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setPage(99);
        dto.setSize(99);
        dto.setSortBy("val-sortBy");
        dto.setSortDirection("val-sortDirection");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-ruleId", dto.getRuleId());
        assertEquals("val-entityType", dto.getEntityType());
        assertEquals("val-entityId", dto.getEntityId());
        assertTrue(dto.getRequiresAction());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
        assertEquals("val-sortBy", dto.getSortBy());
        assertEquals("val-sortDirection", dto.getSortDirection());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceQuery.GetComplianceChecksQuery dto1 = new ComplianceQuery.GetComplianceChecksQuery();
        ComplianceQuery.GetComplianceChecksQuery dto2 = new ComplianceQuery.GetComplianceChecksQuery();
        dto1.setTenantId("test");
        dto1.setRuleId("test");
        dto1.setEntityType("test");
        dto1.setEntityId("test");
        dto1.setResult(ComplianceCheck.CheckResult.COMPLIANT);
        dto1.setSeverity(ComplianceCheck.SeverityLevel.INFO);
        dto1.setRequiresAction(true);
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setPage(42);
        dto1.setSize(42);
        dto1.setSortBy("test");
        dto1.setSortDirection("test");
        dto2.setTenantId("test");
        dto2.setRuleId("test");
        dto2.setEntityType("test");
        dto2.setEntityId("test");
        dto2.setResult(ComplianceCheck.CheckResult.COMPLIANT);
        dto2.setSeverity(ComplianceCheck.SeverityLevel.INFO);
        dto2.setRequiresAction(true);
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
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
        ComplianceQuery.GetComplianceChecksQuery dto = new ComplianceQuery.GetComplianceChecksQuery();
        dto.setTenantId("test");
        dto.setRuleId("test");
        dto.setEntityType("test");
        dto.setEntityId("test");
        dto.setResult(ComplianceCheck.CheckResult.COMPLIANT);
        dto.setSeverity(ComplianceCheck.SeverityLevel.INFO);
        dto.setRequiresAction(true);
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
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
        ComplianceQuery.GetComplianceChecksQuery dto = new ComplianceQuery.GetComplianceChecksQuery();
        dto.setTenantId("test");
        dto.setRuleId("test");
        dto.setEntityType("test");
        dto.setEntityId("test");
        dto.setResult(ComplianceCheck.CheckResult.COMPLIANT);
        dto.setSeverity(ComplianceCheck.SeverityLevel.INFO);
        dto.setRequiresAction(true);
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setPage(42);
        dto.setSize(42);
        dto.setSortBy("test");
        dto.setSortDirection("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}