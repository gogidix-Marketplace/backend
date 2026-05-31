package com.gogidix.shared.courier.driver.application.service;

import com.gogidix.shared.courier.driver.domain.entity.DriverProfile;
import com.gogidix.shared.courier.driver.domain.entity.DriverStatus;
import com.gogidix.shared.courier.driver.domain.repository.DriverProfileRepository;
import com.gogidix.shared.courier.driver.interfaces.rest.dto.CreateDriverRequest;
import com.gogidix.shared.courier.driver.interfaces.rest.dto.DriverResponse;
import com.gogidix.shared.courier.driver.interfaces.rest.dto.UpdateDriverLocationRequest;
import com.gogidix.shared.courier.driver.interfaces.rest.dto.UpdateDriverStatusRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverPoolService {

    private final DriverProfileRepository driverProfileRepository;

    @Transactional
    public DriverResponse createDriver(String tenantId, CreateDriverRequest request) {
        log.info("Creating driver for tenant: {}", tenantId);

        DriverProfile driver = DriverProfile.builder()
                .tenantId(tenantId)
                .driverId(request.getDriverId())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .status(DriverStatus.OFFLINE)
                .vehicleType(request.getVehicleType())
                .licensePlate(request.getLicensePlate())
                .rating(0.0)
                .totalDeliveries(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        DriverProfile saved = driverProfileRepository.save(driver);
        log.info("Driver created with ID: {}", saved.getId());

        return mapToResponse(saved);
    }

    @Transactional
    public DriverResponse updateDriverLocation(String tenantId, String driverId, UpdateDriverLocationRequest request) {
        log.info("Updating location for driver: {} in tenant: {}", driverId, tenantId);

        DriverProfile driver = driverProfileRepository.findByTenantIdAndDriverId(tenantId, driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        GeoJsonPoint location = new GeoJsonPoint(request.getLongitude(), request.getLatitude());
        driver.setCurrentLocation(location);
        driver.setUpdatedAt(LocalDateTime.now());

        DriverProfile updated = driverProfileRepository.save(driver);
        log.info("Driver location updated: {}", location);

        return mapToResponse(updated);
    }

    @Transactional
    public DriverResponse updateDriverStatus(String tenantId, String driverId, UpdateDriverStatusRequest request) {
        log.info("Updating status for driver: {} in tenant: {}", driverId, tenantId);

        DriverProfile driver = driverProfileRepository.findByTenantIdAndDriverId(tenantId, driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        driver.setStatus(DriverStatus.valueOf(request.getStatus()));
        driver.setUpdatedAt(LocalDateTime.now());

        DriverProfile updated = driverProfileRepository.save(driver);
        log.info("Driver status updated to: {}", request.getStatus());

        return mapToResponse(updated);
    }

    public List<DriverResponse> findNearbyDrivers(String tenantId, double latitude, double longitude, double radiusKm) {
        log.info("Finding nearby drivers for tenant: {} at location: ({}, {}) within {}km",
                tenantId, latitude, longitude, radiusKm);

        GeoJsonPoint location = new GeoJsonPoint(longitude, latitude);
        double maxDistanceMeters = radiusKm * 1000;

        List<DriverProfile> drivers = driverProfileRepository.findNearbyAvailableDrivers(
                tenantId, DriverStatus.ONLINE, location, maxDistanceMeters);

        log.info("Found {} nearby drivers", drivers.size());
        return drivers.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<DriverResponse> findDriversByStatus(String tenantId, String status) {
        log.info("Finding drivers by status: {} for tenant: {}", status, tenantId);

        DriverStatus driverStatus = DriverStatus.valueOf(status);
        List<DriverProfile> drivers = driverProfileRepository.findByTenantIdAndStatus(tenantId, driverStatus);

        return drivers.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DriverResponse getDriver(String tenantId, String driverId) {
        log.info("Getting driver: {} for tenant: {}", driverId, tenantId);

        DriverProfile driver = driverProfileRepository.findByTenantIdAndDriverId(tenantId, driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        return mapToResponse(driver);
    }

    @Transactional
    public void updateDriverRating(String tenantId, String driverId, Double rating) {
        log.info("Updating rating for driver: {} in tenant: {}", driverId, tenantId);

        DriverProfile driver = driverProfileRepository.findByTenantIdAndDriverId(tenantId, driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        // Calculate new average rating
        int currentDeliveries = driver.getTotalDeliveries();
        double currentRating = driver.getRating();
        double newRating = ((currentRating * currentDeliveries) + rating) / (currentDeliveries + 1);

        driver.setRating(newRating);
        driver.setTotalDeliveries(currentDeliveries + 1);
        driver.setUpdatedAt(LocalDateTime.now());

        driverProfileRepository.save(driver);
        log.info("Driver rating updated: {}", newRating);
    }

    @Transactional
    public void deleteDriver(String tenantId, String driverId) {
        log.info("Deleting driver: {} for tenant: {}", driverId, tenantId);

        DriverProfile driver = driverProfileRepository.findByTenantIdAndDriverId(tenantId, driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        driverProfileRepository.delete(driver);
        log.info("Driver deleted: {}", driverId);
    }

    private DriverResponse mapToResponse(DriverProfile driver) {
        return DriverResponse.builder()
                .id(driver.getId())
                .tenantId(driver.getTenantId())
                .driverId(driver.getDriverId())
                .fullName(driver.getFullName())
                .email(driver.getEmail())
                .phone(driver.getPhone())
                .status(driver.getStatus().name())
                .currentLocation(driver.getCurrentLocation() != null ?
                        List.of(driver.getCurrentLocation().getX(), driver.getCurrentLocation().getY()) : null)
                .vehicleType(driver.getVehicleType())
                .licensePlate(driver.getLicensePlate())
                .rating(driver.getRating())
                .totalDeliveries(driver.getTotalDeliveries())
                .createdAt(driver.getCreatedAt())
                .updatedAt(driver.getUpdatedAt())
                .build();
    }
}
