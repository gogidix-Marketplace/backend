package com.gogidix.finance.tax.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tax Rule Domain Entity
 * Multi-tenant tax rules for determining tax applicability
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "tax_rules")
@CompoundIndex(def = "{'tenantId': 1, 'jurisdiction': 1, 'ruleType': 1}", name = "tenant_jurisdiction_rule_idx")
public class TaxRule extends BaseEntity {

    @Id
    private String id;

    @Field("tenant_id")
    @Indexed
    private String tenantId;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    @Indexed(unique = true)
    @Field("rule_id")
    private String ruleId;

    @Field("rule_name")
    @Indexed
    private String ruleName;

    @Field("rule_type")
    @Indexed
    private RuleType ruleType;

    @Field("jurisdiction")
    @Indexed
    private TaxRate.Jurisdiction jurisdiction;

    @Field("tax_type")
    @Indexed
    private TaxRate.TaxType taxType;

    @Field("description")
    private String description;

    @Field("conditions")
    @Builder.Default
    private List<RuleCondition> conditions = new ArrayList<>();

    @Field("actions")
    @Builder.Default
    private List<RuleAction> actions = new ArrayList<>();

    @Field("priority")
    private Integer priority;

    @Field("effective_date")
    private LocalDate effectiveDate;

    @Field("expiry_date")
    private LocalDate expiryDate;

    @Field("is_active")
    private Boolean isActive;

    @Field("applies_to")
    @Builder.Default
    private List<String> appliesTo = new ArrayList<>();

    @Field("exemptions")
    @Builder.Default
    private List<String> exemptions = new ArrayList<>();

    @Field("thresholds")
    @Builder.Default
    private Map<String, BigDecimal> thresholds = new HashMap<>();

    @Field("status")
    private TaxRuleStatus status;

    @Field("created_by")
    private String createdBy;

    @Field("version")
    private Integer version;

    @Field("metadata")
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    /**
     * Rule Type enum
     */
    public enum RuleType {
        THRESHOLD_BASED,
        CATEGORY_BASED,
        LOCATION_BASED,
        TIME_BASED,
        ENTITY_BASED,
        TRANSACTION_BASED,
        CUSTOM
    }

    /**
     * Tax Rule Status enum
     */
    public enum TaxRuleStatus {
        DRAFT,
        ACTIVE,
        INACTIVE,
        DEPRECATED,
        ARCHIVED
    }

    /**
     * Rule Condition
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RuleCondition {
        private String field;
        private String operator;
        private String value;
        private String valueType;
        private Boolean isRequired;
    }

    /**
     * Rule Action
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RuleAction {
        private String actionType;
        private String target;
        private String value;
        private Map<String, Object> parameters;
    }

    /**
     * Creates a new tax rule
     */
    public static TaxRule create(String tenantId, String ruleName, RuleType ruleType,
                                  TaxRate.Jurisdiction jurisdiction, TaxRate.TaxType taxType,
                                  String createdBy) {
        TaxRule taxRule = TaxRule.builder()
            .tenantId(tenantId)
            .ruleId(generateRuleId())
            .ruleName(ruleName)
            .ruleType(ruleType)
            .jurisdiction(jurisdiction)
            .taxType(taxType)
            .status(TaxRuleStatus.DRAFT)
            .isActive(false)
            .priority(0)
            .createdBy(createdBy)
            .version(1)
            .conditions(new ArrayList<>())
            .actions(new ArrayList<>())
            .appliesTo(new ArrayList<>())
            .exemptions(new ArrayList<>())
            .thresholds(new HashMap<>())
            .metadata(new HashMap<>())
            .build();

        taxRule.validate();
        return taxRule;
    }

    /**
     * Activates the tax rule
     */
    public void activate() {
        if (this.conditions == null || this.conditions.isEmpty()) {
            throw new IllegalStateException("Cannot activate rule without conditions");
        }

        if (this.actions == null || this.actions.isEmpty()) {
            throw new IllegalStateException("Cannot activate rule without actions");
        }

        this.status = TaxRuleStatus.ACTIVE;
        this.isActive = true;
        this.effectiveDate = LocalDate.now();
        this.updateTimestamp();
    }

    /**
     * Deactivates the tax rule
     */
    public void deactivate() {
        if (this.status != TaxRuleStatus.ACTIVE) {
            throw new IllegalStateException("Can only deactivate active rules");
        }

        this.status = TaxRuleStatus.INACTIVE;
        this.isActive = false;
        this.updateTimestamp();
    }

    /**
     * Adds a condition to the rule
     */
    public void addCondition(RuleCondition condition) {
        if (this.status == TaxRuleStatus.ACTIVE) {
            throw new IllegalStateException("Cannot modify active rules");
        }

        if (this.conditions == null) {
            this.conditions = new ArrayList<>();
        }

        this.conditions.add(condition);
        this.updateTimestamp();
    }

    /**
     * Adds an action to the rule
     */
    public void addAction(RuleAction action) {
        if (this.status == TaxRuleStatus.ACTIVE) {
            throw new IllegalStateException("Cannot modify active rules");
        }

        if (this.actions == null) {
            this.actions = new ArrayList<>();
        }

        this.actions.add(action);
        this.updateTimestamp();
    }

    /**
     * Adds an exemption
     */
    public void addExemption(String exemption) {
        if (this.exemptions == null) {
            this.exemptions = new ArrayList<>();
        }

        if (!this.exemptions.contains(exemption)) {
            this.exemptions.add(exemption);
            this.updateTimestamp();
        }
    }

    /**
     * Removes an exemption
     */
    public void removeExemption(String exemption) {
        if (this.exemptions != null) {
            this.exemptions.remove(exemption);
            this.updateTimestamp();
        }
    }

