package com.gogidix.finance.compliance.domain.model;

import com.gogidix.finance.compliance.domain.event.ComplianceViolationEvent;
import com.gogidix.finance.compliance.shared.base.BaseEntity;
import com.gogidix.finance.compliance.shared.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Compliance Rule Domain Entity
 * Multi-tenant compliance rules for financial operations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "compliance_rules")
public class ComplianceRule extends BaseEntity {

    private String ruleId;

    private String tenantId;

    private String name;

    private String description;

    private RuleType ruleType;

    private RuleCategory category;

    private SeverityLevel severity;

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

    private Instant effectiveFrom;

    private Instant effectiveTo;

    private RuleStatus status;

    private String approvalRequiredBy;

    private Boolean autoApproveThreshold;

    private Integer priority;

    private List<String> tags;

    private String notes;

    @Builder.Default
    private List<ComplianceViolationEvent> domainEvents = new ArrayList<>();

    public enum RuleType {
        AMOUNT_THRESHOLD,
        CATEGORY_RESTRICTION,
        APPROVAL_CHAIN,
        DOCUMENT_REQUIRED,
        TIME_LIMIT,
        FREQUENCY_LIMIT,
        VENDOR_RESTRICTION,
        CUSTOM_EXPRESSION
    }

    public enum RuleCategory {
        EXPENSE_MANAGEMENT,
        PURCHASE_ORDER,
        REIMBURSEMENT,
        TRAVEL_POLICY,
        PROCUREMENT,
        FINANCIAL_REPORTING,
        AUDIT_COMPLIANCE,
        REGULATORY
    }

    public enum SeverityLevel {
        INFO,
        WARNING,
        ERROR,
        CRITICAL
    }

    public enum RuleStatus {
        DRAFT,
        ACTIVE,
        INACTIVE,
        DEPRECATED,
        UNDER_REVIEW
    }

    /**
     * Creates a new compliance rule
     */
    public static ComplianceRule create(String tenantId, String name, String description,
                                         RuleType ruleType, RuleCategory category,
                                         SeverityLevel severity, String createdByUserId) {
        ComplianceRule rule = ComplianceRule.builder()
            .tenantId(tenantId)
            .name(name)
            .description(description)
            .ruleType(ruleType)
            .category(category)
            .severity(severity)
            .status(RuleStatus.DRAFT)
            .enabled(false)
            .createdByUserId(createdByUserId)
            .lastModifiedByUserId(createdByUserId)
            .tags(new ArrayList<>())
            .applicableDepartments(new ArrayList<>())
            .applicableCostCenters(new ArrayList<>())
            .applicableExpenseCategories(new ArrayList<>())
            .build();

        return rule;
    }

