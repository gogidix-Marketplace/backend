package com.gogidix.finance.compliance.application.dto.response;

import com.gogidix.finance.compliance.application.dto.response.ComplianceRuleResponseDto;
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
class ComplianceRuleResponseDtoTest {

        @Test
    void testBuilder() {
        ComplianceRuleResponseDto dto = ComplianceRuleResponseDto.builder()
                        .id("test-id")
            .ruleId("test-ruleId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .ruleType(ComplianceRuleResponseDto.RuleTypeDto.AMOUNT_THRESHOLD)
            .category(ComplianceRuleResponseDto.RuleCategoryDto.EXPENSE_MANAGEMENT)
            .severity(ComplianceRuleResponseDto.SeverityLevelDto.INFO)
            .enabled(true)
            .parameters(Collections.emptyMap())
            .thresholdAmount(BigDecimal.TEN)
            .thresholdCurrency("test-thresholdCurrency")
            .conditionExpression("test-conditionExpression")
            .applicableDepartments(Collections.emptyList())
            .applicableCostCenters(Collections.emptyList())
            .applicableExpenseCategories(Collections.emptyList())
            .createdByUserId("test-createdByUserId")
            .lastModifiedByUserId("test-lastModifiedByUserId")
            .effectiveFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .effectiveTo(Instant.parse("2025-01-15T10:00:00Z"))
            .status(ComplianceRuleResponseDto.RuleStatusDto.DRAFT)
            .approvalRequiredBy("test-approvalRequiredBy")
            .autoApproveThreshold(true)
            .priority(42)
            .tags(Collections.emptyList())
            .notes("test-notes")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-ruleId", dto.getRuleId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals(ComplianceRuleResponseDto.RuleTypeDto.AMOUNT_THRESHOLD, dto.getRuleType());
        assertEquals(ComplianceRuleResponseDto.RuleCategoryDto.EXPENSE_MANAGEMENT, dto.getCategory());
        assertEquals(ComplianceRuleResponseDto.SeverityLevelDto.INFO, dto.getSeverity());
        assertTrue(dto.getEnabled());
        assertEquals(BigDecimal.TEN, dto.getThresholdAmount());
        assertEquals("test-thresholdCurrency", dto.getThresholdCurrency());
        assertEquals("test-conditionExpression", dto.getConditionExpression());
        assertEquals("test-createdByUserId", dto.getCreatedByUserId());
        assertEquals("test-lastModifiedByUserId", dto.getLastModifiedByUserId());
        assertEquals(ComplianceRuleResponseDto.RuleStatusDto.DRAFT, dto.getStatus());
        assertEquals("test-approvalRequiredBy", dto.getApprovalRequiredBy());
        assertTrue(dto.getAutoApproveThreshold());
        assertEquals(42, dto.getPriority());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        ComplianceRuleResponseDto dto = new ComplianceRuleResponseDto();
        dto.setId("val-id");
        dto.setRuleId("val-ruleId");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setRuleType(ComplianceRuleResponseDto.RuleTypeDto.AMOUNT_THRESHOLD);
        dto.setCategory(ComplianceRuleResponseDto.RuleCategoryDto.EXPENSE_MANAGEMENT);
        dto.setSeverity(ComplianceRuleResponseDto.SeverityLevelDto.INFO);
        dto.setEnabled(true);
        dto.setThresholdAmount(BigDecimal.ONE);
        dto.setThresholdCurrency("val-thresholdCurrency");
        dto.setConditionExpression("val-conditionExpression");
        dto.setCreatedByUserId("val-createdByUserId");
        dto.setLastModifiedByUserId("val-lastModifiedByUserId");
        dto.setStatus(ComplianceRuleResponseDto.RuleStatusDto.DRAFT);
        dto.setApprovalRequiredBy("val-approvalRequiredBy");
        dto.setAutoApproveThreshold(true);
        dto.setPriority(99);
        dto.setNotes("val-notes");
        assertEquals("val-id", dto.getId());
        assertEquals("val-ruleId", dto.getRuleId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(ComplianceRuleResponseDto.RuleTypeDto.AMOUNT_THRESHOLD, dto.getRuleType());
        assertEquals(ComplianceRuleResponseDto.RuleCategoryDto.EXPENSE_MANAGEMENT, dto.getCategory());
        assertEquals(ComplianceRuleResponseDto.SeverityLevelDto.INFO, dto.getSeverity());
        assertTrue(dto.getEnabled());
        assertEquals(BigDecimal.ONE, dto.getThresholdAmount());
        assertEquals("val-thresholdCurrency", dto.getThresholdCurrency());
        assertEquals("val-conditionExpression", dto.getConditionExpression());
        assertEquals("val-createdByUserId", dto.getCreatedByUserId());
        assertEquals("val-lastModifiedByUserId", dto.getLastModifiedByUserId());
        assertEquals(ComplianceRuleResponseDto.RuleStatusDto.DRAFT, dto.getStatus());
        assertEquals("val-approvalRequiredBy", dto.getApprovalRequiredBy());
        assertTrue(dto.getAutoApproveThreshold());
        assertEquals(99, dto.getPriority());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceRuleResponseDto dto1 = ComplianceRuleResponseDto.builder()
                        .id("test-id")
            .ruleId("test-ruleId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .ruleType(ComplianceRuleResponseDto.RuleTypeDto.AMOUNT_THRESHOLD)
            .category(ComplianceRuleResponseDto.RuleCategoryDto.EXPENSE_MANAGEMENT)
            .severity(ComplianceRuleResponseDto.SeverityLevelDto.INFO)
            .enabled(true)
            .parameters(Collections.emptyMap())
            .thresholdAmount(BigDecimal.TEN)
            .thresholdCurrency("test-thresholdCurrency")
            .conditionExpression("test-conditionExpression")
            .applicableDepartments(Collections.emptyList())
            .applicableCostCenters(Collections.emptyList())
            .applicableExpenseCategories(Collections.emptyList())
            .createdByUserId("test-createdByUserId")
            .lastModifiedByUserId("test-lastModifiedByUserId")
            .effectiveFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .effectiveTo(Instant.parse("2025-01-15T10:00:00Z"))
            .status(ComplianceRuleResponseDto.RuleStatusDto.DRAFT)
            .approvalRequiredBy("test-approvalRequiredBy")
            .autoApproveThreshold(true)
            .priority(42)
            .tags(Collections.emptyList())
            .notes("test-notes")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ComplianceRuleResponseDto dto2 = ComplianceRuleResponseDto.builder()
                        .id("test-id")
            .ruleId("test-ruleId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .ruleType(ComplianceRuleResponseDto.RuleTypeDto.AMOUNT_THRESHOLD)
            .category(ComplianceRuleResponseDto.RuleCategoryDto.EXPENSE_MANAGEMENT)
            .severity(ComplianceRuleResponseDto.SeverityLevelDto.INFO)
            .enabled(true)
            .parameters(Collections.emptyMap())
            .thresholdAmount(BigDecimal.TEN)
            .thresholdCurrency("test-thresholdCurrency")
            .conditionExpression("test-conditionExpression")
            .applicableDepartments(Collections.emptyList())
            .applicableCostCenters(Collections.emptyList())
            .applicableExpenseCategories(Collections.emptyList())
            .createdByUserId("test-createdByUserId")
            .lastModifiedByUserId("test-lastModifiedByUserId")
            .effectiveFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .effectiveTo(Instant.parse("2025-01-15T10:00:00Z"))
            .status(ComplianceRuleResponseDto.RuleStatusDto.DRAFT)
            .approvalRequiredBy("test-approvalRequiredBy")
            .autoApproveThreshold(true)
            .priority(42)
            .tags(Collections.emptyList())
            .notes("test-notes")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ComplianceRuleResponseDto dto = ComplianceRuleResponseDto.builder()
                        .id("test-id")
            .ruleId("test-ruleId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .ruleType(ComplianceRuleResponseDto.RuleTypeDto.AMOUNT_THRESHOLD)
            .category(ComplianceRuleResponseDto.RuleCategoryDto.EXPENSE_MANAGEMENT)
            .severity(ComplianceRuleResponseDto.SeverityLevelDto.INFO)
            .enabled(true)
            .parameters(Collections.emptyMap())
            .thresholdAmount(BigDecimal.TEN)
            .thresholdCurrency("test-thresholdCurrency")
            .conditionExpression("test-conditionExpression")
            .applicableDepartments(Collections.emptyList())
            .applicableCostCenters(Collections.emptyList())
            .applicableExpenseCategories(Collections.emptyList())
            .createdByUserId("test-createdByUserId")
            .lastModifiedByUserId("test-lastModifiedByUserId")
            .effectiveFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .effectiveTo(Instant.parse("2025-01-15T10:00:00Z"))
            .status(ComplianceRuleResponseDto.RuleStatusDto.DRAFT)
            .approvalRequiredBy("test-approvalRequiredBy")
            .autoApproveThreshold(true)
            .priority(42)
            .tags(Collections.emptyList())
            .notes("test-notes")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}