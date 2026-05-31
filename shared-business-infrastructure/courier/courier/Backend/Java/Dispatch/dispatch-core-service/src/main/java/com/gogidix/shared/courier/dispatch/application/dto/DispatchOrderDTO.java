package com.gogidix.shared.courier.dispatch.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Application DTO for Dispatch Order entity
 * Used for data transfer between application and infrastructure layers
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispatchOrderDTO {

    private String id;

    private String tenantId;

    private String dispatchId;

    private String orderId;

    private String customerId;

    private LocationDto pickupLocation;

    private LocationDto deliveryLocation;

    private String pickupAddress;

    private String deliveryAddress;

    private String status;

    private Integer priority;

    private String assignedDriverId;

    private String assignedVehicleId;

    private LocalDateTime estimatedPickupTime;

    private LocalDateTime estimatedDeliveryTime;

    private LocalDateTime actualPickupTime;

    private LocalDateTime actualDeliveryTime;

    private Map<String, Object> metadata;

    private Double distanceMeters;

    private Double estimatedDurationMinutes;

    private Double totalAmount;

    private String currency;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * Location DTO with coordinates as array [longitude, latitude]
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationDto {

        private Double latitude;

        private Double longitude;
    }
}
