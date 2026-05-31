package com.gogidix.finance.compliance.interfaces.rest;

import com.gogidix.finance.compliance.domain.model.ComplianceRule;
import com.gogidix.finance.compliance.interfaces.rest.ComplianceRuleController;
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
class ComplianceRuleController_CreateRuleRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ComplianceRuleController.CreateRuleRequestDto dto = new ComplianceRuleController.CreateRuleRequestDto();
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setThresholdAmount(BigDecimal.ONE);
        dto.setThresholdCurrency("val-thresholdCurrency");
        dto.setConditionExpression("val-conditionExpression");
        dto.setApprovalRequiredBy("val-approvalRequiredBy");
        dto.setAutoApproveThreshold(true);
        dto.setPriority(99);
        dto.setNotes("val-notes");
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getThresholdAmount());
        assertEquals("val-thresholdCurrency", dto.getThresholdCurrency());
        assertEquals("val-conditionExpression", dto.getConditionExpression());
        assertEquals("val-approvalRequiredBy", dto.getApprovalRequiredBy());
        assertTrue(dto.getAutoApproveThreshold());
        assertEquals(99, dto.getPriority());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceRuleController.CreateRuleRequestDto dto1 = new ComplianceRuleController.CreateRuleRequestDto();
        ComplianceRuleController.CreateRuleRequestDto dto2 = new ComplianceRuleController.CreateRuleRequestDto();
        dto1.setName("test");
        dto1.setDescription("test");
        dto1.setRuleType(ComplianceRule.RuleType.AMOUNT_THRESHOLD);
        dto1.setCategory(ComplianceRule.RuleCategory.EXPENSE_MANAGEMENT);
        dto1.setSeverity(ComplianceRule.SeverityLevel.INFO);
        dto1.setParameters(Collections.emptyMap());
        dto1.setThresholdAmount(BigDecimal.TEN);
        dto1.setThresholdCurrency("test");
        dto1.setConditionExpression("test");
        dto1.setApplicableDepartments(Collections.emptyList());
        dto1.setApplicableCostCenters(Collections.emptyList());
        dto1.setApplicableExpenseCategories(Collections.emptyList());
        dto1.setEffectiveFrom(null);
        dto1.setEffectiveTo(null);
        dto1.setApprovalRequiredBy("test");
        dto1.setAutoApproveThreshold(true);
        dto1.setPriority(42);
        dto1.setTags(Collections.emptyList());
        dto1.setNotes("test");
        dto2.setName("test");
        dto2.setDescription("test");
        dto2.setRuleType(ComplianceRule.RuleType.AMOUNT_THRESHOLD);
        dto2.setCategory(ComplianceRule.RuleCategory.EXPENSE_MANAGEMENT);
        dto2.setSeverity(ComplianceRule.SeverityLevel.INFO);
        dto2.setParameters(Collections.emptyMap());
        dto2.setThresholdAmount(BigDecimal.TEN);
        dto2.setThresholdCurrency("test");
        dto2.setConditionExpression("test");
        dto2.setApplicableDepartments(Collections.emptyList());
        dto2.setApplicableCostCenters(Collections.emptyList());
        dto2.setApplicableExpenseCategories(Collections.emptyList());
        dto2.setEffectiveFrom(null);
        dto2.setEffectiveTo(null);
        dto2.setApprovalRequiredBy("test");
        dto2.setAutoApproveThreshold(true);
        dto2.setPriority(42);
        dto2.setTags(Collections.emptyList());
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setName(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ComplianceRuleController.CreateRuleRequestDto dto = new ComplianceRuleController.CreateRuleRequestDto();
        dto.setName("test");
        dto.setDescription("test");
        dto.setRuleType(ComplianceRule.RuleType.AMOUNT_THRESHOLD);
        dto.setCategory(ComplianceRule.RuleCategory.EXPENSE_MANAGEMENT);
        dto.setSeverity(ComplianceRule.SeverityLevel.INFO);
        dto.setParameters(Collections.emptyMap());
        dto.setThresholdAmount(BigDecimal.TEN);
        dto.setThresholdCurrency("test");
        dto.setConditionExpression("test");
        dto.setApplicableDepartments(Collections.emptyList());
        dto.setApplicableCostCenters(Collections.emptyList());
        dto.setApplicableExpenseCategories(Collections.emptyList());
        dto.setEffectiveFrom(null);
        dto.setEffectiveTo(null);
        dto.setApprovalRequiredBy("test");
        dto.setAutoApproveThreshold(true);
        dto.setPriority(42);
        dto.setTags(Collections.emptyList());
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ComplianceRuleController.CreateRuleRequestDto dto = new ComplianceRuleController.CreateRuleRequestDto();
        dto.setName("test");
        dto.setDescription("test");
        dto.setRuleType(ComplianceRule.RuleType.AMOUNT_THRESHOLD);
        dto.setCategory(ComplianceRule.RuleCategory.EXPENSE_MANAGEMENT);
        dto.setSeverity(ComplianceRule.SeverityLevel.INFO);
        dto.setParameters(Collections.emptyMap());
        dto.setThresholdAmount(BigDecimal.TEN);
        dto.setThresholdCurrency("test");
        dto.setConditionExpression("test");
        dto.setApplicableDepartments(Collections.emptyList());
        dto.setApplicableCostCenters(Collections.emptyList());
        dto.setApplicableExpenseCategories(Collections.emptyList());
        dto.setEffectiveFrom(null);
        dto.setEffectiveTo(null);
        dto.setApprovalRequiredBy("test");
        dto.setAutoApproveThreshold(true);
        dto.setPriority(42);
        dto.setTags(Collections.emptyList());
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}