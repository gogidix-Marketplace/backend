package com.gogidix.shared.courier.driver.domain.service;

import com.gogidix.shared.courier.driver.domain.entity.DriverProfile;
import com.gogidix.shared.courier.driver.domain.entity.DriverStatus;
import com.gogidix.shared.courier.driver.domain.repository.DriverProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Domain Service for driver assignment logic
 * Contains business rules for matching drivers to dispatch orders
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DriverAssignmentService {

    private final DriverProfileRepository driverProfileRepository;

    /**
     * Find best available driver for a dispatch order
     * Considers: location, rating, vehicle type, current status
     */
    public Optional<DriverProfile> findBestDriver(
            String tenantId,
            GeoJsonPoint pickupLocation,
            String requiredVehicleType,
            double searchRadiusKm) {

        log.info("Finding best driver for tenant: {} at location: ({}, {})",
                tenantId, pickupLocation.getY(), pickupLocation.getX());

        double maxDistanceMeters = searchRadiusKm * 1000;

        // Find nearby available drivers with required vehicle type
        List<DriverProfile> nearbyDrivers = driverProfileRepository.findNearbyAvailableDrivers(
                tenantId, DriverStatus.ONLINE, pickupLocation, maxDistanceMeters);

        // Filter by vehicle type if specified
        if (requiredVehicleType != null && !requiredVehicleType.isEmpty()) {
            nearbyDrivers = nearbyDrivers.stream()
                    .filter(d -> requiredVehicleType.equals(d.getVehicleType()))
                    .toList();
        }

        if (nearbyDrivers.isEmpty()) {
            log.warn("No available drivers found for dispatch");
            return Optional.empty();
        }

        // Score drivers based on multiple factors
        DriverProfile bestDriver = nearbyDrivers.stream()
                .max(Comparator.comparingDouble(this::calculateDriverScore))
                .orElse(nearbyDrivers.get(0));

        log.info("Best driver found: {} with rating: {}",
                bestDriver.getDriverId(), bestDriver.getRating());

        return Optional.of(bestDriver);
    }

    /**
     * Calculate driver score for assignment
     * Factors: rating (40%), recent deliveries (30%), location proximity (30%)
     */
    private double calculateDriverScore(DriverProfile driver) {
        double ratingScore = (driver.getRating() / 5.0) * 40.0; // Max 40 points

        double deliveriesScore = Math.min(driver.getTotalDeliveries() / 100.0, 1.0) * 30.0; // Max 30 points

        // Proximity score would be calculated based on distance - placeholder for now
        double proximityScore = 30.0; // Max 30 points

        return ratingScore + deliveriesScore + proximityScore;
    }

    /**
     * Check if driver is eligible for assignment
     */
    public boolean isDriverEligible(DriverProfile driver, String requiredVehicleType) {
        if (driver.getStatus() != DriverStatus.ONLINE) {
            log.debug("Driver {} not eligible: status is {}",
                    driver.getDriverId(), driver.getStatus());
            return false;
        }

        if (requiredVehicleType != null && !requiredVehicleType.equals(driver.getVehicleType())) {
            log.debug("Driver {} not eligible: vehicle type mismatch", driver.getDriverId());
            return false;
        }

        // Check if driver has minimum rating threshold
        if (driver.getRating() < 3.0) {
            log.debug("Driver {} not eligible: rating below threshold", driver.getDriverId());
            return false;
        }

        return true;
    }

    /**
     * Reserve driver for assignment
     * Changes status to BUSY to prevent double-assignment
     */
    public DriverProfile reserveDriver(DriverProfile driver) {
        log.info("Reserving driver: {}", driver.getDriverId());
        driver.setStatus(DriverStatus.BUSY);
        return driverProfileRepository.save(driver);
    }

    /**
     * Release driver after assignment completion
     * Changes status back to ONLINE
     */
    public DriverProfile releaseDriver(DriverProfile driver) {
        log.info("Releasing driver: {}", driver.getDriverId());
        driver.setStatus(DriverStatus.ONLINE);
        return driverProfileRepository.save(driver);
    }
}
