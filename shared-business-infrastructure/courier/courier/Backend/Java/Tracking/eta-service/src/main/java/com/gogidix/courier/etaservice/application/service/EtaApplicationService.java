package com.gogidix.courier.etaservice.application.service;

import com.gogidix.courier.etaservice.application.command.*;
import com.gogidix.courier.etaservice.application.dto.*;
import com.gogidix.courier.etaservice.application.mapper.EtaMapper;
import com.gogidix.courier.etaservice.application.query.EtaQuery;
import com.gogidix.courier.etaservice.domain.entity.EtaCalculation;
import com.gogidix.courier.etaservice.domain.entity.EtaHistory;
import com.gogidix.courier.etaservice.domain.entity.TrafficFactor;
import com.gogidix.courier.etaservice.domain.event.EtaCalculatedEvent;
import com.gogidix.courier.etaservice.domain.event.EtaDeliveredEvent;
import com.gogidix.courier.etaservice.domain.event.EtaUpdatedEvent;
import com.gogidix.courier.etaservice.domain.repository.EtaCalculationRepository;
import com.gogidix.courier.etaservice.domain.repository.EtaHistoryRepository;
import com.gogidix.courier.etaservice.domain.repository.TrafficFactorRepository;
import com.gogidix.courier.etaservice.shared.context.RequestContext;
import com.gogidix.courier.etaservice.shared.exception.EtaCalculationException;
import com.gogidix.courier.etaservice.shared.exception.InvalidLocationException;
import com.gogidix.courier.etaservice.shared.exception.NotFoundException;
import com.gogidix.courier.etaservice.shared.exception.ValidationException;
import com.gogidix.courier.etaservice.shared.util.DistanceCalculator;
import com.gogidix.courier.etaservice.shared.util.TrafficLevel;
import com.gogidix.courier.etaservice.shared.util.VehicleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Application service for ETA operations with traffic-aware algorithms.
 */
@Service
@Transactional(readOnly = true)
public class EtaApplicationService implements EtaQuery {

    private static final Logger log = LoggerFactory.getLogger(EtaApplicationService.class);
    private static final String CACHE_NAME = "etaCalculations";

    // Service time in minutes
    private static final double PICKUP_SERVICE_TIME = 5.0;
    private static final double DELIVERY_SERVICE_TIME = 3.0;

    private final EtaCalculationRepository etaCalculationRepository;
    private final EtaHistoryRepository etaHistoryRepository;
    private final TrafficFactorRepository trafficFactorRepository;
    private final EtaMapper etaMapper;
    private final EtaEventPublisher eventPublisher;

    // Base speeds in km/h by vehicle type
    private final Map<String, Double> baseSpeeds = Map.of(
            VehicleType.WALKING.getCode(), 5.0,
            VehicleType.BIKE.getCode(), 25.0,
            VehicleType.MOTORCYCLE.getCode(), 35.0,
            VehicleType.CAR.getCode(), 40.0,
            VehicleType.TRUCK.getCode(), 30.0
    );

    public EtaApplicationService(
            EtaCalculationRepository etaCalculationRepository,
            EtaHistoryRepository etaHistoryRepository,
            TrafficFactorRepository trafficFactorRepository,
            EtaMapper etaMapper,
            EtaEventPublisher eventPublisher) {
        this.etaCalculationRepository = etaCalculationRepository;
        this.etaHistoryRepository = etaHistoryRepository;
        this.trafficFactorRepository = trafficFactorRepository;
        this.etaMapper = etaMapper;
        this.eventPublisher = eventPublisher;
    }

    // ========================================================================
    // ETA Calculation Operations
    // ========================================================================

