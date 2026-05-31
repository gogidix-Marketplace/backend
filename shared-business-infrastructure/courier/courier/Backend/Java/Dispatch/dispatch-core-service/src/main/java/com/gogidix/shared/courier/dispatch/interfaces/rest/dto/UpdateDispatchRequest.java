package com.gogidix.shared.courier.dispatch.interfaces.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * REST Request DTO for updating dispatch orders
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDispatchRequest {

    private String orderId;

    private String customerId;

    @Valid
    private LocationDto pickupLocation;

    @Valid
    private LocationDto deliveryLocation;

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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationDto {

        private Double latitude;

        private Double longitude;
    }
}
