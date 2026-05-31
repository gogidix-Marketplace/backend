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
 * Command for calculating delivery price
 * Contains all required parameters for price calculation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculatePriceCommand {

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    @NotBlank(message = "Service type is required")
    private String serviceType; // STANDARD, EXPRESS, SAME_DAY

    @NotBlank(message = "Vehicle type is required")
    private String vehicleType; // CAR, BIKE, TRUCK, VAN

    @NotNull(message = "Pickup location is required")
    private LocationCommand pickupLocation;

    @NotNull(message = "Delivery location is required")
    private LocationCommand deliveryLocation;

    @Positive(message = "Distance must be positive")
    private Double distanceKm;

    @Positive(message = "Estimated duration must be positive")
    private Integer estimatedDurationMinutes;

    private Double packageWeightKg;

    private String packageCategory; // DOCUMENT, SMALL, MEDIUM, LARGE, FRAGILE

    private Boolean urgentDelivery;

    private String scheduledTime;

    private String promoCode;

    private List<String> applicableDiscounts;

    private Map<String, String> metadata;

    /**
     * Location command with coordinates
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationCommand {
        @NotNull(message = "Latitude is required")
        private Double latitude;

        @NotNull(message = "Longitude is required")
        private Double longitude;

        private String address;
        private String city;
        private String postalCode;
    }
}
