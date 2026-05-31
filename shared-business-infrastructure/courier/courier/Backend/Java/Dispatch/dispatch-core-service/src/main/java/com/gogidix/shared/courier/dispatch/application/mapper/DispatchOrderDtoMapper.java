package com.gogidix.shared.courier.dispatch.application.mapper;

import com.gogidix.shared.courier.dispatch.application.command.CreateDispatchOrderCommand;
import com.gogidix.shared.courier.dispatch.application.command.UpdateDispatchOrderCommand;
import com.gogidix.shared.courier.dispatch.application.dto.DispatchOrderDTO;
import com.gogidix.shared.courier.dispatch.domain.entity.DispatchOrder;
import com.gogidix.shared.courier.dispatch.domain.entity.DispatchStatus;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mapper for DispatchOrder entity and DTOs
 * Handles bidirectional mapping between entities and DTOs
 */
@Component
public class DispatchOrderDtoMapper {

    /**
     * Convert CreateDispatchOrderCommand to DispatchOrder entity
     */
    public DispatchOrder toEntity(CreateDispatchOrderCommand command) {
        GeoJsonPoint pickupLocation = new GeoJsonPoint(
                command.getPickupLocation().getLongitude(),
                command.getPickupLocation().getLatitude());

        GeoJsonPoint deliveryLocation = new GeoJsonPoint(
                command.getDeliveryLocation().getLongitude(),
                command.getDeliveryLocation().getLatitude());

        // Convert priority string to integer
        int priorityValue = switch (command.getPriority().toUpperCase()) {
            case "HIGH" -> 1;
            case "MEDIUM" -> 2;
            case "LOW" -> 3;
            default -> 2;
        };

        return DispatchOrder.builder()
                .dispatchId(command.getDispatchId())
                .orderId(command.getOrderId())
                .customerId(command.getCustomerId())
                .pickupLocation(pickupLocation)
                .deliveryLocation(deliveryLocation)
                .pickupAddress(command.getPickupAddress())
                .deliveryAddress(command.getDeliveryAddress())
                .status(DispatchStatus.PENDING)
                .priority(priorityValue)
                .estimatedPickupTime(command.getEstimatedPickupTime())
                .estimatedDeliveryTime(command.getEstimatedDeliveryTime())
                .metadata(command.getMetadata())
                .totalAmount(command.getTotalAmount())
                .currency(command.getCurrency())
                .build();
    }

    /**
     * Update DispatchOrder entity from UpdateDispatchOrderCommand
     * Only updates non-null fields
     */
    public void updateEntity(DispatchOrder dispatchOrder, UpdateDispatchOrderCommand command) {
        if (command.getOrderId() != null) {
            dispatchOrder.setOrderId(command.getOrderId());
        }
        if (command.getCustomerId() != null) {
            dispatchOrder.setCustomerId(command.getCustomerId());
        }
        if (command.getPickupLocation() != null) {
            GeoJsonPoint pickupLocation = new GeoJsonPoint(
                    command.getPickupLocation().getLongitude(),
                    command.getPickupLocation().getLatitude());
            dispatchOrder.setPickupLocation(pickupLocation);
        }
        if (command.getDeliveryLocation() != null) {
            GeoJsonPoint deliveryLocation = new GeoJsonPoint(
                    command.getDeliveryLocation().getLongitude(),
                    command.getDeliveryLocation().getLatitude());
            dispatchOrder.setDeliveryLocation(deliveryLocation);
        }
        if (command.getPickupAddress() != null) {
            dispatchOrder.setPickupAddress(command.getPickupAddress());
        }
        if (command.getDeliveryAddress() != null) {
            dispatchOrder.setDeliveryAddress(command.getDeliveryAddress());
        }
        if (command.getStatus() != null) {
            dispatchOrder.setStatus(DispatchStatus.valueOf(command.getStatus()));
        }
        if (command.getPriority() != null) {
            int priorityValue = switch (command.getPriority().toUpperCase()) {
                case "HIGH" -> 1;
                case "MEDIUM" -> 2;
                case "LOW" -> 3;
                default -> 2;
            };
            dispatchOrder.setPriority(priorityValue);
        }
        if (command.getAssignedDriverId() != null) {
            dispatchOrder.setAssignedDriverId(command.getAssignedDriverId());
        }
        if (command.getAssignedVehicleId() != null) {
            dispatchOrder.setAssignedVehicleId(command.getAssignedVehicleId());
        }
        if (command.getEstimatedPickupTime() != null) {
            dispatchOrder.setEstimatedPickupTime(command.getEstimatedPickupTime());
        }
        if (command.getEstimatedDeliveryTime() != null) {
            dispatchOrder.setEstimatedDeliveryTime(command.getEstimatedDeliveryTime());
        }
        if (command.getActualPickupTime() != null) {
            dispatchOrder.setActualPickupTime(command.getActualPickupTime());
        }
        if (command.getActualDeliveryTime() != null) {
            dispatchOrder.setActualDeliveryTime(command.getActualDeliveryTime());
        }
        if (command.getMetadata() != null) {
            dispatchOrder.setMetadata(command.getMetadata());
        }
        if (command.getTotalAmount() != null) {
            dispatchOrder.setTotalAmount(command.getTotalAmount());
        }
        if (command.getCurrency() != null) {
            dispatchOrder.setCurrency(command.getCurrency());
        }
    }

