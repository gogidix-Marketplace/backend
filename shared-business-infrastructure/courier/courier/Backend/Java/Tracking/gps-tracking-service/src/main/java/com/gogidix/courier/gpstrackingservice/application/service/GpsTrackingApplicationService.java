package com.gogidix.courier.gpstrackingservice.application.service;

import com.gogidix.courier.gpstrackingservice.application.command.StartTrackingCommand;
import com.gogidix.courier.gpstrackingservice.application.command.StopTrackingCommand;
import com.gogidix.courier.gpstrackingservice.application.command.SubmitGpsCommand;
import com.gogidix.courier.gpstrackingservice.application.dto.*;
import com.gogidix.courier.gpstrackingservice.application.mapper.GpsTrackingMapper;
import com.gogidix.courier.gpstrackingservice.domain.entity.DriverTrackingSession;
import com.gogidix.courier.gpstrackingservice.domain.entity.GpsLocation;
import com.gogidix.courier.gpstrackingservice.domain.entity.LocationHistory;
import com.gogidix.courier.gpstrackingservice.domain.event.LocationUpdatedEvent;
import com.gogidix.courier.gpstrackingservice.domain.event.TrackingSessionEndedEvent;
import com.gogidix.courier.gpstrackingservice.domain.event.TrackingSessionStartedEvent;
import com.gogidix.courier.gpstrackingservice.domain.repository.DriverTrackingSessionRepository;
import com.gogidix.courier.gpstrackingservice.domain.repository.GpsLocationRepository;
import com.gogidix.courier.gpstrackingservice.shared.exception.ConflictException;
import com.gogidix.courier.gpstrackingservice.shared.exception.NotFoundException;
import com.gogidix.courier.gpstrackingservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Application service for GPS tracking operations.
 */
@Service
@Transactional(readOnly = true)
public class GpsTrackingApplicationService {

    private static final Logger log = LoggerFactory.getLogger(GpsTrackingApplicationService.class);
    private static final String LOCATION_CACHE = "gpsLocations";
    private static final String SESSION_CACHE = "trackingSessions";

    private final GpsLocationRepository locationRepository;
    private final DriverTrackingSessionRepository sessionRepository;
    private final GpsTrackingMapper mapper;
    private final GpsTrackingEventPublisher eventPublisher;