    /**
     * Sets a threshold
     */
    public void setThreshold(String name, BigDecimal value) {
        if (this.thresholds == null) {
            this.thresholds = new HashMap<>();
        }

        this.thresholds.put(name, value);
        this.updateTimestamp();
    }

    /**
     * Checks if the rule applies to a given category
     */
    public boolean appliesToCategory(String category) {
        if (this.appliesTo == null || this.appliesTo.isEmpty()) {
            return true; // No restrictions means applies to all
        }

        return this.appliesTo.contains(category);
    }

    /**
     * Checks if a category is exempt
     */
    public boolean isExempt(String category) {
        return this.exemptions != null && this.exemptions.contains(category);
    }

    /**
     * Checks if the rule is effective on a given date
     */
    public boolean isEffectiveOn(LocalDate date) {
        if (!this.isActive || this.status != TaxRuleStatus.ACTIVE) {
            return false;
        }

        boolean isAfterEffective = this.effectiveDate == null || !date.isBefore(this.effectiveDate);
        boolean isBeforeExpiry = this.expiryDate == null || !date.isAfter(this.expiryDate);

        return isAfterEffective && isBeforeExpiry;
    }

    /**
     * Evaluates the rule against provided context
     */
    public boolean evaluate(Map<String, Object> context) {
        if (!this.isEffectiveOn(LocalDate.now())) {
            return false;
        }

        for (RuleCondition condition : this.conditions) {
            if (!evaluateCondition(condition, context)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Executes the rule actions
     */
    public Map<String, Object> execute(Map<String, Object> context) {
        Map<String, Object> result = new HashMap<>(context);

        for (RuleAction action : this.actions) {
            result = executeAction(action, result);
        }

        return result;
    }

    private boolean evaluateCondition(RuleCondition condition, Map<String, Object> context) {
        Object actualValue = context.get(condition.getField());

        if (actualValue == null) {
            return !Boolean.TRUE.equals(condition.getIsRequired());
        }

        String expectedValue = condition.getValue();
        String operator = condition.getOperator();

        return switch (operator) {
            case "equals" -> actualValue.toString().equals(expectedValue);
            case "not_equals" -> !actualValue.toString().equals(expectedValue);
            case "contains" -> actualValue.toString().contains(expectedValue);
            case "greater_than" -> compareNumbers(actualValue, expectedValue) > 0;
            case "less_than" -> compareNumbers(actualValue, expectedValue) < 0;
            case "greater_equal" -> compareNumbers(actualValue, expectedValue) >= 0;
            case "less_equal" -> compareNumbers(actualValue, expectedValue) <= 0;
            case "in" -> List.of(expectedValue.split(",")).contains(actualValue.toString());
            default -> true;
        };
    }

    private int compareNumbers(Object actual, String expected) {
        try {
            BigDecimal actualNum = new BigDecimal(actual.toString());
            BigDecimal expectedNum = new BigDecimal(expected);
            return actualNum.compareTo(expectedNum);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private Map<String, Object> executeAction(RuleAction action, Map<String, Object> context) {
        Map<String, Object> result = new HashMap<>(context);

        return switch (action.getActionType()) {
            case "set_rate" -> {
                result.put("taxRate", new BigDecimal(action.getValue()));
                yield result;
            }
            case "set_exempt" -> {
                result.put("isExempt", Boolean.parseBoolean(action.getValue()));
                yield result;
            }
            case "apply_threshold" -> {
                result.put("threshold", new BigDecimal(action.getValue()));
                yield result;
            }
            case "multiply_by" -> {
                Object baseValue = result.get(action.getTarget());
                if (baseValue != null) {
                    BigDecimal multiplier = new BigDecimal(action.getValue());
                    BigDecimal base = new BigDecimal(baseValue.toString());
                    result.put(action.getTarget(), base.multiply(multiplier));
                }
                yield result;
            }
            default -> result;
        };
    }

    /**
     * Validates the tax rule
     */
    public void validate() {
        if (this.ruleName == null || this.ruleName.isBlank()) {
            throw new IllegalArgumentException("Rule name is required");
        }

        if (this.ruleType == null) {
            throw new IllegalArgumentException("Rule type is required");
        }

        if (this.jurisdiction == null) {
            throw new IllegalArgumentException("Jurisdiction is required");
        }

        if (this.taxType == null) {
            throw new IllegalArgumentException("Tax type is required");
        }

        if (this.priority == null) {
            this.priority = 0;
        }

        if (this.version == null) {
            this.version = 1;
        }
    }

    /**
     * Creates a new version of this tax rule
     */
    public TaxRule createNewVersion(String updatedBy) {
        TaxRule newVersion = TaxRule.builder()
            .tenantId(this.tenantId)
            .ruleId(generateRuleId())
            .ruleName(this.ruleName)
            .ruleType(this.ruleType)
            .jurisdiction(this.jurisdiction)
            .taxType(this.taxType)
            .description(this.description)
            .conditions(new ArrayList<>(this.conditions))
            .actions(new ArrayList<>(this.actions))
            .priority(this.priority)
            .status(TaxRuleStatus.DRAFT)
            .isActive(false)
            .appliesTo(new ArrayList<>(this.appliesTo))
            .exemptions(new ArrayList<>(this.exemptions))
            .thresholds(new HashMap<>(this.thresholds))
            .createdBy(updatedBy)
            .version(this.version + 1)
            .metadata(new HashMap<>(this.metadata))
            .build();

        newVersion.validate();
        return newVersion;
    }

    private static String generateRuleId() {
        return "RULE-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
