package com.gogidix.shared.courier.dispatch.interfaces.rest.dto;

import com.gogidix.shared.courier.dispatch.application.dto.DispatchOrderDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * REST Response DTO for dispatch order data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispatchResponse {

    private String id;

    private String tenantId;

    private String dispatchId;

    private String orderId;

    private String customerId;

    private LocationResponse pickupLocation;

    private LocationResponse deliveryLocation;

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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationResponse {

        private Double latitude;

        private Double longitude;
    }

    /**
     * Convert from DispatchOrderDTO to DispatchResponse
     */
    public static DispatchResponse fromDTO(DispatchOrderDTO dto) {
        LocationResponse pickupLoc = null;
        if (dto.getPickupLocation() != null) {
            pickupLoc = LocationResponse.builder()
                    .latitude(dto.getPickupLocation().getLatitude())
                    .longitude(dto.getPickupLocation().getLongitude())
                    .build();
        }

        LocationResponse deliveryLoc = null;
        if (dto.getDeliveryLocation() != null) {
            deliveryLoc = LocationResponse.builder()
                    .latitude(dto.getDeliveryLocation().getLatitude())
                    .longitude(dto.getDeliveryLocation().getLongitude())
                    .build();
        }

        return DispatchResponse.builder()
                .id(dto.getId())
                .tenantId(dto.getTenantId())
                .dispatchId(dto.getDispatchId())
                .orderId(dto.getOrderId())
                .customerId(dto.getCustomerId())
                .pickupLocation(pickupLoc)
                .deliveryLocation(deliveryLoc)
                .pickupAddress(dto.getPickupAddress())
                .deliveryAddress(dto.getDeliveryAddress())
                .status(dto.getStatus())
                .priority(dto.getPriority())
                .assignedDriverId(dto.getAssignedDriverId())
                .assignedVehicleId(dto.getAssignedVehicleId())
                .estimatedPickupTime(dto.getEstimatedPickupTime())
                .estimatedDeliveryTime(dto.getEstimatedDeliveryTime())
                .actualPickupTime(dto.getActualPickupTime())
                .actualDeliveryTime(dto.getActualDeliveryTime())
                .metadata(dto.getMetadata())
                .distanceMeters(dto.getDistanceMeters())
                .estimatedDurationMinutes(dto.getEstimatedDurationMinutes())
                .totalAmount(dto.getTotalAmount())
                .currency(dto.getCurrency())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
