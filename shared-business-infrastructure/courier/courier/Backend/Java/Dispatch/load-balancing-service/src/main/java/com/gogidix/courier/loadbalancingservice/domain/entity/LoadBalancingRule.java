package com.gogidix.courier.loadbalancingservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Domain Entity representing a Load Balancing Rule.
 * Defines how work should be distributed among drivers.
 */
@Document(collection = "load_balancing_rules")
@CompoundIndex(name = "idx_rule_tenant", def = "{'tenantId': 1, 'priority': -1, 'active': 1}")
public class LoadBalancingRule {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("rule_type")
    private RuleType ruleType;

    @Field("strategy")
    private BalancingStrategy strategy;

    @Field("priority")
    private Integer priority;

    @Field("active")
    private Boolean active;

    @Field("conditions")
    private List<RuleCondition> conditions;

    @Field("zone_ids")
    private List<String> zoneIds;

    @Field("config")
    private Map<String, Object> config;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    @Field("version")
    private Long version;

    /**
     * Default constructor for persistence.
     */
    protected LoadBalancingRule() {
    }

    /**
     * Create a new LoadBalancingRule.
     *
     * @param tenantId  the tenant identifier
     * @param name      the rule name
     * @param ruleType  the rule type
     * @param strategy  the balancing strategy
     * @param priority  the priority (higher = more important)
     */
    public LoadBalancingRule(String tenantId, String name, RuleType ruleType,
                            BalancingStrategy strategy, Integer priority) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.name = Objects.requireNonNull(name, "name is required");
        this.ruleType = Objects.requireNonNull(ruleType, "ruleType is required");
        this.strategy = Objects.requireNonNull(strategy, "strategy is required");
        this.priority = Objects.requireNonNull(priority, "priority is required");
        this.active = true;
        this.conditions = new ArrayList<>();
        this.zoneIds = new ArrayList<>();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.version = 0L;
    }

    // Domain Logic Methods

    /**
     * Activate this rule.
     */
    public void activate() {
        this.active = true;
        this.updatedAt = Instant.now();
        this.version++;
    }

    /**
     * Deactivate this rule.
     */
    public void deactivate() {
        this.active = false;
        this.updatedAt = Instant.now();
        this.version++;
    }

    /**
     * Add a condition to this rule.
     *
     * @param condition the condition to add
     */
    public void addCondition(RuleCondition condition) {
        if (condition != null) {
            this.conditions.add(condition);
            this.updatedAt = Instant.now();
            this.version++;
        }
    }

    /**
     * Add zone to rule.
     *
     * @param zoneId the zone ID
     */
    public void addZone(String zoneId) {
        if (zoneId != null && !zoneId.isBlank() && !this.zoneIds.contains(zoneId)) {
            this.zoneIds.add(zoneId);
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Update configuration.
     *
     * @param config the new configuration
     */
    public void updateConfig(Map<String, Object> config) {
        this.config = config;
        this.updatedAt = Instant.now();
        this.version++;
    }

    /**
     * Check if rule matches given conditions.
     *
     * @param context the evaluation context
     * @return true if matches
     */
    public boolean matches(EvaluationContext context) {
        if (!active) {
            return false;
        }

        // Check zone match
        if (!zoneIds.isEmpty() && context.zoneId() != null) {
            if (!zoneIds.contains(context.zoneId())) {
                return false;
            }
        }

        // Check all conditions
        for (RuleCondition condition : conditions) {
            if (!condition.evaluate(context)) {
                return false;
            }
        }

        return true;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public BalancingStrategy getStrategy() {
        return strategy;
    }

    public Integer getPriority() {
        return priority;
    }

    public Boolean getActive() {
        return active;
    }

    public List<RuleCondition> getConditions() {
        return conditions;
    }

    public List<String> getZoneIds() {
        return zoneIds;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    // Setters for persistence
    protected void setId(String id) {
        this.id = id;
    }

    protected void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    protected void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    protected void setStrategy(BalancingStrategy strategy) {
        this.strategy = strategy;
    }

    protected void setPriority(Integer priority) {
        this.priority = priority;
    }

    protected void setActive(Boolean active) {
        this.active = active;
    }

    protected void setConditions(List<RuleCondition> conditions) {
        this.conditions = conditions;
    }

    protected void setZoneIds(List<String> zoneIds) {
        this.zoneIds = zoneIds;
    }

    protected void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    protected void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Rule type enum.
     */
    public enum RuleType {
        ZONE_BASED,
        CAPACITY_BASED,
        PERFORMANCE_BASED,
        PROXIMITY_BASED,
        TIME_BASED,
        CUSTOM
    }

    /**
     * Balancing strategy enum.
     */
    public enum BalancingStrategy {
        ROUND_ROBIN,
        LEAST_LOADED,
        WEIGHTED,
        GEOGRAPHIC,
        PERFORMANCE_BASED,
        CUSTOM
    }

    /**
     * Rule condition value object.
     */
    public static class RuleCondition {
        @Field("condition_type")
        private ConditionType conditionType;

        @Field("operator")
        private String operator;

        @Field("value")
        private Object value;

        public RuleCondition() {
        }

        public RuleCondition(ConditionType conditionType, String operator, Object value) {
            this.conditionType = conditionType;
            this.operator = operator;
            this.value = value;
        }

        public boolean evaluate(EvaluationContext context) {
            return switch (conditionType) {
                case TIME_OF_DAY -> evaluateTimeOfDay(context);
                case DAY_OF_WEEK -> evaluateDayOfWeek(context);
                case DRIVER_COUNT -> evaluateDriverCount(context);
                case LOAD_THRESHOLD -> evaluateLoadThreshold(context);
                case MIN_RATING -> evaluateMinRating(context);
                case CUSTOM -> evaluateCustom(context);
            };
        }

        private boolean evaluateTimeOfDay(EvaluationContext context) {
            // Implementation would compare current hour with value
            return true;
        }

        private boolean evaluateDayOfWeek(EvaluationContext context) {
            // Implementation would compare current day with value
            return true;
        }

        private boolean evaluateDriverCount(EvaluationContext context) {
            if (context.driverCount() == null) return false;
            return switch (operator) {
                case "gt" -> context.driverCount() > ((Number) value).doubleValue();
                case "lt" -> context.driverCount() < ((Number) value).doubleValue();
                case "eq" -> context.driverCount().equals(((Number) value).doubleValue());
                default -> true;
            };
        }

        private boolean evaluateLoadThreshold(EvaluationContext context) {
            if (context.currentLoad() == null) return false;
            return switch (operator) {
                case "gt" -> context.currentLoad() > ((Number) value).doubleValue();
                case "lt" -> context.currentLoad() < ((Number) value).doubleValue();
                case "eq" -> context.currentLoad().equals(((Number) value).doubleValue());
                default -> true;
            };
        }

        private boolean evaluateMinRating(EvaluationContext context) {
            if (context.avgRating() == null) return false;
            return context.avgRating() >= ((Number) value).doubleValue();
        }

        private boolean evaluateCustom(EvaluationContext context) {
            return true;
        }

        // Getters and Setters
        public ConditionType getConditionType() {
            return conditionType;
        }

        public void setConditionType(ConditionType conditionType) {
            this.conditionType = conditionType;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public enum ConditionType {
            TIME_OF_DAY,
            DAY_OF_WEEK,
            DRIVER_COUNT,
            LOAD_THRESHOLD,
            MIN_RATING,
            CUSTOM
        }
    }

    /**
     * Evaluation context record.
     */
    public record EvaluationContext(
            String zoneId,
            Integer driverCount,
            Double currentLoad,
            Double avgRating,
            String timeOfDay,
            String dayOfWeek
    ) {
    }
}
