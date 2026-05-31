package com.gogidix.courier.gpstrackingservice.application.mapper;

import com.gogidix.courier.gpstrackingservice.application.command.StartTrackingCommand;
import com.gogidix.courier.gpstrackingservice.application.dto.*;
import com.gogidix.courier.gpstrackingservice.domain.entity.DriverTrackingSession;
import com.gogidix.courier.gpstrackingservice.domain.entity.GpsLocation;
import com.gogidix.courier.gpstrackingservice.shared.context.RequestContext;
import org.springframework.stereotype.Component;

/**
 * Mapper for GPS tracking entities and DTOs.
 */
@Component
public class GpsTrackingMapper {

    /**
     * Convert GPS location request to entity.
     */
    public GpsLocation toEntity(GpsLocationRequest request, RequestContext context) {
        return new GpsLocation(
                context.tenantId(),
                request.driverId(),
                request.orderId(),
                request.latitude(),
                request.longitude(),
                request.altitude(),
                request.accuracy(),
                request.speed(),
                request.heading(),
                request.locationSource()
        );
    }

    /**
     * Convert GPS location entity to response DTO.
     */
    public GpsLocationResponse toResponseDto(GpsLocation location) {
        return new GpsLocationResponse(
                location.getId(),
                location.getTenantId(),
                location.getDriverId(),
                location.getOrderId(),
                location.getLatitude(),
                location.getLongitude(),
                location.getAltitude(),
                location.getAccuracy(),
                location.getSpeed(),
                location.getHeading(),
                location.getBatteryLevel(),
                location.getLocationSource(),
                location.getTimestamp(),
                location.getCreatedAt(),
                location.isMoving(),
                location.isRecent(300) // 5 minutes
        );
    }

    /**
     * Convert tracking session entity to response DTO.
     */
    public TrackingSessionDTO toSessionDto(DriverTrackingSession session) {
        return new TrackingSessionDTO(
                session.getId(),
                session.getTenantId(),
                session.getSessionId(),
                session.getDriverId(),
                session.getOrderIds(),
                session.getStatus(),
                session.getStartTime(),
                session.getEndTime(),
                session.getLastLocation() != null ? session.getLastLocation().getLatitude() : null,
                session.getLastLocation() != null ? session.getLastLocation().getLongitude() : null,
                session.getLastLocationTime(),
                session.getTotalDistanceMeters(),
                session.getLocationUpdateCount(),
                session.getDurationSeconds(),
                session.getEndReason(),
                session.getMetadata() != null ? session.getMetadata().getDeviceType() : null,
                session.getMetadata() != null ? session.getMetadata().getAppVersion() : null,
                session.isLocationStale(5), // 5 minutes stale threshold
                session.getCreatedAt(),
                session.getUpdatedAt()
        );
    }

    /**
     * Convert nearby driver location to response DTO.
     */
    public NearbyDriverResponse toNearbyDriverDto(GpsLocation location, Double distance, Boolean sessionActive) {
        return new NearbyDriverResponse(
                location.getDriverId(),
                location.getLatitude(),
                location.getLongitude(),
                distance,
                location.getSpeed(),
                location.getHeading(),
                location.getTimestamp(),
                location.isMoving(),
                location.getBatteryLevel(),
                sessionActive
        );
    }

    /**
     * Convert start tracking request to command.
     */
    public StartTrackingCommand toCommand(StartTrackingRequest request, RequestContext context) {
        String sessionId = request.sessionId() != null && !request.sessionId().isBlank()
                ? request.sessionId()
                : java.util.UUID.randomUUID().toString();

        return new StartTrackingCommand(
                context.tenantId(),
                sessionId,
                request.driverId(),
                request.orderIds(),
                request.deviceType(),
                request.appVersion(),
                request.startReason(),
                context.userId()
        );
    }

    /**
     * Convert location history entity to response DTO.
     */
    public LocationHistoryResponse toHistoryDto(com.gogidix.courier.gpstrackingservice.domain.entity.LocationHistory history) {
        return new LocationHistoryResponse(
                history.getId(),
                history.getTenantId(),
                history.getDriverId(),
                history.getOrderId(),
                history.getDate(),
                history.getTotalDistanceMeters(),
                history.getDurationSeconds(),
                history.getStartTime(),
                history.getEndTime(),
                history.getLocationCount(),
                history.getAverageSpeed()
        );
    }
}
