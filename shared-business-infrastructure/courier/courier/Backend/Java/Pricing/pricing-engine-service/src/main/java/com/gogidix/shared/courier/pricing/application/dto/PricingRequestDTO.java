package com.gogidix.shared.courier.pricing.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for pricing request tracking
 * Used for logging and audit of price calculations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingRequestDTO {

    private String id;

    private String tenantId;

    private String requestId;

    private String serviceType;

    private String vehicleType;

    private Double distanceKm;

    private Integer estimatedDurationMinutes;

    private Double packageWeightKg;

    private String packageCategory;

    private String promoCode;

    private String status; // PENDING, CALCULATED, FAILED

    private String errorMessage;

    private PriceCalculationDTO calculationResult;

    private Map<String, String> metadata;

    private LocalDateTime createdAt;

    private LocalDateTime processedAt;
}
