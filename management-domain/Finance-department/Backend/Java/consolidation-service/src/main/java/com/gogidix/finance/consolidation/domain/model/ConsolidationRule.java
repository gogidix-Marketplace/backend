package com.gogidix.finance.consolidation.domain.model;

import com.gogidix.finance.consolidation.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Consolidation Rule Domain Entity
 * Defines rules for financial consolidation across subsidiaries and departments
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "consolidation_rules")
public class ConsolidationRule extends BaseEntity {

    @Indexed
    private String ruleId;

    @Indexed
    private String tenantId;

    private String ruleName;

    private String description;

    private RuleType ruleType;

    private RuleScope ruleScope;

    private List<String> applicableDepartments;

    private List<String> applicableCostCenters;

    private List<String> applicableSubsidiaries;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private Boolean active;

    private Priority priority;

    private ConsolidationMethod consolidationMethod;

    private String currencyCode;

    private CurrencyConversionMethod conversionMethod;

    private BigDecimal exchangeRate;

    private String exchangeRateSource;

    private IntercompanyElimination eliminationRule;

    private List<AdjustmentRule> adjustmentRules;

    private String createdBy;

    private String approvedBy;

    private LocalDate approvedAt;

    private String version;

    @Builder.Default
    private List<ConsolidationRuleSnapshot> snapshots = new ArrayList<>();

    public enum RuleType {
        FINANCIAL_STATEMENT,
        BALANCE_SHEET,
        INCOME_STATEMENT,
        CASH_FLOW,
        INTERCOMPANY,
        TAX_CONSOLIDATION,
        REGULATORY_REPORTING,
        CUSTOM
    }

    public enum RuleScope {
        GLOBAL,
        REGIONAL,
        DEPARTMENTAL,
        COST_CENTER,
        SUBSIDIARY,
        PROJECT
    }

    public enum Priority {
        CRITICAL,
        HIGH,
        MEDIUM,
        LOW
    }

    public enum ConsolidationMethod {
        FULL_CONSOLIDATION,
        PROPORTIONAL_CONSOLIDATION,
        EQUITY_METHOD,
        LINE_BY_LINE,
        AGGREGATION
    }

    public enum CurrencyConversionMethod {
        CLOSING_RATE,
        AVERAGE_RATE,
        HISTORICAL_RATE,
        TEMPORAL_METHOD,
        CURRENT_RATE
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IntercompanyElimination {
        private Boolean enabled;
        private EliminationMethod method;
        private Boolean eliminateReciprocalAccounts;
        private Boolean eliminateUnrealizedProfits;
        private Boolean eliminateDownstreamTransactions;
        private Boolean eliminateUpstreamTransactions;
        private String matchingCriteria;
    }

    public enum EliminationMethod {
        GROSS_UP,
        NETTING,
        PAID_IN_CAPITAL,
        SHAREHOLDER_EQUITY
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdjustmentRule {
        private String adjustmentId;
        private String accountCode;
        private AdjustmentType type;
        private BigDecimal percentage;
        private String formula;
        private String description;
    }

    public enum AdjustmentType {
        PERCENTAGE,
        FIXED_AMOUNT,
        FORMULA_BASED,
        PRORATED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsolidationRuleSnapshot {
        private String snapshotId;
        private Instant capturedAt;
        private String capturedBy;
        private String version;
        private String data;
    }

    /**
     * Creates a new consolidation rule
     */
    public static ConsolidationRule create(String tenantId, String ruleName,
                                           RuleType ruleType, RuleScope ruleScope,
                                           ConsolidationMethod method, String createdBy) {
        ConsolidationRule rule = ConsolidationRule.builder()
            .ruleId(generateRuleId())
            .tenantId(tenantId)
            .ruleName(ruleName)
            .ruleType(ruleType)
            .ruleScope(ruleScope)
            .consolidationMethod(method)
            .active(true)
            .priority(Priority.MEDIUM)
            .createdBy(createdBy)
            .version("1.0")
            .applicableDepartments(new ArrayList<>())
            .applicableCostCenters(new ArrayList<>())
            .applicableSubsidiaries(new ArrayList<>())
            .adjustmentRules(new ArrayList<>())
            .snapshots(new ArrayList<>())
            .build();

        return rule;
    }

    /**
     * Activates the rule
     */
    public void activate() {
        this.active = true;
    }

    /**
     * Deactivates the rule
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * Checks if rule is currently effective
     */
    public boolean isEffectiveOn(LocalDate date) {
        if (!active) {
            return false;
        }
        if (effectiveFrom != null && date.isBefore(effectiveFrom)) {
            return false;
        }
        if (effectiveTo != null && date.isAfter(effectiveTo)) {
            return false;
        }
        return true;
    }

    /**
     * Adds a department to applicable departments
     */
    public void addDepartment(String departmentId) {
        if (this.applicableDepartments == null) {
            this.applicableDepartments = new ArrayList<>();
        }
        if (!this.applicableDepartments.contains(departmentId)) {
            this.applicableDepartments.add(departmentId);
        }
    }

    /**
     * Removes a department from applicable departments
     */
    public void removeDepartment(String departmentId) {
        if (this.applicableDepartments != null) {
            this.applicableDepartments.remove(departmentId);
        }
    }

    /**
     * Adds a cost center to applicable cost centers
     */
    public void addCostCenter(String costCenterId) {
        if (this.applicableCostCenters == null) {
            this.applicableCostCenters = new ArrayList<>();
        }
        if (!this.applicableCostCenters.contains(costCenterId)) {
            this.applicableCostCenters.add(costCenterId);
        }
    }

    /**
     * Adds an adjustment rule
     */
    public void addAdjustmentRule(AdjustmentRule rule) {
        if (this.adjustmentRules == null) {
            this.adjustmentRules = new ArrayList<>();
        }
        this.adjustmentRules.add(rule);
    }

    /**
     * Updates the exchange rate
     */
    public void updateExchangeRate(BigDecimal rate, String source) {
        this.exchangeRate = rate;
        this.exchangeRateSource = source;
    }

    /**
     * Approves the rule
     */
    public void approve(String approver) {
        this.approvedBy = approver;
        this.approvedAt = LocalDate.now();
    }

    /**
     * Creates a snapshot of the current rule state
     */
    public void createSnapshot(String capturedBy) {
        ConsolidationRuleSnapshot snapshot = ConsolidationRuleSnapshot.builder()
            .snapshotId(java.util.UUID.randomUUID().toString())
            .capturedAt(Instant.now())
            .capturedBy(capturedBy)
            .version(this.version)
            .build();

        if (this.snapshots == null) {
            this.snapshots = new ArrayList<>();
        }
        this.snapshots.add(snapshot);
    }

    /**
     * Increments the version
     */
    public void incrementVersion() {
        if (this.version != null) {
            String[] parts = this.version.split("\\.");
            if (parts.length == 2) {
                try {
                    int major = Integer.parseInt(parts[0]);
                    int minor = Integer.parseInt(parts[1]);
                    this.version = major + "." + (minor + 1);
                } catch (NumberFormatException e) {
                    this.version = "1.0";
                }
            }
        } else {
            this.version = "1.0";
        }
    }

    private static String generateRuleId() {
        return "CRULE-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
