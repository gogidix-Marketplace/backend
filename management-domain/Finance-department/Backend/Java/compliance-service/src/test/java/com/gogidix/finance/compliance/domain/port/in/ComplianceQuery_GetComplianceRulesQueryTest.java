package com.gogidix.finance.compliance.domain.port.in;

import com.gogidix.finance.compliance.domain.model.ComplianceRule;
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
class ComplianceQuery_GetComplianceRulesQueryTest {

        @Test
    void testSettersAndGetters() {
        ComplianceQuery.GetComplianceRulesQuery dto = new ComplianceQuery.GetComplianceRulesQuery();
        dto.setTenantId("val-tenantId");
        dto.setEnabled(true);
        dto.setDepartment("val-department");
        dto.setPage(99);
        dto.setSize(99);
        dto.setSortBy("val-sortBy");
        dto.setSortDirection("val-sortDirection");
        assertEquals("val-tenantId", dto.getTenantId());
        assertTrue(dto.getEnabled());
        assertEquals("val-department", dto.getDepartment());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
        assertEquals("val-sortBy", dto.getSortBy());
        assertEquals("val-sortDirection", dto.getSortDirection());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceQuery.GetComplianceRulesQuery dto1 = new ComplianceQuery.GetComplianceRulesQuery();
        ComplianceQuery.GetComplianceRulesQuery dto2 = new ComplianceQuery.GetComplianceRulesQuery();
        dto1.setTenantId("test");
        dto1.setRuleType(ComplianceRule.RuleType.AMOUNT_THRESHOLD);
        dto1.setCategory(ComplianceRule.RuleCategory.EXPENSE_MANAGEMENT);
        dto1.setStatus(ComplianceRule.RuleStatus.DRAFT);
        dto1.setEnabled(true);
        dto1.setDepartment("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto1.setSortBy("test");
        dto1.setSortDirection("test");
        dto2.setTenantId("test");
        dto2.setRuleType(ComplianceRule.RuleType.AMOUNT_THRESHOLD);
        dto2.setCategory(ComplianceRule.RuleCategory.EXPENSE_MANAGEMENT);
        dto2.setStatus(ComplianceRule.RuleStatus.DRAFT);
        dto2.setEnabled(true);
        dto2.setDepartment("test");
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
        ComplianceQuery.GetComplianceRulesQuery dto = new ComplianceQuery.GetComplianceRulesQuery();
        dto.setTenantId("test");
        dto.setRuleType(ComplianceRule.RuleType.AMOUNT_THRESHOLD);
        dto.setCategory(ComplianceRule.RuleCategory.EXPENSE_MANAGEMENT);
        dto.setStatus(ComplianceRule.RuleStatus.DRAFT);
        dto.setEnabled(true);
        dto.setDepartment("test");
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
        ComplianceQuery.GetComplianceRulesQuery dto = new ComplianceQuery.GetComplianceRulesQuery();
        dto.setTenantId("test");
        dto.setRuleType(ComplianceRule.RuleType.AMOUNT_THRESHOLD);
        dto.setCategory(ComplianceRule.RuleCategory.EXPENSE_MANAGEMENT);
        dto.setStatus(ComplianceRule.RuleStatus.DRAFT);
        dto.setEnabled(true);
        dto.setDepartment("test");
        dto.setPage(42);
        dto.setSize(42);
        dto.setSortBy("test");
        dto.setSortDirection("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}