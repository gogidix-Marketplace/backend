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
 * Pricing Model Entity
 * Stores calculated price quotes with full breakdown
 * Used for audit and reference of price calculations
 */
@Document(collection = "pricing_models")
@CompoundIndex(def = "{'tenantId': 1, 'quoteId': 1}", unique = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingModel {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String quoteId;

    private String serviceType;

    private String vehicleType;

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

    private Integer validityMinutes;

    private LocalDateTime calculatedAt;

    private LocalDateTime expiresAt;

    private Map<String, Object> breakdown;

    private Map<String, String> metadata;

    private String requestId;

    @Indexed
    private Boolean accepted;
}
