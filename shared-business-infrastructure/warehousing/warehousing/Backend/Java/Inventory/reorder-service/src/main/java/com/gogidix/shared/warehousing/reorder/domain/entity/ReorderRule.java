package com.gogidix.shared.warehousing.reorder.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Reorder Rule Entity - Multi-tenant with MongoDB
 *
 * Defines rules for automated reorder calculations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reorder_rules")
@CompoundIndex(def = "{'tenantId': 1, 'ruleName': 1}", name = "idx_tenant_rule")
@Schema(description = "Reorder rule for automated calculations")
public class ReorderRule {

    @Id
    @Schema(description = "Unique identifier for the rule")
    private String id;

    @Indexed
    @Schema(description = "Tenant identifier for multi-tenancy")
    private String tenantId;

    @Indexed
    @Schema(description = "Rule name", required = true)
    private String ruleName;

    @Schema(description = "Rule description")
    private String description;

    @Schema(description = "Rule type", required = true)
    private RuleType ruleType;

    @Schema(description = "Applicable to all products or specific SKUs")
    private Boolean applyToAllProducts;

    @Schema(description = "Applicable SKUs (if not all products)")
    private java.util.List<String> applicableSkus;

    @Schema(description = "Applicable categories")
    private java.util.List<String> applicableCategories;

    @Schema(description = "Reorder level calculation method")
    private CalculationMethod calculationMethod;

    @Schema(description = "Rule parameters (key-value pairs)")
    private Map<String, Object> parameters;

    @Schema(description = "Minimum reorder level")
    private Integer minReorderLevel;

    @Schema(description = "Maximum reorder level")
    private Integer maxReorderLevel;

    @Schema(description = "Safety stock percentage")
    private Double safetyStockPercentage;

    @Schema(description = "Lead time buffer days")
    private Integer leadTimeBufferDays;

    @Schema(description = "Demand forecast days")
    private Integer demandForecastDays;

    @Schema(description = "Rule priority (higher = more priority)")
    private Integer priority;

    @Schema(description = "Rule status", required = true)
    private RuleStatus status;

    @Schema(description = "Effective from date")
    private LocalDateTime effectiveFromDate;

    @Schema(description = "Effective to date")
    private LocalDateTime effectiveToDate;

    @CreatedDate
    @Schema(description = "Timestamp when the rule was created")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Schema(description = "Timestamp when the rule was last updated")
    private LocalDateTime updatedAt;

    /**
     * Rule type enumeration
     */
    public enum RuleType {
        PERCENTAGE_OF_MAX,
        DAYS_OF_SUPPLY,
        BASED_ON_FORECAST,
        MIN_MAX_LEVELS,
        STATIC_REORDER_POINT
    }

    /**
     * Calculation method enumeration
     */
    public enum CalculationMethod {
        AVERAGE_DAILY_USAGE,
        WEIGHTED_AVERAGE,
        MOVING_AVERAGE,
        SEASONAL_FORECAST,
        MANUAL
    }

    /**
     * Rule status enumeration
     */
    public enum RuleStatus {
        ACTIVE,
        INACTIVE,
        DRAFT,
        ARCHIVED
    }
}
