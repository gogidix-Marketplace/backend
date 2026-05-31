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
 * Compliance Rule Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComplianceRuleResponseDto {

    private String id;

    private String ruleId;

    private String tenantId;

    private String name;

    private String description;

    private RuleTypeDto ruleType;

    private RuleCategoryDto category;

    private SeverityLevelDto severity;

    private Boolean enabled;

    private Map<String, Object> parameters;

    private BigDecimal thresholdAmount;

    private String thresholdCurrency;

    private String conditionExpression;

    private List<String> applicableDepartments;

    private List<String> applicableCostCenters;

    private List<String> applicableExpenseCategories;

    private String createdByUserId;

    private String lastModifiedByUserId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant effectiveFrom;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant effectiveTo;

    private RuleStatusDto status;

    private String approvalRequiredBy;

    private Boolean autoApproveThreshold;

    private Integer priority;

    private List<String> tags;

    private String notes;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum RuleTypeDto {
        AMOUNT_THRESHOLD,
        CATEGORY_RESTRICTION,
        APPROVAL_CHAIN,
        DOCUMENT_REQUIRED,
        TIME_LIMIT,
        FREQUENCY_LIMIT,
        VENDOR_RESTRICTION,
        CUSTOM_EXPRESSION
    }

    public enum RuleCategoryDto {
        EXPENSE_MANAGEMENT,
        PURCHASE_ORDER,
        REIMBURSEMENT,
        TRAVEL_POLICY,
        PROCUREMENT,
        FINANCIAL_REPORTING,
        AUDIT_COMPLIANCE,
        REGULATORY
    }

    public enum SeverityLevelDto {
        INFO,
        WARNING,
        ERROR,
        CRITICAL
    }

    public enum RuleStatusDto {
        DRAFT,
        ACTIVE,
        INACTIVE,
        DEPRECATED,
        UNDER_REVIEW
    }
}