    /**
     * Activates the compliance rule
     */
    public void activate(String modifiedByUserId) {
        if (this.status != RuleStatus.DRAFT && this.status != RuleStatus.INACTIVE) {
            throw new IllegalStateException("Can only activate draft or inactive rules");
        }

        validateForActivation();

        this.status = RuleStatus.ACTIVE;
        this.enabled = true;
        this.lastModifiedByUserId = modifiedByUserId;
        this.effectiveFrom = Instant.now();

        addDomainEvent(ComplianceViolationEvent.builder()
            .ruleId(this.ruleId)
            .tenantId(this.tenantId)
            .eventType("RULE_ACTIVATED")
            .severity(this.severity.name())
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Deactivates the compliance rule
     */
    public void deactivate(String modifiedByUserId) {
        if (this.status != RuleStatus.ACTIVE) {
            throw new IllegalStateException("Can only deactivate active rules");
        }

        this.status = RuleStatus.INACTIVE;
        this.enabled = false;
        this.lastModifiedByUserId = modifiedByUserId;
        this.effectiveTo = Instant.now();

        addDomainEvent(ComplianceViolationEvent.builder()
            .ruleId(this.ruleId)
            .tenantId(this.tenantId)
            .eventType("RULE_DEACTIVATED")
            .severity(this.severity.name())
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Updates rule parameters
     */
    public void updateParameters(Map<String, Object> parameters, String modifiedByUserId) {
        if (this.status == RuleStatus.ACTIVE) {
            throw new IllegalStateException("Cannot modify parameters of active rule. Deactivate first.");
        }

        this.parameters = parameters;
        this.lastModifiedByUserId = modifiedByUserId;
    }

    /**
     * Sets threshold amount
     */
    public void setThreshold(BigDecimal amount, String currency, String modifiedByUserId) {
        if (this.status == RuleStatus.ACTIVE) {
            throw new IllegalStateException("Cannot modify threshold of active rule");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("thresholdAmount", "Threshold amount must be positive");
        }

        this.thresholdAmount = amount;
        this.thresholdCurrency = currency;
        this.lastModifiedByUserId = modifiedByUserId;
    }

    /**
     * Adds an applicable department
     */
    public void addApplicableDepartment(String department) {
        if (this.applicableDepartments == null) {
            this.applicableDepartments = new ArrayList<>();
        }
        if (!this.applicableDepartments.contains(department)) {
            this.applicableDepartments.add(department);
        }
    }

    /**
     * Removes an applicable department
     */
    public void removeApplicableDepartment(String department) {
        if (this.applicableDepartments != null) {
            this.applicableDepartments.remove(department);
        }
    }

    /**
     * Adds an applicable expense category
     */
    public void addApplicableExpenseCategory(String category) {
        if (this.applicableExpenseCategories == null) {
            this.applicableExpenseCategories = new ArrayList<>();
        }
        if (!this.applicableExpenseCategories.contains(category)) {
            this.applicableExpenseCategories.add(category);
        }
    }

    /**
     * Checks if rule is currently effective
     */
    public boolean isCurrentlyEffective() {
        Instant now = Instant.now();

        if (status != RuleStatus.ACTIVE || !enabled) {
            return false;
        }

        if (effectiveFrom != null && now.isBefore(effectiveFrom)) {
            return false;
        }

        if (effectiveTo != null && now.isAfter(effectiveTo)) {
            return false;
        }

        return true;
    }

    /**
     * Checks if rule applies to given context
     */
    public boolean appliesToContext(String department, String costCenter, String expenseCategory) {
        if (!isCurrentlyEffective()) {
            return false;
        }

        boolean departmentMatch = applicableDepartments == null || applicableDepartments.isEmpty() ||
                                 applicableDepartments.contains(department);

        boolean costCenterMatch = applicableCostCenters == null || applicableCostCenters.isEmpty() ||
                                 applicableCostCenters.contains(costCenter);

        boolean categoryMatch = applicableExpenseCategories == null || applicableExpenseCategories.isEmpty() ||
                               applicableExpenseCategories.contains(expenseCategory);

        return departmentMatch && costCenterMatch && categoryMatch;
    }

    /**
     * Adds a tag to the rule
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Removes a tag from the rule
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    /**
     * Sets priority for rule evaluation order
     */
    public void setPriority(Integer priority) {
        if (priority == null || priority < 1 || priority > 100) {
            throw new ValidationException("priority", "Priority must be between 1 and 100");
        }
        this.priority = priority;
    }

    private void validateForActivation() {
        if (name == null || name.isBlank()) {
            throw new ValidationException("name", "Rule name is required");
        }
        if (ruleType == null) {
            throw new ValidationException("ruleType", "Rule type is required");
        }
        if (category == null) {
            throw new ValidationException("category", "Rule category is required");
        }
        if (severity == null) {
            throw new ValidationException("severity", "Severity level is required");
        }
        if (ruleType == RuleType.AMOUNT_THRESHOLD && thresholdAmount == null) {
            throw new ValidationException("thresholdAmount", "Threshold amount is required for amount threshold rules");
        }
    }

    public void addDomainEvent(ComplianceViolationEvent event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}
