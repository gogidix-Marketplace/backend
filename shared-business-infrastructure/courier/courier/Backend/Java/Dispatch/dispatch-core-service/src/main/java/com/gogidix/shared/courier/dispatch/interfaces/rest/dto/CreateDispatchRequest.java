package com.gogidix.shared.courier.dispatch.interfaces.rest.dto;

import com.gogidix.shared.courier.dispatch.application.command.CreateDispatchOrderCommand;
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
 * REST Request DTO for creating dispatch orders
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDispatchRequest {

    @NotBlank(message = "Dispatch ID is required")
    private String dispatchId;

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

    @NotNull(message = "Priority is required")
    @Pattern(regexp = "^(HIGH|MEDIUM|LOW)$", message = "Priority must be HIGH, MEDIUM, or LOW")
    private String priority;

    private LocalDateTime estimatedPickupTime;

    private LocalDateTime estimatedDeliveryTime;

    private Map<String, Object> metadata;

    @Positive(message = "Total amount must be positive")
    private Double totalAmount;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be 3-letter ISO code")
    private String currency;

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

    /**
     * Convert to CreateDispatchOrderCommand
     */
    public CreateDispatchOrderCommand toCommand() {
        CreateDispatchOrderCommand.LocationCommand pickupLoc = CreateDispatchOrderCommand.LocationCommand.builder()
                .latitude(this.pickupLocation.getLatitude())
                .longitude(this.pickupLocation.getLongitude())
                .build();

        CreateDispatchOrderCommand.LocationCommand deliveryLoc = CreateDispatchOrderCommand.LocationCommand.builder()
                .latitude(this.deliveryLocation.getLatitude())
                .longitude(this.deliveryLocation.getLongitude())
                .build();

        return CreateDispatchOrderCommand.builder()
                .dispatchId(this.dispatchId)
                .orderId(this.orderId)
                .customerId(this.customerId)
                .pickupLocation(pickupLoc)
                .deliveryLocation(deliveryLoc)
                .pickupAddress(this.pickupAddress)
                .deliveryAddress(this.deliveryAddress)
                .priority(this.priority)
                .estimatedPickupTime(this.estimatedPickupTime)
                .estimatedDeliveryTime(this.estimatedDeliveryTime)
                .metadata(this.metadata)
                .totalAmount(this.totalAmount)
                .currency(this.currency)
                .build();
    }
}
