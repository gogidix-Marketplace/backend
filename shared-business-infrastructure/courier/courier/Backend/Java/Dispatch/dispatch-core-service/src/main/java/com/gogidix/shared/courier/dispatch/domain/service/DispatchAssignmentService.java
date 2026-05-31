package com.gogidix.shared.courier.dispatch.domain.service;

import com.gogidix.shared.courier.dispatch.domain.entity.DispatchOrder;
import com.gogidix.shared.courier.dispatch.domain.entity.DispatchStatus;
import com.gogidix.shared.courier.dispatch.domain.repository.DispatchOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Domain Service for Dispatch Assignment Logic
 * Contains business rules for matching dispatch orders to drivers
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DispatchAssignmentService {

    private final DispatchOrderRepository dispatchOrderRepository;

    /**
     * Find best available dispatch order for a driver
     * Considers: location, priority, time sensitivity
     */
    public Optional<DispatchOrder> findBestDispatchForDriver(
            String tenantId,
            GeoJsonPoint driverLocation,
            String requiredVehicleType,
            double searchRadiusKm) {

        log.info("Finding best dispatch for driver at location: ({}, {})",
                driverLocation.getY(), driverLocation.getX());

        double maxDistanceMeters = searchRadiusKm * 1000;

        // Find nearby pending dispatches
        List<DispatchOrder> nearbyDispatches = dispatchOrderRepository.findNearbyPickups(
                tenantId, driverLocation, maxDistanceMeters);

        // Filter by status (PENDING or CONFIRMED)
        nearbyDispatches = nearbyDispatches.stream()
                .filter(d -> d.getStatus() == DispatchStatus.PENDING ||
                             d.getStatus() == DispatchStatus.CONFIRMED)
                .filter(d -> d.getAssignedDriverId() == null)
                .toList();

        if (nearbyDispatches.isEmpty()) {
            log.debug("No available dispatches found for driver assignment");
            return Optional.empty();
        }

        // Score dispatches based on multiple factors
        DispatchOrder bestDispatch = nearbyDispatches.stream()
                .max(Comparator.comparingDouble(this::calculateDispatchScore))
                .orElse(nearbyDispatches.get(0));

        log.info("Best dispatch found: {} with priority: {}",
                bestDispatch.getDispatchId(), bestDispatch.getPriority());

        return Optional.of(bestDispatch);
    }

    /**
     * Calculate dispatch score for assignment
     * Factors: priority (40%), time urgency (30%), proximity (30%)
     */
    private double calculateDispatchScore(DispatchOrder dispatch) {
        // Priority score: higher priority (lower number) = higher score
        double priorityScore = (4 - dispatch.getPriority()) * 40.0;

        // Time urgency score
        double timeScore = 30.0;
        if (dispatch.getEstimatedPickupTime() != null) {
            long minutesUntilPickup = java.time.Duration.between(
                    java.time.LocalDateTime.now(),
                    dispatch.getEstimatedPickupTime()
            ).toMinutes();

            if (minutesUntilPickup < 30) {
                timeScore = 30.0; // Urgent
            } else if (minutesUntilPickup < 120) {
                timeScore = 20.0; // Medium urgency
            } else {
                timeScore = 10.0; // Less urgent
            }
        }

        // Proximity score would be calculated based on distance - placeholder for now
        double proximityScore = 30.0;

        return priorityScore + timeScore + proximityScore;
    }

    /**
     * Check if dispatch is eligible for assignment
     */
    public boolean isDispatchEligible(DispatchOrder dispatch) {
        if (dispatch.getAssignedDriverId() != null) {
            log.debug("Dispatch {} not eligible: already assigned", dispatch.getDispatchId());
            return false;
        }

        if (dispatch.getStatus() != DispatchStatus.PENDING &&
            dispatch.getStatus() != DispatchStatus.CONFIRMED) {
            log.debug("Dispatch {} not eligible: status is {}",
                    dispatch.getDispatchId(), dispatch.getStatus());
            return false;
        }

        return true;
    }

    /**
     * Reserve dispatch for driver assignment
     * Changes status to ASSIGNED to prevent double-assignment
     */
    @Transactional
    public DispatchOrder reserveDispatch(DispatchOrder dispatch, String driverId, String vehicleId) {
        log.info("Reserving dispatch {} for driver: {}", dispatch.getDispatchId(), driverId);

        dispatch.setAssignedDriverId(driverId);
        dispatch.setAssignedVehicleId(vehicleId);
        dispatch.setStatus(DispatchStatus.ASSIGNED);

        return dispatchOrderRepository.save(dispatch);
    }

    /**
     * Release dispatch from driver
     * Changes status back to CONFIRMED
     */
    @Transactional
    public DispatchOrder releaseDispatch(DispatchOrder dispatch) {
        log.info("Releasing dispatch {} from driver: {}", dispatch.getDispatchId(), dispatch.getAssignedDriverId());

        dispatch.setAssignedDriverId(null);
        dispatch.setAssignedVehicleId(null);
        dispatch.setStatus(DispatchStatus.CONFIRMED);

        return dispatchOrderRepository.save(dispatch);
    }
}
