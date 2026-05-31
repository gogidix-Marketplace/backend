package com.gogidix.finance.compliance.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Compliance Check Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComplianceCheckResponseDto {

    private String id;

    private String checkId;

    private String tenantId;

    private String ruleId;

    private String ruleName;

    private String entityType;

    private String entityId;

    private String referenceNumber;

    private CheckStatusDto status;

    private CheckResultDto result;

    private SeverityLevelDto severity;

    private String violationDescription;

    private Map<String, Object> evaluatedContext;

    private BigDecimal evaluatedAmount;

    private String evaluatedCurrency;

    private BigDecimal thresholdAmount;

    private String thresholdCurrency;

    private BigDecimal variance;

    private String department;

    private String costCenter;

    private String expenseCategory;

    private String evaluatedBy;

    private String evaluatedByUserId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant evaluatedAt;

    private String approvedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant approvedAt;

    private String approvalNotes;

    private Boolean waived;

    private String waivedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant waivedAt;

    private String waiverReason;

    private Boolean remediationRequired;

    private String remediationAction;

    private String remediationAssignedTo;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant remediationDueDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant remediationCompletedAt;

    private List<String> tags;

    private String notes;

    private String correlationId;

    private List<ViolationDetailDto> violationDetails;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum CheckStatusDto {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        CANCELLED
    }

    public enum CheckResultDto {
        COMPLIANT,
        NON_COMPLIANT,
        WARNING,
        NOT_APPLICABLE,
        ERROR
    }

    public enum SeverityLevelDto {
        INFO,
        WARNING,
        ERROR,
        CRITICAL
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ViolationDetailDto {
        private String field;
        private String expected;
        private String actual;
        private String message;
    }
}
