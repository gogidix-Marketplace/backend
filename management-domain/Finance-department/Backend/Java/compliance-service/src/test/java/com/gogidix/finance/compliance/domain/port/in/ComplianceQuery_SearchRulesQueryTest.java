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
class ComplianceQuery_SearchRulesQueryTest {

        @Test
    void testSettersAndGetters() {
        ComplianceQuery.SearchRulesQuery dto = new ComplianceQuery.SearchRulesQuery();
        dto.setTenantId("val-tenantId");
        dto.setSearchTerm("val-searchTerm");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-searchTerm", dto.getSearchTerm());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceQuery.SearchRulesQuery dto1 = new ComplianceQuery.SearchRulesQuery();
        ComplianceQuery.SearchRulesQuery dto2 = new ComplianceQuery.SearchRulesQuery();
        dto1.setTenantId("test");
        dto1.setSearchTerm("test");
        dto1.setRuleType(ComplianceRule.RuleType.AMOUNT_THRESHOLD);
        dto1.setCategory(ComplianceRule.RuleCategory.EXPENSE_MANAGEMENT);
        dto1.setSeverity(ComplianceRule.SeverityLevel.INFO);
        dto1.setTags(Collections.emptyList());
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setSearchTerm("test");
        dto2.setRuleType(ComplianceRule.RuleType.AMOUNT_THRESHOLD);
        dto2.setCategory(ComplianceRule.RuleCategory.EXPENSE_MANAGEMENT);
        dto2.setSeverity(ComplianceRule.SeverityLevel.INFO);
        dto2.setTags(Collections.emptyList());
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ComplianceQuery.SearchRulesQuery dto = new ComplianceQuery.SearchRulesQuery();
        dto.setTenantId("test");
        dto.setSearchTerm("test");
        dto.setRuleType(ComplianceRule.RuleType.AMOUNT_THRESHOLD);
        dto.setCategory(ComplianceRule.RuleCategory.EXPENSE_MANAGEMENT);
        dto.setSeverity(ComplianceRule.SeverityLevel.INFO);
        dto.setTags(Collections.emptyList());
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ComplianceQuery.SearchRulesQuery dto = new ComplianceQuery.SearchRulesQuery();
        dto.setTenantId("test");
        dto.setSearchTerm("test");
        dto.setRuleType(ComplianceRule.RuleType.AMOUNT_THRESHOLD);
        dto.setCategory(ComplianceRule.RuleCategory.EXPENSE_MANAGEMENT);
        dto.setSeverity(ComplianceRule.SeverityLevel.INFO);
        dto.setTags(Collections.emptyList());
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}