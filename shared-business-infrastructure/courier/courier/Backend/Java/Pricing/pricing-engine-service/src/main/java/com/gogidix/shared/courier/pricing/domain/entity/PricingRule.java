package com.gogidix.shared.courier.pricing.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Pricing Rule Entity
 * Multi-tenant document for pricing engine management
 * Defines pricing strategies and rules for delivery services
 */
@Document(collection = "pricing_rules")
@CompoundIndex(def = "{'tenantId': 1, 'ruleId': 1}", unique = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingRule {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String ruleId;

    private String ruleName;

    private String description;

    @Indexed
    private String ruleType; // BASE_RATE, DISTANCE_BASED, TIME_BASED, SURGE, DISCOUNT

    @Indexed
    private Integer priority;

    @Indexed
    private Boolean active;

    @Indexed
    private String vehicleType; // CAR, BIKE, TRUCK, VAN

    @Indexed
    private String serviceType; // STANDARD, EXPRESS, SAME_DAY

    private Map<String, Object> parameters;

    private String createdBy;

    private String updatedBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