    /**
     * Convert DispatchOrder entity to DispatchOrderDTO
     */
    public DispatchOrderDTO toDTO(DispatchOrder dispatchOrder) {
        DispatchOrderDTO.LocationDto pickupLocationDto = null;
        if (dispatchOrder.getPickupLocation() != null) {
            pickupLocationDto = DispatchOrderDTO.LocationDto.builder()
                    .latitude(dispatchOrder.getPickupLocation().getY())
                    .longitude(dispatchOrder.getPickupLocation().getX())
                    .build();
        }

        DispatchOrderDTO.LocationDto deliveryLocationDto = null;
        if (dispatchOrder.getDeliveryLocation() != null) {
            deliveryLocationDto = DispatchOrderDTO.LocationDto.builder()
                    .latitude(dispatchOrder.getDeliveryLocation().getY())
                    .longitude(dispatchOrder.getDeliveryLocation().getX())
                    .build();
        }

        return DispatchOrderDTO.builder()
                .id(dispatchOrder.getId())
                .tenantId(dispatchOrder.getTenantId())
                .dispatchId(dispatchOrder.getDispatchId())
                .orderId(dispatchOrder.getOrderId())
                .customerId(dispatchOrder.getCustomerId())
                .pickupLocation(pickupLocationDto)
                .deliveryLocation(deliveryLocationDto)
                .pickupAddress(dispatchOrder.getPickupAddress())
                .deliveryAddress(dispatchOrder.getDeliveryAddress())
                .status(dispatchOrder.getStatus() != null ? dispatchOrder.getStatus().name() : null)
                .priority(dispatchOrder.getPriority())
                .assignedDriverId(dispatchOrder.getAssignedDriverId())
                .assignedVehicleId(dispatchOrder.getAssignedVehicleId())
                .estimatedPickupTime(dispatchOrder.getEstimatedPickupTime())
                .estimatedDeliveryTime(dispatchOrder.getEstimatedDeliveryTime())
                .actualPickupTime(dispatchOrder.getActualPickupTime())
                .actualDeliveryTime(dispatchOrder.getActualDeliveryTime())
                .metadata(dispatchOrder.getMetadata())
                .distanceMeters(dispatchOrder.getDistanceMeters())
                .estimatedDurationMinutes(dispatchOrder.getEstimatedDurationMinutes())
                .totalAmount(dispatchOrder.getTotalAmount())
                .currency(dispatchOrder.getCurrency())
                .createdAt(dispatchOrder.getCreatedAt())
                .updatedAt(dispatchOrder.getUpdatedAt())
                .build();
    }

    /**
     * Convert list of DispatchOrder entities to list of DispatchOrderDTOs
     */
    public List<DispatchOrderDTO> toDTOList(List<DispatchOrder> dispatchOrders) {
        return dispatchOrders.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
