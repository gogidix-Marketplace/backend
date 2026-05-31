package com.gogidix.shared.courier.dispatch.application.service;

import com.gogidix.shared.courier.dispatch.application.command.AssignDriverCommand;
import com.gogidix.shared.courier.dispatch.application.command.CancelDispatchCommand;
import com.gogidix.shared.courier.dispatch.application.command.CompleteDispatchCommand;
import com.gogidix.shared.courier.dispatch.application.command.CreateDispatchOrderCommand;
import com.gogidix.shared.courier.dispatch.application.dto.DispatchOrderDTO;
import com.gogidix.shared.courier.dispatch.application.mapper.DispatchOrderDtoMapper;
import com.gogidix.shared.courier.dispatch.application.query.DispatchQuery;
import com.gogidix.shared.courier.dispatch.domain.entity.DispatchOrder;
import com.gogidix.shared.courier.dispatch.domain.entity.DispatchStatus;
import com.gogidix.shared.courier.dispatch.domain.events.*;
import com.gogidix.shared.courier.dispatch.domain.repository.DispatchOrderRepository;
import com.gogidix.shared.courier.dispatch.infrastructure.messaging.DispatchEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Application Service for Dispatch Management
 * Orchestrates business operations and publishes domain events
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DispatchApplicationService {

    private final DispatchOrderRepository dispatchOrderRepository;
    private final DispatchOrderDtoMapper dispatchOrderDtoMapper;
    private final DispatchEventPublisher eventPublisher;

    /**
     * Create a new dispatch order
     */
    @Transactional
    public DispatchOrderDTO createDispatch(String tenantId, CreateDispatchOrderCommand command) {
        log.info("Creating dispatch order: {} for tenant: {}", command.getDispatchId(), tenantId);

        // Check if dispatch order already exists
        if (dispatchOrderRepository.existsByTenantIdAndDispatchId(tenantId, command.getDispatchId())) {
            throw new IllegalArgumentException("Dispatch order with ID " + command.getDispatchId() + " already exists");
        }

        // Convert command to entity
        DispatchOrder dispatchOrder = dispatchOrderDtoMapper.toEntity(command);
        dispatchOrder.setTenantId(tenantId);

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
    public DispatchOrderDTO getDispatch(String tenantId, String dispatchId) {
        log.debug("Fetching dispatch order: {} for tenant: {}", dispatchId, tenantId);

        DispatchOrder dispatchOrder = dispatchOrderRepository.findByTenantIdAndDispatchId(tenantId, dispatchId)
                .orElseThrow(() -> new IllegalArgumentException("Dispatch order not found: " + dispatchId));

        return dispatchOrderDtoMapper.toDTO(dispatchOrder);
    }

    /**
     * Query dispatch orders with filters
     */
    public List<DispatchOrderDTO> queryDispatches(String tenantId, DispatchQuery query) {
        log.debug("Querying dispatch orders with filters: {}", query);

        List<DispatchOrder> dispatchOrders;

        if (query.getDriverId() != null) {
            dispatchOrders = dispatchOrderRepository.findByTenantIdAndAssignedDriverId(tenantId, query.getDriverId());
        } else if (query.getCustomerId() != null) {
            dispatchOrders = dispatchOrderRepository.findByTenantIdAndCustomerId(tenantId, query.getCustomerId());
        } else if (query.getStatus() != null) {
            dispatchOrders = dispatchOrderRepository.findByTenantIdAndStatus(tenantId, DispatchStatus.valueOf(query.getStatus()));
        } else {
            dispatchOrders = dispatchOrderRepository.findByTenantId(tenantId);
        }

        return dispatchOrderDtoMapper.toDTOList(dispatchOrders);
    }

    /**
     * Assign driver to dispatch order
     */
    @Transactional
    public DispatchOrderDTO assignDriver(String tenantId, String dispatchId, AssignDriverCommand command) {
        log.info("Assigning driver {} to dispatch order: {}", command.getDriverId(), dispatchId);

        DispatchOrder dispatchOrder = dispatchOrderRepository.findByTenantIdAndDispatchId(tenantId, dispatchId)
                .orElseThrow(() -> new IllegalArgumentException("Dispatch order not found: " + dispatchId));

        // Validate status transition
        if (dispatchOrder.getStatus() == DispatchStatus.DELIVERED ||
            dispatchOrder.getStatus() == DispatchStatus.CANCELLED ||
            dispatchOrder.getStatus() == DispatchStatus.FAILED) {
            throw new IllegalArgumentException("Cannot assign driver to dispatch with status: " + dispatchOrder.getStatus());
        }

        dispatchOrder.setAssignedDriverId(command.getDriverId());
        dispatchOrder.setAssignedVehicleId(command.getVehicleId());
        dispatchOrder.setStatus(DispatchStatus.ASSIGNED);

        DispatchOrder updatedDispatch = dispatchOrderRepository.save(dispatchOrder);

        // Publish dispatch assigned event
        DispatchAssignedEvent event = DispatchAssignedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tenantId(updatedDispatch.getTenantId())
                .dispatchId(updatedDispatch.getDispatchId())
                .orderId(updatedDispatch.getOrderId())
                .driverId(command.getDriverId())
                .vehicleId(command.getVehicleId())
                .assignedAt(LocalDateTime.now())
                .occurredAt(LocalDateTime.now())
                .build();
        eventPublisher.publishDispatchAssigned(event);

        log.info("Successfully assigned driver to dispatch order: {}", updatedDispatch.getDispatchId());
        return dispatchOrderDtoMapper.toDTO(updatedDispatch);
    }

    /**
     * Cancel dispatch order
     */
    @Transactional
    public DispatchOrderDTO cancelDispatch(String tenantId, String dispatchId, CancelDispatchCommand command) {
        log.info("Cancelling dispatch order: {}", dispatchId);

        DispatchOrder dispatchOrder = dispatchOrderRepository.findByTenantIdAndDispatchId(tenantId, dispatchId)
                .orElseThrow(() -> new IllegalArgumentException("Dispatch order not found: " + dispatchId));

        // Validate status transition
        if (dispatchOrder.getStatus() == DispatchStatus.DELIVERED) {
            throw new IllegalArgumentException("Cannot cancel a delivered dispatch");
        }
        if (dispatchOrder.getStatus() == DispatchStatus.CANCELLED) {
            throw new IllegalArgumentException("Dispatch is already cancelled");
        }

        DispatchStatus oldStatus = dispatchOrder.getStatus();
        dispatchOrder.setStatus(DispatchStatus.CANCELLED);

        DispatchOrder updatedDispatch = dispatchOrderRepository.save(dispatchOrder);

        // Publish dispatch cancelled event
        DispatchCancelledEvent event = DispatchCancelledEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tenantId(updatedDispatch.getTenantId())
                .dispatchId(updatedDispatch.getDispatchId())
                .orderId(updatedDispatch.getOrderId())
                .customerId(updatedDispatch.getCustomerId())
                .cancelledBy(command.getCancelledBy())
                .cancellationReason(command.getCancellationReason())
                .cancelledAt(LocalDateTime.now())
                .occurredAt(LocalDateTime.now())
                .build();
        eventPublisher.publishDispatchCancelled(event);

        log.info("Successfully cancelled dispatch order: {}", updatedDispatch.getDispatchId());
        return dispatchOrderDtoMapper.toDTO(updatedDispatch);
    }

    /**
     * Complete dispatch order
     */
    @Transactional
    public DispatchOrderDTO completeDispatch(String tenantId, String dispatchId, CompleteDispatchCommand command) {
        log.info("Completing dispatch order: {}", dispatchId);

        DispatchOrder dispatchOrder = dispatchOrderRepository.findByTenantIdAndDispatchId(tenantId, dispatchId)
                .orElseThrow(() -> new IllegalArgumentException("Dispatch order not found: " + dispatchId));

        // Validate status
        if (dispatchOrder.getStatus() == DispatchStatus.DELIVERED) {
            throw new IllegalArgumentException("Dispatch is already delivered");
        }
        if (dispatchOrder.getStatus() == DispatchStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot complete a cancelled dispatch");
        }

        dispatchOrder.setStatus(DispatchStatus.DELIVERED);
        dispatchOrder.setActualDeliveryTime(LocalDateTime.now());
        dispatchOrder.setDistanceMeters(command.getActualDistanceMeters());
        dispatchOrder.setEstimatedDurationMinutes(command.getActualDurationMinutes() / 60.0);

        DispatchOrder updatedDispatch = dispatchOrderRepository.save(dispatchOrder);

        // Publish dispatch completed event
        DispatchCompletedEvent event = DispatchCompletedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tenantId(updatedDispatch.getTenantId())
                .dispatchId(updatedDispatch.getDispatchId())
                .orderId(updatedDispatch.getOrderId())
                .customerId(updatedDispatch.getCustomerId())
                .driverId(updatedDispatch.getAssignedDriverId())
                .vehicleId(updatedDispatch.getAssignedVehicleId())
                .completedAt(LocalDateTime.now())
                .actualDistanceMeters(command.getActualDistanceMeters())
                .actualDurationMinutes(command.getActualDurationMinutes())
                .occurredAt(LocalDateTime.now())
                .build();
        eventPublisher.publishDispatchCompleted(event);

        log.info("Successfully completed dispatch order: {}", updatedDispatch.getDispatchId());
        return dispatchOrderDtoMapper.toDTO(updatedDispatch);
    }

    /**
     * Get dashboard statistics
     */
    public Map<String, Object> getDashboard(String tenantId) {
        log.debug("Fetching dashboard statistics for tenant: {}", tenantId);

        List<DispatchOrder> allDispatches = dispatchOrderRepository.findByTenantId(tenantId);

        Map<DispatchStatus, Long> statusCounts = allDispatches.stream()
                .collect(Collectors.groupingBy(DispatchOrder::getStatus, Collectors.counting()));

        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalDispatches", (long) allDispatches.size());
        dashboard.put("pendingDispatches", statusCounts.getOrDefault(DispatchStatus.PENDING, 0L));
        dashboard.put("assignedDispatches", statusCounts.getOrDefault(DispatchStatus.ASSIGNED, 0L));
        dashboard.put("inTransitDispatches",
                statusCounts.getOrDefault(DispatchStatus.PICKUP_IN_PROGRESS, 0L) +
                statusCounts.getOrDefault(DispatchStatus.PICKED_UP, 0L) +
                statusCounts.getOrDefault(DispatchStatus.IN_TRANSIT, 0L) +
                statusCounts.getOrDefault(DispatchStatus.DELIVERY_IN_PROGRESS, 0L));
        dashboard.put("deliveredDispatches", statusCounts.getOrDefault(DispatchStatus.DELIVERED, 0L));
        dashboard.put("cancelledDispatches", statusCounts.getOrDefault(DispatchStatus.CANCELLED, 0L));
        dashboard.put("failedDispatches", statusCounts.getOrDefault(DispatchStatus.FAILED, 0L));

        // Calculate average delivery time
        double avgDeliveryTime = allDispatches.stream()
                .filter(d -> d.getActualPickupTime() != null && d.getActualDeliveryTime() != null)
                .mapToLong(d -> java.time.Duration.between(d.getActualPickupTime(), d.getActualDeliveryTime()).toMinutes())
                .average()
                .orElse(0.0);
        dashboard.put("averageDeliveryTimeMinutes", avgDeliveryTime);

        // Calculate average distance
        double avgDistance = allDispatches.stream()
                .filter(d -> d.getDistanceMeters() != null)
                .mapToDouble(DispatchOrder::getDistanceMeters)
                .average()
                .orElse(0.0) / 1000.0;
        dashboard.put("averageDistanceKm", avgDistance);

        return dashboard;
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

    /**
     * Find nearby deliveries
     */
    public List<DispatchOrderDTO> findNearbyDeliveries(String tenantId, double latitude, double longitude, double maxDistanceKm) {
        log.info("Finding nearby deliveries for tenant: {} at location: ({}, {})", tenantId, latitude, longitude);

        GeoJsonPoint location = new GeoJsonPoint(longitude, latitude);
        double maxDistanceMeters = maxDistanceKm * 1000;

        List<DispatchOrder> nearbyDeliveries = dispatchOrderRepository.findNearbyDeliveries(tenantId, location, maxDistanceMeters);

        return dispatchOrderDtoMapper.toDTOList(nearbyDeliveries);
    }
}
