package com.gogidix.shared.courier.dispatch.application.service;

import com.gogidix.shared.courier.dispatch.application.command.CreateDispatchOrderCommand;
import com.gogidix.shared.courier.dispatch.application.command.UpdateDispatchOrderCommand;
import com.gogidix.shared.courier.dispatch.application.dto.DispatchOrderDTO;
import com.gogidix.shared.courier.dispatch.application.mapper.DispatchOrderDtoMapper;
import com.gogidix.shared.courier.dispatch.application.query.DispatchOrderQuery;
import com.gogidix.shared.courier.dispatch.domain.entity.DispatchOrder;
import com.gogidix.shared.courier.dispatch.domain.entity.DispatchStatus;
import com.gogidix.shared.courier.dispatch.domain.events.DispatchOrderCreatedEvent;
import com.gogidix.shared.courier.dispatch.domain.events.DispatchStatusChangedEvent;
import com.gogidix.shared.courier.dispatch.domain.events.DriverAssignedEvent;
import com.gogidix.shared.courier.dispatch.domain.repository.DispatchOrderRepository;
import com.gogidix.shared.courier.dispatch.infrastructure.messaging.DispatchEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Application Service for Dispatch Order Management
 * Orchestrates business operations and publishes domain events
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DispatchOrderApplicationService {

    private final DispatchOrderRepository dispatchOrderRepository;
    private final DispatchOrderDtoMapper dispatchOrderDtoMapper;
    private final DispatchEventPublisher eventPublisher;

    /**
     * Create a new dispatch order
     */
    @Transactional
    public DispatchOrderDTO createDispatchOrder(CreateDispatchOrderCommand command) {
        log.info("Creating dispatch order: {}", command.getDispatchId());

        // Check if dispatch order already exists
        if (dispatchOrderRepository.existsByTenantIdAndDispatchId("default-tenant", command.getDispatchId())) {
            throw new IllegalArgumentException("Dispatch order with ID " + command.getDispatchId() + " already exists");
        }

        // Convert command to entity
        DispatchOrder dispatchOrder = dispatchOrderDtoMapper.toEntity(command);
        dispatchOrder.setTenantId("default-tenant");

        // Save dispatch order
        DispatchOrder savedDispatch = dispatchOrderRepository.save(dispatchOrder);

        // Publish domain event
        DispatchOrderCreatedEvent event = DispatchOrderCreatedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tenantId(savedDispatch.getTenantId())
                .dispatchId(savedDispatch.getDispatchId())
                .orderId(savedDispatch.getOrderId())
                .customerId(savedDispatch.getCustomerId())
                .pickupLatitude(savedDispatch.getPickupLocation().getY())
                .pickupLongitude(savedDispatch.getPickupLocation().getX())
                .deliveryLatitude(savedDispatch.getDeliveryLocation().getY())
                .deliveryLongitude(savedDispatch.getDeliveryLocation().getX())
                .priority(savedDispatch.getPriority())
                .status(savedDispatch.getStatus().name())
                .occurredAt(LocalDateTime.now())
                .build();
        eventPublisher.publishDispatchCreated(event);

        log.info("Successfully created dispatch order: {}", savedDispatch.getDispatchId());
        return dispatchOrderDtoMapper.toDTO(savedDispatch);
    }

    /**
     * Get dispatch order by ID
     */
    public DispatchOrderDTO getDispatchOrder(String tenantId, String dispatchId) {
        log.debug("Fetching dispatch order: {}", dispatchId);

        DispatchOrder dispatchOrder = dispatchOrderRepository.findByTenantIdAndDispatchId(tenantId, dispatchId)
                .orElseThrow(() -> new IllegalArgumentException("Dispatch order not found: " + dispatchId));

        return dispatchOrderDtoMapper.toDTO(dispatchOrder);
    }

    /**
     * Query dispatch orders with filters
     */
    public List<DispatchOrderDTO> queryDispatchOrders(DispatchOrderQuery query) {
        log.debug("Querying dispatch orders with filters: {}", query);

        List<DispatchOrder> dispatchOrders;

        if (query.getAssignedDriverId() != null) {
            dispatchOrders = dispatchOrderRepository.findByTenantIdAndAssignedDriverId(
                    query.getTenantId(), query.getAssignedDriverId());
        } else if (query.getCustomerId() != null) {
            dispatchOrders = dispatchOrderRepository.findByTenantIdAndCustomerId(
                    query.getTenantId(), query.getCustomerId());
        } else if (query.getStatus() != null) {
            dispatchOrders = dispatchOrderRepository.findByTenantIdAndStatus(
                    query.getTenantId(), DispatchStatus.valueOf(query.getStatus()));
        } else {
            dispatchOrders = dispatchOrderRepository.findByTenantId(query.getTenantId());
        }

        return dispatchOrderDtoMapper.toDTOList(dispatchOrders);
    }

    /**
     * Update dispatch order
     */
    @Transactional
    public DispatchOrderDTO updateDispatchOrder(String tenantId, String dispatchId, UpdateDispatchOrderCommand command) {
        log.info("Updating dispatch order: {}", dispatchId);

        DispatchOrder dispatchOrder = dispatchOrderRepository.findByTenantIdAndDispatchId(tenantId, dispatchId)
                .orElseThrow(() -> new IllegalArgumentException("Dispatch order not found: " + dispatchId));

        // Track old status for event
        DispatchStatus oldStatus = dispatchOrder.getStatus();

        // Update entity
        dispatchOrderDtoMapper.updateEntity(dispatchOrder, command);

        // Save updated dispatch order
        DispatchOrder updatedDispatch = dispatchOrderRepository.save(dispatchOrder);

        // Publish status change event if status changed
        if (updatedDispatch.getStatus() != oldStatus && command.getStatus() != null) {
            DispatchStatusChangedEvent event = DispatchStatusChangedEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .tenantId(updatedDispatch.getTenantId())
                    .dispatchId(updatedDispatch.getDispatchId())
                    .orderId(updatedDispatch.getOrderId())
                    .oldStatus(oldStatus.name())
                    .newStatus(updatedDispatch.getStatus().name())
                    .occurredAt(LocalDateTime.now())
                    .build();
            eventPublisher.publishStatusChanged(event);
        }

        log.info("Successfully updated dispatch order: {}", updatedDispatch.getDispatchId());
        return dispatchOrderDtoMapper.toDTO(updatedDispatch);
    }

    /**
     * Assign driver to dispatch order
     */
    @Transactional
    public DispatchOrderDTO assignDriver(String tenantId, String dispatchId, String driverId, String vehicleId) {
        log.info("Assigning driver {} to dispatch order: {}", driverId, dispatchId);

        DispatchOrder dispatchOrder = dispatchOrderRepository.findByTenantIdAndDispatchId(tenantId, dispatchId)
                .orElseThrow(() -> new IllegalArgumentException("Dispatch order not found: " + dispatchId));

        dispatchOrder.setAssignedDriverId(driverId);
        dispatchOrder.setAssignedVehicleId(vehicleId);
        dispatchOrder.setStatus(DispatchStatus.ASSIGNED);

        DispatchOrder updatedDispatch = dispatchOrderRepository.save(dispatchOrder);

        // Publish driver assigned event
        DriverAssignedEvent event = DriverAssignedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tenantId(updatedDispatch.getTenantId())
                .dispatchId(updatedDispatch.getDispatchId())
                .orderId(updatedDispatch.getOrderId())
                .driverId(driverId)
                .vehicleId(vehicleId)
                .assignedAt(LocalDateTime.now())
                .occurredAt(LocalDateTime.now())
                .build();
        eventPublisher.publishDriverAssigned(event);

        log.info("Successfully assigned driver to dispatch order: {}", updatedDispatch.getDispatchId());
        return dispatchOrderDtoMapper.toDTO(updatedDispatch);
    }

    /**
     * Update dispatch order status
     */
    @Transactional
    public DispatchOrderDTO updateStatus(String tenantId, String dispatchId, String newStatus) {
        log.info("Updating status of dispatch order {} to {}", dispatchId, newStatus);

        DispatchOrder dispatchOrder = dispatchOrderRepository.findByTenantIdAndDispatchId(tenantId, dispatchId)
                .orElseThrow(() -> new IllegalArgumentException("Dispatch order not found: " + dispatchId));

        DispatchStatus oldStatus = dispatchOrder.getStatus();
        DispatchStatus status = DispatchStatus.valueOf(newStatus);

        dispatchOrder.setStatus(status);

        // Update actual times based on status
        if (status == DispatchStatus.PICKED_UP && dispatchOrder.getActualPickupTime() == null) {
            dispatchOrder.setActualPickupTime(LocalDateTime.now());
        } else if (status == DispatchStatus.DELIVERED && dispatchOrder.getActualDeliveryTime() == null) {
            dispatchOrder.setActualDeliveryTime(LocalDateTime.now());
        }

        DispatchOrder updatedDispatch = dispatchOrderRepository.save(dispatchOrder);

        // Publish status change event
        DispatchStatusChangedEvent event = DispatchStatusChangedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tenantId(updatedDispatch.getTenantId())
                .dispatchId(updatedDispatch.getDispatchId())
                .orderId(updatedDispatch.getOrderId())
                .oldStatus(oldStatus.name())
                .newStatus(updatedDispatch.getStatus().name())
                .occurredAt(LocalDateTime.now())
                .build();
        eventPublisher.publishStatusChanged(event);

        log.info("Successfully updated dispatch order status: {}", updatedDispatch.getDispatchId());
        return dispatchOrderDtoMapper.toDTO(updatedDispatch);
    }

    /**
     * Find nearby dispatches for pickup
     */
    public List<DispatchOrderDTO> findNearbyPickups(String tenantId, double latitude, double longitude, double maxDistanceKm) {
        log.info("Finding nearby pickups for tenant: {} at location: ({}, {})", tenantId, latitude, longitude);

        GeoJsonPoint location = new GeoJsonPoint(longitude, latitude);
        double maxDistanceMeters = maxDistanceKm * 1000;

        List<DispatchOrder> nearbyPickups = dispatchOrderRepository.findNearbyPickups(tenantId, location, maxDistanceMeters);

        return dispatchOrderDtoMapper.toDTOList(nearbyPickups);
    }
}
