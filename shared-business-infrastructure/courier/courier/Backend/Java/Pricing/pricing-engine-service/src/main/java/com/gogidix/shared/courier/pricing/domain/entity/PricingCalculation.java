package com.gogidix.shared.courier.pricing.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Pricing Calculation Entity
 * Multi-tenant document storing historical price calculations
 * Records all pricing calculations for audit and analytics
 */
@Document(collection = "pricing_calculations")
@CompoundIndex(def = "{'tenantId': 1, 'calculationId': 1}", unique = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingCalculation {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String calculationId;

    private String customerId;

    private String requestId;

    private String serviceType;

    private String vehicleType;

    // Pricing components
    private BigDecimal baseFare;

    private BigDecimal distanceCharge;

    private BigDecimal timeCharge;

    private BigDecimal weightCharge;

    private BigDecimal surcharge;

    private BigDecimal discount;

    private BigDecimal tax;

    private BigDecimal subtotal;

    private BigDecimal totalAmount;

    private String currency;

    // Calculation details
    private Double distanceKm;

    private Integer estimatedDurationMinutes;

    private Double packageWeightKg;

    private String packageCategory;

    private Boolean urgentDelivery;

    private String appliedPromoCode;

    private BigDecimal discountPercentage;

    private BigDecimal taxPercentage;

    // Rule applied
    private String appliedRuleId;

    private String appliedRuleName;

    // Additional breakdown
    private Map<String, Object> breakdown;

    private Map<String, String> metadata;

    @Indexed
    private LocalDateTime createdAt;

    @Indexed
    private LocalDateTime expiresAt;

    private String createdBy;
}