    /**
     * Calculate ETA for a dispatch.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public EtaResponse calculateEta(EtaRequest request, RequestContext context) {
        log.info("Calculating ETA for dispatch: {}", request.dispatchId());

        // Validate request
        validateEtaRequest(request);

        // Check if ETA already exists
        Optional<EtaCalculation> existing = etaCalculationRepository
                .findByDispatchIdAndTenantId(request.dispatchId(), request.tenantId());

        EtaCalculation calculation;
        Integer previousEta = null;

        if (existing.isPresent()) {
            calculation = existing.get();
            previousEta = calculation.getEtaMinutes();
            calculation.updateCurrentLocation(etaMapper.toLocationEntity(request.pickupLocation()));
        } else {
            calculation = etaMapper.toEntity(request);
            calculation.validate();
        }

        // Perform traffic-aware ETA calculation
        EtaCalculationResult result = performTrafficAwareCalculation(
                calculation.getPickupLocation(),
                calculation.getDropoffLocation(),
                calculation.getCurrentLocation(),
                request.vehicleType(),
                request.tenantId()
        );

        // Update calculation with results
        calculation.updateEta(
                result.etaMinutes(),
                result.distanceKm(),
                result.trafficLevel(),
                result.trafficMultiplier(),
                result.confidenceScore()
        );

        EtaCalculation saved = etaCalculationRepository.save(calculation);

        // Create history entry
        createHistoryEntry(saved, previousEta, EtaHistory.ChangeType.INITIAL_CALCULATION,
                "Initial ETA calculation", context);

        // Publish event
        if (previousEta == null) {
            eventPublisher.publish(new EtaCalculatedEvent(
                    saved.getDispatchId(),
                    saved.getTenantId(),
                    saved.getEtaMinutes(),
                    saved.getDistanceKm(),
                    saved.getTrafficLevel(),
                    saved.getTrafficMultiplier(),
                    saved.getConfidenceScore(),
                    saved.getVehicleType(),
                    context.correlationId()
            ));
        } else {
            publishUpdateEvent(saved, previousEta, "Location update", context);
        }

        log.info("ETA calculated for dispatch {}: {} minutes", saved.getDispatchId(), saved.getEtaMinutes());
        return etaMapper.toResponseDto(saved);
    }

    /**
     * Recalculate ETA for a dispatch.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public EtaResponse recalculateEta(String dispatchId, String tenantId,
                                       RecalculateEtaRequest request,
                                       RequestContext context) {
        log.info("Recalculating ETA for dispatch: {}", dispatchId);

        EtaCalculation calculation = etaCalculationRepository
                .findByDispatchIdAndTenantId(dispatchId, tenantId)
                .orElseThrow(() -> new NotFoundException("EtaCalculation", dispatchId));

        if (!calculation.canRecalculate() && !Boolean.TRUE.equals(request.forceRecalculation())) {
            throw new ValidationException("ETA cannot be recalculated in current state: " + calculation.getStatus());
        }

        Integer previousEta = calculation.getEtaMinutes();

        // Update current location if provided
        if (request.currentLocation() != null) {
            EtaCalculation.Location newLocation = etaMapper.toLocationEntity(request.currentLocation());
            calculation.updateCurrentLocation(newLocation);
        }

        // Perform new calculation
        EtaCalculationResult result = performTrafficAwareCalculation(
                calculation.getPickupLocation(),
                calculation.getDropoffLocation(),
                calculation.getCurrentLocation(),
                calculation.getVehicleType(),
                tenantId
        );

        // Update calculation
        calculation.updateEta(
                result.etaMinutes(),
                result.distanceKm(),
                result.trafficLevel(),
                result.trafficMultiplier(),
                result.confidenceScore()
        );
        calculation.markAsRecalculated();

        EtaCalculation saved = etaCalculationRepository.save(calculation);

        // Create history entry
        createHistoryEntry(saved, previousEta, EtaHistory.ChangeType.RECALCULATION,
                request.reason() != null ? request.reason() : "Manual recalculation", context);

        // Publish update event
        publishUpdateEvent(saved, previousEta, request.reason(), context);

        log.info("ETA recalculated for dispatch {}: {} minutes (was: {})",
                saved.getDispatchId(), saved.getEtaMinutes(), previousEta);
        return etaMapper.toResponseDto(saved);
    }

    /**
     * Batch calculate ETA for multiple dispatches.
     */
    @Transactional
    public BatchEtaResponse batchCalculateEta(BatchCalculateEtaCommand command, RequestContext context) {
        log.info("Batch calculating ETA for {} dispatches", command.requests().size());

        List<BatchEtaResponse.ResultItem> results = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        for (CalculateEtaCommand cmd : command.requests()) {
            try {
                EtaRequest request = new EtaRequest(
                        cmd.dispatchId(),
                        cmd.tenantId(),
                        toLocationDto(cmd.pickupLocation()),
                        toLocationDto(cmd.dropoffLocation()),
                        cmd.vehicleType(),
                        context.userId()
                );

                EtaResponse response = calculateEta(request, context);
                results.add(etaMapper.toSuccessResultItem(cmd.dispatchId(), response));
                successCount++;
            } catch (Exception e) {
                log.error("Failed to calculate ETA for dispatch: {}", cmd.dispatchId(), e);
                results.add(etaMapper.toFailureResultItem(cmd.dispatchId(), e.getMessage()));
                failureCount++;
            }
        }

        return new BatchEtaResponse(command.requests().size(), successCount, failureCount, results);
    }

