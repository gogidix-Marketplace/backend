package com.gogidix.finance.compliance.application.dto.response;

import com.gogidix.finance.compliance.application.dto.response.ComplianceCheckResponseDto;
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
class ComplianceCheckResponseDtoTest {

        @Test
    void testBuilder() {
        ComplianceCheckResponseDto dto = ComplianceCheckResponseDto.builder()
                        .id("test-id")
            .checkId("test-checkId")
            .tenantId("test-tenantId")
            .ruleId("test-ruleId")
            .ruleName("test-ruleName")
            .entityType("test-entityType")
            .entityId("test-entityId")
            .referenceNumber("test-referenceNumber")
            .status(ComplianceCheckResponseDto.CheckStatusDto.PENDING)
            .result(ComplianceCheckResponseDto.CheckResultDto.COMPLIANT)
            .severity(ComplianceCheckResponseDto.SeverityLevelDto.INFO)
            .violationDescription("test-violationDescription")
            .evaluatedContext(Collections.emptyMap())
            .evaluatedAmount(BigDecimal.TEN)
            .evaluatedCurrency("test-evaluatedCurrency")
            .thresholdAmount(BigDecimal.TEN)
            .thresholdCurrency("test-thresholdCurrency")
            .variance(BigDecimal.TEN)
            .department("test-department")
            .costCenter("test-costCenter")
            .expenseCategory("test-expenseCategory")
            .evaluatedBy("test-evaluatedBy")
            .evaluatedByUserId("test-evaluatedByUserId")
            .evaluatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvalNotes("test-approvalNotes")
            .waived(true)
            .waivedBy("test-waivedBy")
            .waivedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .waiverReason("test-waiverReason")
            .remediationRequired(true)
            .remediationAction("test-remediationAction")
            .remediationAssignedTo("test-remediationAssignedTo")
            .remediationDueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .remediationCompletedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .tags(Collections.emptyList())
            .notes("test-notes")
            .correlationId("test-correlationId")
            .violationDetails(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-checkId", dto.getCheckId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-ruleId", dto.getRuleId());
        assertEquals("test-ruleName", dto.getRuleName());
        assertEquals("test-entityType", dto.getEntityType());
        assertEquals("test-entityId", dto.getEntityId());
        assertEquals("test-referenceNumber", dto.getReferenceNumber());
        assertEquals(ComplianceCheckResponseDto.CheckStatusDto.PENDING, dto.getStatus());
        assertEquals(ComplianceCheckResponseDto.CheckResultDto.COMPLIANT, dto.getResult());
        assertEquals(ComplianceCheckResponseDto.SeverityLevelDto.INFO, dto.getSeverity());
        assertEquals("test-violationDescription", dto.getViolationDescription());
        assertEquals(BigDecimal.TEN, dto.getEvaluatedAmount());
        assertEquals("test-evaluatedCurrency", dto.getEvaluatedCurrency());
        assertEquals(BigDecimal.TEN, dto.getThresholdAmount());
        assertEquals("test-thresholdCurrency", dto.getThresholdCurrency());
        assertEquals(BigDecimal.TEN, dto.getVariance());
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-costCenter", dto.getCostCenter());
        assertEquals("test-expenseCategory", dto.getExpenseCategory());
        assertEquals("test-evaluatedBy", dto.getEvaluatedBy());
        assertEquals("test-evaluatedByUserId", dto.getEvaluatedByUserId());
        assertEquals("test-approvedBy", dto.getApprovedBy());
        assertEquals("test-approvalNotes", dto.getApprovalNotes());
        assertTrue(dto.getWaived());
        assertEquals("test-waivedBy", dto.getWaivedBy());
        assertEquals("test-waiverReason", dto.getWaiverReason());
        assertTrue(dto.getRemediationRequired());
        assertEquals("test-remediationAction", dto.getRemediationAction());
        assertEquals("test-remediationAssignedTo", dto.getRemediationAssignedTo());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-correlationId", dto.getCorrelationId());
    }

