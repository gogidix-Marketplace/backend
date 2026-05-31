package com.gogidix.shared.courier.dispatch.application.service;

import com.gogidix.shared.courier.dispatch.domain.entity.DispatchOrder;
import com.gogidix.shared.courier.dispatch.domain.entity.DispatchStatus;
import com.gogidix.shared.courier.dispatch.domain.repository.DispatchOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing Dispatch Orders with geospatial capabilities
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DispatchOrderService {

    private final DispatchOrderRepository dispatchOrderRepository;

    @Transactional
    public DispatchOrder createDispatchOrder(DispatchOrder dispatchOrder) {
        log.info("Creating dispatch order for tenant: {}", dispatchOrder.getTenantId());

        if (dispatchOrderRepository.existsByTenantIdAndDispatchId(
                dispatchOrder.getTenantId(), dispatchOrder.getDispatchId())) {
            throw new IllegalArgumentException(
                    "Dispatch order with ID " + dispatchOrder.getDispatchId() + " already exists");
        }

        if (dispatchOrder.getStatus() == null) {
            dispatchOrder.setStatus(DispatchStatus.PENDING);
        }

        DispatchOrder savedOrder = dispatchOrderRepository.save(dispatchOrder);
        log.info("Successfully created dispatch order: {}", savedOrder.getDispatchId());
        return savedOrder;
    }

    @Transactional(readOnly = true)
    public List<DispatchOrder> getAllDispatchOrders(String tenantId) {
        log.debug("Fetching all dispatch orders for tenant: {}", tenantId);
        return dispatchOrderRepository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public Optional<DispatchOrder> getDispatchOrder(String tenantId, String dispatchId) {
        log.debug("Fetching dispatch order: {} for tenant: {}", dispatchId, tenantId);
        return dispatchOrderRepository.findByTenantIdAndDispatchId(tenantId, dispatchId);
    }

    @Transactional
    public DispatchOrder updateDispatchOrder(String tenantId, String dispatchId, DispatchOrder update) {
        log.info("Updating dispatch order: {} for tenant: {}", dispatchId, tenantId);

        DispatchOrder existingOrder = dispatchOrderRepository.findByTenantIdAndDispatchId(tenantId, dispatchId)
                .orElseThrow(() -> new IllegalArgumentException("Dispatch order not found: " + dispatchId));

        if (update.getPickupLocation() != null) {
            existingOrder.setPickupLocation(update.getPickupLocation());
        }
        if (update.getDeliveryLocation() != null) {
            existingOrder.setDeliveryLocation(update.getDeliveryLocation());
        }
        if (update.getPickupAddress() != null) {
            existingOrder.setPickupAddress(update.getPickupAddress());
        }
        if (update.getDeliveryAddress() != null) {
            existingOrder.setDeliveryAddress(update.getDeliveryAddress());
        }
        if (update.getStatus() != null) {
            existingOrder.setStatus(update.getStatus());
        }
        if (update.getPriority() != null) {
            existingOrder.setPriority(update.getPriority());
        }
        if (update.getAssignedDriverId() != null) {
            existingOrder.setAssignedDriverId(update.getAssignedDriverId());
        }
        if (update.getAssignedVehicleId() != null) {
            existingOrder.setAssignedVehicleId(update.getAssignedVehicleId());
        }
        if (update.getEstimatedPickupTime() != null) {
            existingOrder.setEstimatedPickupTime(update.getEstimatedPickupTime());
        }
        if (update.getEstimatedDeliveryTime() != null) {
            existingOrder.setEstimatedDeliveryTime(update.getEstimatedDeliveryTime());
        }
        if (update.getActualPickupTime() != null) {
            existingOrder.setActualPickupTime(update.getActualPickupTime());
        }
        if (update.getActualDeliveryTime() != null) {
            existingOrder.setActualDeliveryTime(update.getActualDeliveryTime());
        }
        if (update.getTotalAmount() != null) {
            existingOrder.setTotalAmount(update.getTotalAmount());
        }

        DispatchOrder savedOrder = dispatchOrderRepository.save(existingOrder);
        log.info("Successfully updated dispatch order: {}", savedOrder.getDispatchId());
        return savedOrder;
    }

    @Transactional
    public void deleteDispatchOrder(String tenantId, String dispatchId) {
        log.info("Deleting dispatch order: {} for tenant: {}", dispatchId, tenantId);

        if (!dispatchOrderRepository.existsByTenantIdAndDispatchId(tenantId, dispatchId)) {
            throw new IllegalArgumentException("Dispatch order not found: " + dispatchId);
        }

        dispatchOrderRepository.deleteByTenantIdAndDispatchId(tenantId, dispatchId);
        log.info("Successfully deleted dispatch order: {}", dispatchId);
    }

    @Transactional(readOnly = true)
    public List<DispatchOrder> getDispatchOrdersByStatus(String tenantId, DispatchStatus status) {
        log.debug("Fetching dispatch orders by status: {} for tenant: {}", status, tenantId);
        return dispatchOrderRepository.findByTenantIdAndStatus(tenantId, status);
    }

    @Transactional(readOnly = true)
    public List<DispatchOrder> getDriverDispatchOrders(String tenantId, String driverId) {
        log.debug("Fetching dispatch orders for driver: {} in tenant: {}", driverId, tenantId);
        return dispatchOrderRepository.findByTenantIdAndAssignedDriverId(tenantId, driverId);
    }

    // GEOSPATIAL METHODS
    @Transactional(readOnly = true)
    public List<DispatchOrder> findNearbyPickups(String tenantId, double longitude, double latitude, double maxDistanceMeters) {
        log.debug("Finding nearby pickups for tenant: {} at location: {}, {}", tenantId, longitude, latitude);
        GeoJsonPoint location = new GeoJsonPoint(longitude, latitude);
        return dispatchOrderRepository.findNearbyPickups(tenantId, location, maxDistanceMeters);
    }

    @Transactional(readOnly = true)
    public List<DispatchOrder> findNearbyDeliveries(String tenantId, double longitude, double latitude, double maxDistanceMeters) {
        log.debug("Finding nearby deliveries for tenant: {} at location: {}, {}", tenantId, longitude, latitude);
        GeoJsonPoint location = new GeoJsonPoint(longitude, latitude);
        return dispatchOrderRepository.findNearbyDeliveries(tenantId, location, maxDistanceMeters);
    }

    @Transactional(readOnly = true)
    public List<DispatchOrder> findNearbyPickupsByStatus(
            String tenantId, DispatchStatus status, double longitude, double latitude, double maxDistanceMeters) {
        log.debug("Finding nearby pickups by status: {} for tenant: {}", status, tenantId);
        GeoJsonPoint location = new GeoJsonPoint(longitude, latitude);
        return dispatchOrderRepository.findNearbyPickupsByStatus(tenantId, status, location, maxDistanceMeters);
    }

    @Transactional(readOnly = true)
    public List<DispatchOrder> getDispatchOrdersByTimeRange(
            String tenantId, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Fetching dispatch orders by time range for tenant: {}", tenantId);
        return dispatchOrderRepository.findByTenantIdAndEstimatedPickupTimeBetween(tenantId, startTime, endTime);
    }

    @Transactional(readOnly = true)
    public List<DispatchOrder> getHighPriorityDispatchOrders(String tenantId, Integer minPriority) {
        log.debug("Fetching high priority dispatch orders for tenant: {}", tenantId);
        return dispatchOrderRepository.findByTenantIdAndPriorityGreaterThanEqualOrderByPriorityDesc(
                tenantId, minPriority);
    }
}
