package com.gogidix.shared.courier.dispatch.application.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Command to update an existing dispatch order
 * All fields are optional - only provided fields will be updated
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDispatchOrderCommand {

    private String orderId;

    private String customerId;

    @Valid
    private LocationCommand pickupLocation;

    @Valid
    private LocationCommand deliveryLocation;

    private String pickupAddress;

    private String deliveryAddress;

    @Pattern(regexp = "^(PENDING|CONFIRMED|ASSIGNED|PICKUP_IN_PROGRESS|PICKED_UP|IN_TRANSIT|DELIVERY_IN_PROGRESS|DELIVERED|CANCELLED|FAILED)$")
    private String status;

    @Pattern(regexp = "^(HIGH|MEDIUM|LOW)$")
    private String priority;

    private String assignedDriverId;

    private String assignedVehicleId;

    private LocalDateTime estimatedPickupTime;

    private LocalDateTime estimatedDeliveryTime;

    private LocalDateTime actualPickupTime;

    private LocalDateTime actualDeliveryTime;

    private Map<String, Object> metadata;

    @Positive
    private Double totalAmount;

    @Pattern(regexp = "^[A-Z]{3}$")
    private String currency;

    /**
     * Location command with coordinates
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationCommand {

        private Double latitude;

        private Double longitude;
    }
}