    @Test
    void testSettersAndGetters() {
        ComplianceCheckResponseDto dto = new ComplianceCheckResponseDto();
        dto.setId("val-id");
        dto.setCheckId("val-checkId");
        dto.setTenantId("val-tenantId");
        dto.setRuleId("val-ruleId");
        dto.setRuleName("val-ruleName");
        dto.setEntityType("val-entityType");
        dto.setEntityId("val-entityId");
        dto.setReferenceNumber("val-referenceNumber");
        dto.setStatus(ComplianceCheckResponseDto.CheckStatusDto.PENDING);
        dto.setResult(ComplianceCheckResponseDto.CheckResultDto.COMPLIANT);
        dto.setSeverity(ComplianceCheckResponseDto.SeverityLevelDto.INFO);
        dto.setViolationDescription("val-violationDescription");
        dto.setEvaluatedAmount(BigDecimal.ONE);
        dto.setEvaluatedCurrency("val-evaluatedCurrency");
        dto.setThresholdAmount(BigDecimal.ONE);
        dto.setThresholdCurrency("val-thresholdCurrency");
        dto.setVariance(BigDecimal.ONE);
        dto.setDepartment("val-department");
        dto.setCostCenter("val-costCenter");
        dto.setExpenseCategory("val-expenseCategory");
        dto.setEvaluatedBy("val-evaluatedBy");
        dto.setEvaluatedByUserId("val-evaluatedByUserId");
        dto.setApprovedBy("val-approvedBy");
        dto.setApprovalNotes("val-approvalNotes");
        dto.setWaived(true);
        dto.setWaivedBy("val-waivedBy");
        dto.setWaiverReason("val-waiverReason");
        dto.setRemediationRequired(true);
        dto.setRemediationAction("val-remediationAction");
        dto.setRemediationAssignedTo("val-remediationAssignedTo");
        dto.setNotes("val-notes");
        dto.setCorrelationId("val-correlationId");
        assertEquals("val-id", dto.getId());
        assertEquals("val-checkId", dto.getCheckId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-ruleId", dto.getRuleId());
        assertEquals("val-ruleName", dto.getRuleName());
        assertEquals("val-entityType", dto.getEntityType());
        assertEquals("val-entityId", dto.getEntityId());
        assertEquals("val-referenceNumber", dto.getReferenceNumber());
        assertEquals(ComplianceCheckResponseDto.CheckStatusDto.PENDING, dto.getStatus());
        assertEquals(ComplianceCheckResponseDto.CheckResultDto.COMPLIANT, dto.getResult());
        assertEquals(ComplianceCheckResponseDto.SeverityLevelDto.INFO, dto.getSeverity());
        assertEquals("val-violationDescription", dto.getViolationDescription());
        assertEquals(BigDecimal.ONE, dto.getEvaluatedAmount());
        assertEquals("val-evaluatedCurrency", dto.getEvaluatedCurrency());
        assertEquals(BigDecimal.ONE, dto.getThresholdAmount());
        assertEquals("val-thresholdCurrency", dto.getThresholdCurrency());
        assertEquals(BigDecimal.ONE, dto.getVariance());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-expenseCategory", dto.getExpenseCategory());
        assertEquals("val-evaluatedBy", dto.getEvaluatedBy());
        assertEquals("val-evaluatedByUserId", dto.getEvaluatedByUserId());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals("val-approvalNotes", dto.getApprovalNotes());
        assertTrue(dto.getWaived());
        assertEquals("val-waivedBy", dto.getWaivedBy());
        assertEquals("val-waiverReason", dto.getWaiverReason());
        assertTrue(dto.getRemediationRequired());
        assertEquals("val-remediationAction", dto.getRemediationAction());
        assertEquals("val-remediationAssignedTo", dto.getRemediationAssignedTo());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-correlationId", dto.getCorrelationId());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceCheckResponseDto dto1 = ComplianceCheckResponseDto.builder()
                        .id("test-id")
            .checkId("test-checkId")
            .tenantId("test-tenantId")
            .ruleId("test-ruleId")
            .ruleName("test-ruleName")
            .entityType("test-entityType")
            .entityId("test-entityId")
            .referenceNumber("test-referenceNumber")
            .status(ComplianceCheckResponseDto.CheckStatusDto.PENDING)
            .result(ComplianceCheckResponseDto.CheckResultDto.COMPLIANT)
            .severity(ComplianceCheckResponseDto.SeverityLevelDto.INFO)
            .violationDescription("test-violationDescription")
            .evaluatedContext(Collections.emptyMap())
            .evaluatedAmount(BigDecimal.TEN)
            .evaluatedCurrency("test-evaluatedCurrency")
            .thresholdAmount(BigDecimal.TEN)
            .thresholdCurrency("test-thresholdCurrency")
            .variance(BigDecimal.TEN)
            .department("test-department")
            .costCenter("test-costCenter")
            .expenseCategory("test-expenseCategory")
            .evaluatedBy("test-evaluatedBy")
            .evaluatedByUserId("test-evaluatedByUserId")
            .evaluatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvalNotes("test-approvalNotes")
            .waived(true)
            .waivedBy("test-waivedBy")
            .waivedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .waiverReason("test-waiverReason")
            .remediationRequired(true)
            .remediationAction("test-remediationAction")
            .remediationAssignedTo("test-remediationAssignedTo")
            .remediationDueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .remediationCompletedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .tags(Collections.emptyList())
            .notes("test-notes")
            .correlationId("test-correlationId")
            .violationDetails(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ComplianceCheckResponseDto dto2 = ComplianceCheckResponseDto.builder()
                        .id("test-id")
            .checkId("test-checkId")
            .tenantId("test-tenantId")
            .ruleId("test-ruleId")
            .ruleName("test-ruleName")
            .entityType("test-entityType")
            .entityId("test-entityId")
            .referenceNumber("test-referenceNumber")
            .status(ComplianceCheckResponseDto.CheckStatusDto.PENDING)
            .result(ComplianceCheckResponseDto.CheckResultDto.COMPLIANT)
            .severity(ComplianceCheckResponseDto.SeverityLevelDto.INFO)
            .violationDescription("test-violationDescription")
            .evaluatedContext(Collections.emptyMap())
            .evaluatedAmount(BigDecimal.TEN)
            .evaluatedCurrency("test-evaluatedCurrency")
            .thresholdAmount(BigDecimal.TEN)
            .thresholdCurrency("test-thresholdCurrency")
            .variance(BigDecimal.TEN)
            .department("test-department")
            .costCenter("test-costCenter")
            .expenseCategory("test-expenseCategory")
            .evaluatedBy("test-evaluatedBy")
            .evaluatedByUserId("test-evaluatedByUserId")
            .evaluatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvalNotes("test-approvalNotes")
            .waived(true)
            .waivedBy("test-waivedBy")
            .waivedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .waiverReason("test-waiverReason")
            .remediationRequired(true)
            .remediationAction("test-remediationAction")
            .remediationAssignedTo("test-remediationAssignedTo")
            .remediationDueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .remediationCompletedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .tags(Collections.emptyList())
            .notes("test-notes")
            .correlationId("test-correlationId")
            .violationDetails(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ComplianceCheckResponseDto dto = ComplianceCheckResponseDto.builder()
                        .id("test-id")
            .checkId("test-checkId")
            .tenantId("test-tenantId")
            .ruleId("test-ruleId")
            .ruleName("test-ruleName")
            .entityType("test-entityType")
            .entityId("test-entityId")
            .referenceNumber("test-referenceNumber")
            .status(ComplianceCheckResponseDto.CheckStatusDto.PENDING)
            .result(ComplianceCheckResponseDto.CheckResultDto.COMPLIANT)
            .severity(ComplianceCheckResponseDto.SeverityLevelDto.INFO)
            .violationDescription("test-violationDescription")
            .evaluatedContext(Collections.emptyMap())
            .evaluatedAmount(BigDecimal.TEN)
            .evaluatedCurrency("test-evaluatedCurrency")
            .thresholdAmount(BigDecimal.TEN)
            .thresholdCurrency("test-thresholdCurrency")
            .variance(BigDecimal.TEN)
            .department("test-department")
            .costCenter("test-costCenter")
            .expenseCategory("test-expenseCategory")
            .evaluatedBy("test-evaluatedBy")
            .evaluatedByUserId("test-evaluatedByUserId")
            .evaluatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvalNotes("test-approvalNotes")
            .waived(true)
            .waivedBy("test-waivedBy")
            .waivedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .waiverReason("test-waiverReason")
            .remediationRequired(true)
            .remediationAction("test-remediationAction")
            .remediationAssignedTo("test-remediationAssignedTo")
            .remediationDueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .remediationCompletedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .tags(Collections.emptyList())
            .notes("test-notes")
            .correlationId("test-correlationId")
            .violationDetails(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}