    // ========================================================================
    // Query Operations (Implementation of EtaQuery)
    // ========================================================================

    @Override
    @Cacheable(value = CACHE_NAME, key = "'dispatch:' + #dispatchId")
    public EtaCalculation getByDispatchId(String dispatchId) {
        return etaCalculationRepository.findByDispatchId(dispatchId)
                .orElseThrow(() -> new NotFoundException("EtaCalculation", dispatchId));
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'dispatch:' + #dispatchId + ':' + #tenantId")
    public EtaCalculation getByDispatchIdAndTenantId(String dispatchId, String tenantId) {
        return etaCalculationRepository.findByDispatchIdAndTenantId(dispatchId, tenantId)
                .orElseThrow(() -> new NotFoundException("EtaCalculation", dispatchId));
    }

    @Override
    public List<EtaHistoryDto> getHistory(String dispatchId, String tenantId) {
        List<EtaHistory> history = etaHistoryRepository
                .findByDispatchIdAndTenantId(dispatchId, tenantId);

        return history.stream()
                .map(h -> new EtaHistoryDto(
                        h.getId(),
                        h.getDispatchId(),
                        h.getTimestamp(),
                        h.getEtaMinutes(),
                        h.getPreviousEtaMinutes(),
                        h.getEtaChangeMinutes(),
                        h.getDistanceKm(),
                        h.getTrafficLevel(),
                        h.getChangeReason(),
                        h.getChangeType() != null ? h.getChangeType().name() : null
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<EtaCalculation> getActiveByTenantId(String tenantId) {
        return etaCalculationRepository.findActiveByTenantId(tenantId);
    }

    @Override
    public List<EtaCalculation> getArrivingSoon(String tenantId, int withinMinutes) {
        Instant threshold = Instant.now().plusSeconds(withinMinutes * 60L);
        return etaCalculationRepository.findByEstimatedArrivalBeforeAndTenantId(threshold, tenantId)
                .stream()
                .filter(e -> e.getStatus() == EtaCalculation.EtaStatus.IN_TRANSIT
                        || e.getStatus() == EtaCalculation.EtaStatus.CALCULATED)
                .collect(Collectors.toList());
    }

    @Override
    public List<EtaCalculation> getByStatus(String tenantId, EtaCalculation.EtaStatus status) {
        return etaCalculationRepository.findByStatusAndTenantId(status, tenantId);
    }

    @Override
    public EtaStatisticsDto getStatistics(String tenantId, Instant fromDate, Instant toDate) {
        List<EtaCalculation> all = etaCalculationRepository.findByTenantId(tenantId);

        // Filter by date range if provided
        List<EtaCalculation> filtered = all;
        if (fromDate != null || toDate != null) {
            filtered = all.stream()
                    .filter(e -> (fromDate == null || !e.getCreatedAt().isBefore(fromDate))
                            && (toDate == null || !e.getCreatedAt().isAfter(toDate)))
                    .collect(Collectors.toList());
        }

        long total = filtered.size();
        long active = filtered.stream().filter(e -> e.isActive()).count();
        long completed = filtered.stream().filter(e -> e.getStatus() == EtaCalculation.EtaStatus.DELIVERED).count();

        DoubleSummaryStatistics etaStats = filtered.stream()
                .filter(e -> e.getEtaMinutes() != null)
                .mapToDouble(EtaCalculation::getEtaMinutes)
                .summaryStatistics();

        // Count by traffic level
        Map<String, Long> trafficCounts = filtered.stream()
                .filter(e -> e.getTrafficLevel() != null)
                .collect(Collectors.groupingBy(EtaCalculation::getTrafficLevel, Collectors.counting()));

        String mostCommonTrafficLevel = trafficCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("UNKNOWN");

        // For demo purposes, calculate accuracy from completed deliveries
        // In a real system, this would come from actual vs. estimated comparison
        long onTime = (long) (completed * 0.85); // Demo: 85% on-time
        long delayed = (long) (completed * 0.12); // Demo: 12% delayed
        long early = (long) (completed * 0.03); // Demo: 3% early

        return new EtaStatisticsDto(
                total,
                active,
                completed,
                etaStats.getCount() > 0 ? etaStats.getAverage() : 0.0,
                85.0, // Demo accuracy
                onTime,
                delayed,
                early,
                mostCommonTrafficLevel
        );
    }

    // ========================================================================
    // State Management Operations
    // ========================================================================

    /**
     * Mark dispatch as in-transit.
     */
    @Transactional
    public EtaResponse markAsInTransit(String dispatchId, String tenantId) {
        EtaCalculation calculation = etaCalculationRepository
                .findByDispatchIdAndTenantId(dispatchId, tenantId)
                .orElseThrow(() -> new NotFoundException("EtaCalculation", dispatchId));

        calculation.markAsInTransit();
        EtaCalculation saved = etaCalculationRepository.save(calculation);

        log.info("Dispatch {} marked as in-transit", dispatchId);
        return etaMapper.toResponseDto(saved);
    }

    /**
     * Mark dispatch as delivered.
     */
    @Transactional
    public EtaResponse markAsDelivered(String dispatchId, String tenantId, RequestContext context) {
        EtaCalculation calculation = etaCalculationRepository
                .findByDispatchIdAndTenantId(dispatchId, tenantId)
                .orElseThrow(() -> new NotFoundException("EtaCalculation", dispatchId));

        Integer estimatedEta = calculation.getEtaMinutes();
        calculation.markAsDelivered();
        EtaCalculation saved = etaCalculationRepository.save(calculation);

        // Create history entry
        createHistoryEntry(saved, estimatedEta, EtaHistory.ChangeType.DELIVERED,
                "Delivery completed", context);

        // Publish delivered event
        int actualEta = estimatedEta != null ? estimatedEta : 0; // In real system, calculate from timestamps
        eventPublisher.publish(new EtaDeliveredEvent(
                saved.getDispatchId(),
                saved.getTenantId(),
                saved.getEstimatedArrival(),
                Instant.now(),
                estimatedEta,
                actualEta,
                context.correlationId()
        ));

        log.info("Dispatch {} marked as delivered", dispatchId);
        return etaMapper.toResponseDto(saved);
    }

    /**
     * Mark dispatch as cancelled.
     */
    @Transactional
    public EtaResponse markAsCancelled(String dispatchId, String tenantId, String reason, RequestContext context) {
        EtaCalculation calculation = etaCalculationRepository
                .findByDispatchIdAndTenantId(dispatchId, tenantId)
                .orElseThrow(() -> new NotFoundException("EtaCalculation", dispatchId));

        calculation.markAsCancelled();
        EtaCalculation saved = etaCalculationRepository.save(calculation);

        // Create history entry
        createHistoryEntry(saved, saved.getEtaMinutes(), EtaHistory.ChangeType.CANCELLED,
                reason != null ? reason : "Dispatch cancelled", context);

        log.info("Dispatch {} marked as cancelled", dispatchId);
        return etaMapper.toResponseDto(saved);
    }

    // ========================================================================
    // Traffic-Aware ETA Calculation Algorithm
    // ========================================================================

    /**
     * Perform traffic-aware ETA calculation.
     * This is the core algorithm that considers:
     * - Distance between locations
     * - Vehicle type and base speed
     * - Current traffic conditions
     * - Time of day (peak hours)
     * - Historical traffic patterns
     */
    private EtaCalculationResult performTrafficAwareCalculation(
            EtaCalculation.Location pickup,
            EtaCalculation.Location dropoff,
            EtaCalculation.Location current,
            String vehicleType,
            String tenantId) {

        try {
            // Calculate distance
            double distanceKm;
            if (current != null && isInRoute(current, pickup, dropoff)) {
                // Calculate remaining distance from current location
                distanceKm = DistanceCalculator.calculateDistance(
                        current.getLatitude(), current.getLongitude(),
                        dropoff.getLatitude(), dropoff.getLongitude()
                );
            } else {
                // Calculate full route distance
                distanceKm = DistanceCalculator.calculateDistance(
                        pickup.getLatitude(), pickup.getLongitude(),
                        dropoff.getLatitude(), dropoff.getLongitude()
                );
            }

            // Get base speed for vehicle type
            double baseSpeedKmh = baseSpeeds.getOrDefault(vehicleType, 40.0);

            // Get traffic multiplier
            double trafficMultiplier = getTrafficMultiplier(pickup, dropoff, tenantId);
            String trafficLevel = TrafficLevel.fromMultiplier(trafficMultiplier).name();

            // Apply peak hour multiplier if applicable
            LocalDateTime now = LocalDateTime.now();
            boolean isPeakHour = isPeakHour(now);
            if (isPeakHour) {
                trafficMultiplier *= 1.4; // Peak hour increases travel time by 40%
            }

            // Calculate effective speed
            double effectiveSpeedKmh = baseSpeedKmh / trafficMultiplier;

            // Calculate travel time in minutes
            double travelTimeMinutes = (distanceKm / effectiveSpeedKmh) * 60;

            // Add service time
            double totalTimeMinutes = travelTimeMinutes + PICKUP_SERVICE_TIME + DELIVERY_SERVICE_TIME;

            // Calculate confidence score
            double confidenceScore = calculateConfidenceScore(trafficMultiplier, distanceKm, isPeakHour);

            // Ensure ETA is within reasonable bounds
            int etaMinutes = (int) Math.round(Math.max(1, Math.min(480, totalTimeMinutes)));

            return new EtaCalculationResult(
                    etaMinutes,
                    Math.round(distanceKm * 100.0) / 100.0,
                    trafficLevel,
                    Math.round(trafficMultiplier * 100.0) / 100.0,
                    Math.round(confidenceScore * 100.0) / 100.0
            );

        } catch (Exception e) {
            log.error("Error performing traffic-aware calculation", e);
            throw new EtaCalculationException("Failed to calculate ETA", pickup.toString(), e);
        }
    }

    /**
     * Get traffic multiplier for the route.
     * Considers current time, day of week, and location.
     */
    private double getTrafficMultiplier(EtaCalculation.Location pickup,
                                         EtaCalculation.Location dropoff,
                                         String tenantId) {
        LocalDateTime now = LocalDateTime.now();
        int dayOfWeek = now.getDayOfWeek().getValue();
        int hourOfDay = now.getHour();

        // Try to find traffic factors for pickup area
        String areaCode = generateAreaCode(pickup);

        Optional<TrafficFactor> factorOpt = trafficFactorRepository
                .findByAreaCodeAndTenantIdAndDayOfWeekAndHourOfDay(
                        areaCode, tenantId, dayOfWeek, hourOfDay);

        if (factorOpt.isPresent() && factorOpt.get().hasSufficientData(10)) {
            return factorOpt.get().getEffectiveMultiplier();
        }

        // Fallback to time-based estimation
        return getTimeBasedMultiplier(dayOfWeek, hourOfDay);
    }

    /**
     * Get time-based traffic multiplier.
     */
    private double getTimeBasedMultiplier(int dayOfWeek, int hourOfDay) {
        // Weekend traffic
        if (dayOfWeek == 6 || dayOfWeek == 7) {
            if (hourOfDay >= 10 && hourOfDay <= 18) {
                return 1.3; // Weekend shopping traffic
            }
            return 1.0;
        }

        // Weekday traffic
        // Morning rush: 7-9 AM
        if (hourOfDay >= 7 && hourOfDay <= 9) {
            return 1.6;
        }
        // Lunch traffic: 12-1 PM
        if (hourOfDay == 12) {
            return 1.3;
        }
        // Evening rush: 5-7 PM
        if (hourOfDay >= 17 && hourOfDay <= 19) {
            return 1.7;
        }
        // Night time: low traffic
        if (hourOfDay >= 22 || hourOfDay <= 5) {
            return 0.9;
        }

        // Normal daytime traffic
        return 1.2;
    }

    /**
     * Calculate confidence score for the ETA.
     */
    private double calculateConfidenceScore(double trafficMultiplier, double distanceKm, boolean isPeakHour) {
        double baseConfidence = 0.8;

        // Reduce confidence for high traffic
        if (trafficMultiplier > 1.5) {
            baseConfidence -= 0.1;
        }
        if (trafficMultiplier > 2.0) {
            baseConfidence -= 0.1;
        }

        // Reduce confidence during peak hours
        if (isPeakHour) {
            baseConfidence -= 0.05;
        }

        // Increase confidence for longer distances (more stable)
        if (distanceKm > 10) {
            baseConfidence += 0.05;
        }

        return Math.max(0.3, Math.min(1.0, baseConfidence));
    }

    /**
     * Check if current location is on the route.
     */
    private boolean isInRoute(EtaCalculation.Location current,
                              EtaCalculation.Location pickup,
                              EtaCalculation.Location dropoff) {
        if (current == null) {
            return false;
        }

        // Simple check: if current is closer to dropoff than pickup
        double distanceToPickup = DistanceCalculator.calculateDistance(
                current.getLatitude(), current.getLongitude(),
                pickup.getLatitude(), pickup.getLongitude()
        );
        double distanceToDropoff = DistanceCalculator.calculateDistance(
                current.getLatitude(), current.getLongitude(),
                dropoff.getLatitude(), dropoff.getLongitude()
        );

        return distanceToDropoff < distanceToPickup;
    }

    /**
     * Check if current time is peak hour.
     */
    private boolean isPeakHour(LocalDateTime dateTime) {
        int hour = dateTime.getHour();
        int dayOfWeek = dateTime.getDayOfWeek().getValue();

        // Weekend is not peak
        if (dayOfWeek == 6 || dayOfWeek == 7) {
            return false;
        }

        // Morning peak: 7-9 AM
        if (hour >= 7 && hour <= 9) {
            return true;
        }

        // Evening peak: 5-7 PM
        if (hour >= 17 && hour <= 19) {
            return true;
        }

        return false;
    }

    /**
     * Generate area code from location.
     */
    private String generateAreaCode(EtaCalculation.Location location) {
        // Simple grid-based area code (in production, use geohashing or similar)
        int latGrid = (int) ((location.getLatitude() + 90) / 2);
        int lonGrid = (int) ((location.getLongitude() + 180) / 2);
        return String.format("AREA-%d-%d", latGrid, lonGrid);
    }

    // ========================================================================
    // Helper Methods
    // ========================================================================

    private void validateEtaRequest(EtaRequest request) {
        try {
            DistanceCalculator.validateCoordinates(
                    request.pickupLocation().latitude(),
                    request.pickupLocation().longitude()
            );
            DistanceCalculator.validateCoordinates(
                    request.dropoffLocation().latitude(),
                    request.dropoffLocation().longitude()
            );
        } catch (IllegalArgumentException e) {
            throw new InvalidLocationException(e.getMessage());
        }
    }

    private void createHistoryEntry(EtaCalculation calculation, Integer previousEta,
                                    EtaHistory.ChangeType changeType, String reason,
                                    RequestContext context) {
        EtaHistory history = new EtaHistory(
                calculation.getDispatchId(),
                calculation.getTenantId(),
                calculation,
                changeType,
                reason
        );

        if (previousEta != null) {
            history = new EtaHistory(
                    calculation.getDispatchId(),
                    calculation.getTenantId(),
                    calculation.getEtaMinutes(),
                    previousEta,
                    calculation,
                    changeType,
                    reason
            );
        }

        etaHistoryRepository.save(history);
    }

    private void publishUpdateEvent(EtaCalculation calculation, Integer previousEta,
                                     String reason, RequestContext context) {
        Integer etaChange = null;
        if (previousEta != null && calculation.getEtaMinutes() != null) {
            etaChange = calculation.getEtaMinutes() - previousEta;
        }

        eventPublisher.publish(new EtaUpdatedEvent(
                calculation.getDispatchId(),
                calculation.getTenantId(),
                previousEta,
                calculation.getEtaMinutes(),
                etaChange,
                reason,
                calculation.getTrafficLevel(),
                calculation.getTrafficMultiplier(),
                calculation.getConfidenceScore(),
                calculation.getRecalculationCount(),
                context.correlationId()
        ));
    }

    private EtaRequest.LocationDto toLocationDto(CalculateEtaCommand.LocationDto cmdLocation) {
        return new EtaRequest.LocationDto(
                cmdLocation.latitude(),
                cmdLocation.longitude(),
                cmdLocation.address(),
                cmdLocation.city(),
                cmdLocation.country()
        );
    }

    /**
     * Record for ETA calculation result.
     */
    private record EtaCalculationResult(
            int etaMinutes,
            double distanceKm,
            String trafficLevel,
            double trafficMultiplier,
            double confidenceScore
    ) {}
}
