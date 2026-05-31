package com.gogidix.shared.courier.pricing.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Command for creating a new pricing rule
 * Contains all validation and business rules for pricing configuration
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePricingRuleCommand {

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    @NotBlank(message = "Rule name is required")
    private String ruleName;

    private String description;

    @NotBlank(message = "Rule type is required")
    private String ruleType; // BASE_RATE, DISTANCE_BASED, TIME_BASED, SURGE, DISCOUNT

    @NotNull(message = "Priority is required")
    @Positive(message = "Priority must be positive")
    private Integer priority;

    private Boolean active;

    private String vehicleType; // CAR, BIKE, TRUCK, VAN

    private String serviceType; // STANDARD, EXPRESS, SAME_DAY

    // Pricing parameters
    private Map<String, Object> parameters;

    /**
     * Base rate parameters
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaseRateParams {
        private Double baseFare;
        private Double perKmRate;
        private Double perMinuteRate;
        private Double minimumFare;
    }

    /**
     * Distance-based parameters
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DistanceBasedParams {
        private Double baseFare;
        private List<DistanceTier> distanceTiers;
    }

    /**
     * Distance tier for progressive pricing
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DistanceTier {
        private Double fromKm;
        private Double toKm;
        private Double ratePerKm;
    }

    /**
     * Surge pricing parameters
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SurgeParams {
        private Double surgeMultiplier;
        private String triggerCondition; // DEMAND_HIGH, BAD_WEATHER, PEAK_HOURS
        private List<String> applicableHours;
    }

    /**
     * Discount parameters
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiscountParams {
        private Double discountPercentage;
        private Double maxDiscountAmount;
        private String discountCode;
        private String validityCondition;
    }

    /**
     * Time-based parameters
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeBasedParams {
        private String timeSlot; // MORNING, AFTERNOON, EVENING, NIGHT
        private Double rateMultiplier;
        private List<String> applicableDays;
    }
}
