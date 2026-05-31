package com.gogidix.shared.courier.dispatch.interfaces.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Request DTO for creating a dispatch order
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispatchRequest {

    @NotBlank(message = "Order ID is required")
    private String orderId;

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @Valid
    @NotNull(message = "Pickup location is required")
    private LocationDto pickupLocation;

    @Valid
    @NotNull(message = "Delivery location is required")
    private LocationDto deliveryLocation;

    @NotBlank(message = "Pickup address is required")
    private String pickupAddress;

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    @Pattern(regexp = "^(HIGH|MEDIUM|LOW)$", message = "Priority must be HIGH, MEDIUM, or LOW")
    private String priority;

    private LocalDateTime estimatedPickupTime;

    private LocalDateTime estimatedDeliveryTime;

    private Map<String, Object> metadata;

    @Positive(message = "Total amount must be positive")
    private Double totalAmount;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be 3-letter ISO code")
    private String currency;

    /**
     * Location DTO with coordinates
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationDto {

        @NotNull(message = "Latitude is required")
        private Double latitude;

        @NotNull(message = "Longitude is required")
        private Double longitude;
    }
}