    public GpsTrackingApplicationService(
            GpsLocationRepository locationRepository,
            DriverTrackingSessionRepository sessionRepository,
            GpsTrackingMapper mapper,
            GpsTrackingEventPublisher eventPublisher) {
        this.locationRepository = locationRepository;
        this.sessionRepository = sessionRepository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    // ==================== GPS Location Operations ====================

    /**
     * Submit a GPS location update.
     */
    @Transactional
    @CacheEvict(value = LOCATION_CACHE, allEntries = true)
    public GpsLocationResponse submitGpsLocation(SubmitGpsCommand command) {
        log.debug("Submitting GPS location for driver: {}", command.driverId());

        GpsLocation location = new GpsLocation(
                command.tenantId(),
                command.driverId(),
                command.orderId(),
                command.latitude(),
                command.longitude(),
                command.altitude(),
                command.accuracy(),
                command.speed(),
                command.heading(),
                command.locationSource()
        );

        if (command.batteryLevel() != null) {
            // Set battery level via reflection or add setter
        }

        location.validate();

        GpsLocation saved = locationRepository.save(location);

        // Update or create location history
        updateLocationHistory(location);

        // Update active tracking session if exists
        updateActiveSession(location);

        // Publish domain event
        eventPublisher.publish(new LocationUpdatedEvent(
                saved.getId(),
                saved.getTenantId(),
                saved.getDriverId(),
                saved.getLatitude(),
                saved.getLongitude(),
                saved.getAltitude(),
                saved.getAccuracy(),
                saved.getSpeed(),
                saved.getHeading()
        ));

        log.debug("GPS location saved: {}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Get current location for a driver.
     */
    @Cacheable(value = LOCATION_CACHE, key = "'current:' + #tenantId + ':' + #driverId")
    public GpsLocationResponse getCurrentLocation(String tenantId, String driverId) {
        log.debug("Getting current location for driver: {}", driverId);

        GpsLocation location = locationRepository.findMostRecentByDriverIdAndTenantId(tenantId, driverId)
                .orElseThrow(() -> new NotFoundException("GPS Location", "driver:" + driverId));

        return mapper.toResponseDto(location);
    }

    /**
     * Get location history for a driver.
     */
    public PagedResponseDto<GpsLocationResponse> getLocationHistory(
            String tenantId,
            String driverId,
            Instant startTime,
            Instant endTime,
            int page,
            int size) {

        log.debug("Getting location history for driver: {} from {} to {}", driverId, startTime, endTime);

        if (startTime == null) {
            startTime = Instant.now().minusSeconds(24 * 60 * 60); // Default 24 hours
        }
        if (endTime == null) {
            endTime = Instant.now();
        }

        List<GpsLocation> locations = locationRepository.findByTenantIdAndDriverIdAndTimestampBetween(
                tenantId, driverId, startTime, endTime);

        // Apply pagination
        int start = page * size;
        int end = Math.min(start + size, locations.size());

        List<GpsLocation> pagedLocations = new ArrayList<>();
        if (start < locations.size()) {
            pagedLocations = locations.subList(start, end);
        }

        List<GpsLocationResponse> responses = pagedLocations.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());

        return PagedResponseDto.of(responses, page, size, locations.size());
    }

    /**
     * Get location history for an order.
     */
    public List<GpsLocationResponse> getOrderLocationHistory(String tenantId, String orderId) {
        log.debug("Getting location history for order: {}", orderId);

        List<GpsLocation> locations = locationRepository.findByTenantIdAndOrderId(tenantId, orderId);

        return locations.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Find nearby drivers.
     */
    public List<NearbyDriverResponse> findNearbyDrivers(
            String tenantId,
            double latitude,
            double longitude,
            double radiusMeters,
            int maxResults) {

        log.debug("Finding nearby drivers near ({}, {}) within {}m", latitude, longitude, radiusMeters);

        List<GpsLocation> nearbyLocations = locationRepository.findByTenantIdNearby(
                tenantId, latitude, longitude, radiusMeters);

        // Get active sessions for drivers
        Set<String> activeDrivers = sessionRepository.findActiveByTenantId(tenantId)
                .stream()
                .map(DriverTrackingSession::getDriverId)
                .collect(Collectors.toSet());

        // Calculate distances and create response
        GpsLocation center = new GpsLocation(tenantId, "temp", null, latitude, longitude, null, null, null, null, null);

        List<NearbyDriverResponse> responses = nearbyLocations.stream()
                .limit(maxResults)
                .map(loc -> {
                    double distance = center.distanceTo(loc);
                    return mapper.toNearbyDriverDto(
                            loc,
                            distance,
                            activeDrivers.contains(loc.getDriverId())
                    );
                })
                .sorted(Comparator.comparing(NearbyDriverResponse::distanceMeters))
                .collect(Collectors.toList());

        return responses;
    }

    // ==================== Tracking Session Operations ====================

    /**
     * Start a tracking session.
     */
    @Transactional
    @CacheEvict(value = {LOCATION_CACHE, SESSION_CACHE}, allEntries = true)
    public TrackingSessionDTO startTracking(StartTrackingCommand command) {
        log.info("Starting tracking session for driver: {}", command.driverId());

        // Check if driver already has an active session
        Optional<DriverTrackingSession> existingSession =
                sessionRepository.findActiveByTenantIdAndDriverId(command.tenantId(), command.driverId());

        if (existingSession.isPresent()) {
            throw new ConflictException("Driver already has an active tracking session: " +
                    existingSession.get().getSessionId());
        }

        // Create new session
        String sessionId = command.sessionId() != null ? command.sessionId() : UUID.randomUUID().toString();
        DriverTrackingSession session = new DriverTrackingSession(
                command.tenantId(),
                sessionId,
                command.driverId()
        );

        // Add orders if provided
        if (command.orderIds() != null) {
            command.orderIds().forEach(session::addOrder);
        }

        // Set metadata
        if (command.deviceType() != null) {
            session.updateMetadata("deviceType", command.deviceType());
        }
        if (command.appVersion() != null) {
            session.updateMetadata("appVersion", command.appVersion());
        }
        if (command.startReason() != null) {
            session.updateMetadata("startReason", command.startReason());
        }

        session.start();
        session.validate();

        DriverTrackingSession saved = sessionRepository.save(session);

        // Publish domain event
        eventPublisher.publish(new TrackingSessionStartedEvent(
                saved.getId(),
                saved.getTenantId(),
                saved.getSessionId(),
                saved.getDriverId(),
                saved.getOrderIds() != null && !saved.getOrderIds().isEmpty()
                        ? saved.getOrderIds().get(0) : null
        ));

        log.info("Tracking session started: {}", saved.getSessionId());
        return mapper.toSessionDto(saved);
    }

    /**
     * Stop a tracking session.
     */
    @Transactional
    @CacheEvict(value = {LOCATION_CACHE, SESSION_CACHE}, allEntries = true)
    public TrackingSessionDTO stopTracking(StopTrackingCommand command) {
        log.info("Stopping tracking session: {}", command.sessionId());

        DriverTrackingSession session = sessionRepository.findBySessionId(command.sessionId())
                .orElseThrow(() -> new NotFoundException("Tracking Session", command.sessionId()));

        if (!session.isActive()) {
            throw new ValidationException("Session is not active: " + command.sessionId());
        }

        session.end(command.endReason());
        DriverTrackingSession saved = sessionRepository.save(session);

        // Publish domain event
        eventPublisher.publish(new TrackingSessionEndedEvent(
                saved.getId(),
                saved.getTenantId(),
                saved.getSessionId(),
                saved.getDriverId(),
                command.endReason()
        ));

        log.info("Tracking session stopped: {}", saved.getSessionId());
        return mapper.toSessionDto(saved);
    }

    /**
     * Get active tracking sessions.
     */
    @Cacheable(value = SESSION_CACHE, key = "'active:' + #tenantId")
    public List<TrackingSessionDTO> getActiveSessions(String tenantId) {
        log.debug("Getting active sessions for tenant: {}", tenantId);

        List<DriverTrackingSession> sessions = sessionRepository.findActiveByTenantId(tenantId);

        return sessions.stream()
                .map(mapper::toSessionDto)
                .collect(Collectors.toList());
    }

    /**
     * Get a tracking session by ID.
     */
    @Cacheable(value = SESSION_CACHE, key = "#sessionId")
    public TrackingSessionDTO getSession(String sessionId) {
        log.debug("Getting session: {}", sessionId);

        DriverTrackingSession session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new NotFoundException("Tracking Session", sessionId));

        return mapper.toSessionDto(session);
    }

    /**
     * Get sessions for a driver.
     */
    public List<TrackingSessionDTO> getDriverSessions(String tenantId, String driverId) {
        log.debug("Getting sessions for driver: {}", driverId);

        List<DriverTrackingSession> sessions = sessionRepository.findByTenantIdAndDriverId(tenantId, driverId);

        return sessions.stream()
                .map(mapper::toSessionDto)
                .collect(Collectors.toList());
    }

    // ==================== Helper Methods ====================

    /**
     * Update or create location history for a location.
     */
    private void updateLocationHistory(GpsLocation location) {
        String today = LocalDate.now().toString();

        Optional<LocationHistory> existingHistory = locationRepository
                .findHistoryByTenantIdAndDriverIdAndDate(
                        location.getTenantId(),
                        location.getDriverId(),
                        today
                );

        LocationHistory history = existingHistory.orElseGet(() ->
                new LocationHistory(location.getTenantId(), location.getDriverId(), location.getOrderId())
        );

        history.addLocation(location);
        locationRepository.saveHistory(history);
    }

    /**
     * Update active tracking session with new location.
     */
    private void updateActiveSession(GpsLocation location) {
        Optional<DriverTrackingSession> activeSession = sessionRepository
                .findActiveByTenantIdAndDriverId(location.getTenantId(), location.getDriverId());

        if (activeSession.isPresent()) {
            DriverTrackingSession session = activeSession.get();

            // Calculate distance from last location
            double distance = 0;
            if (session.getLastLocation() != null) {
                GpsLocation lastLoc = new GpsLocation(
                        location.getTenantId(), location.getDriverId(), null,
                        session.getLastLocation().getLatitude(),
                        session.getLastLocation().getLongitude(),
                        null, null, null, null, null
                );
                distance = lastLoc.distanceTo(location);
            }

            session.updateLocation(new GpsLocation.GeoPoint(
                    location.getLatitude(),
                    location.getLongitude()
            ), distance);

            sessionRepository.save(session);
        }
    }

    /**
     * Clean up old location data.
     */
    @Transactional
    public long cleanupOldLocations(int daysToKeep) {
        Instant cutoff = Instant.now().minusSeconds(daysToKeep * 24L * 60 * 60);
        long deleted = locationRepository.deleteOlderThan(cutoff);
        log.info("Deleted {} old locations older than {}", deleted, cutoff);
        return deleted;
    }

    /**
     * Get tracking statistics for a driver.
     */
    public Map<String, Object> getDriverStats(String tenantId, String driverId) {
        log.debug("Getting stats for driver: {}", driverId);

        long locationCount = locationRepository.countByTenantIdAndDriverId(tenantId, driverId);
        long sessionCount = sessionRepository.findByTenantIdAndDriverId(tenantId, driverId).size();

        Map<String, Object> stats = new HashMap<>();
        stats.put("driverId", driverId);
        stats.put("locationCount", locationCount);
        stats.put("sessionCount", sessionCount);

        // Get current session if active
        sessionRepository.findActiveByTenantIdAndDriverId(tenantId, driverId)
                .ifPresent(session -> {
                    stats.put("activeSessionId", session.getSessionId());
                    stats.put("sessionDuration", session.getDurationSeconds());
                    stats.put("sessionDistance", session.getTotalDistanceMeters());
                    stats.put("locationUpdateCount", session.getLocationUpdateCount());
                });

        return stats;
    }
}
