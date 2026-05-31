package com.gogidix.courier.routingservice.application.dto;

import com.gogidix.courier.routingservice.domain.entity.Route;
import com.gogidix.courier.routingservice.domain.entity.RouteWaypoint;

import java.time.Instant;

/**
 * DTO for waypoint data.
 */
public record WaypointDTO(
        String waypointId,
        Integer sequenceNumber,
        GeoPointDTO location,
        String orderId,
        String customerId,
        String customerName,
        String address,
        RouteWaypoint.WaypointType waypointType,
        RouteWaypoint.WaypointStatus status,
        Instant estimatedArrival,
        Instant actualArrival,
        Instant estimatedDeparture,
        Instant actualDeparture,
        Integer serviceDurationSeconds,
        Double distanceFromPreviousMeters,
        Double distanceToNextMeters,
        Integer travelTimeFromPreviousSeconds,
        Integer travelTimeToNextSeconds,
        Instant timeWindowStart,
        Instant timeWindowEnd,
        Integer priority,
        String notes,
        String contactPhone,
        Integer packageCount,
        Double packageWeightKg,
        WaypointMetadataDTO metadata,
        Instant createdAt,
        Instant updatedAt
) {
    /**
     * DTO for geographic coordinates.
     */
    public record GeoPointDTO(
            Double latitude,
            Double longitude,
            String address,
            String name
    ) {
        public static GeoPointDTO from(Route.GeoPoint geoPoint) {
            if (geoPoint == null) {
                return null;
            }
            return new GeoPointDTO(
                    geoPoint.getLatitude(),
                    geoPoint.getLongitude(),
                    geoPoint.getAddress(),
                    geoPoint.getName()
            );
        }
    }

    /**
     * DTO for waypoint metadata.
     */
    public record WaypointMetadataDTO(
            String skipReason,
            String failureReason,
            String signature,
            String photoUrl,
            Integer attemptCount,
            Instant firstAttempt,
            String specialInstructions
    ) {
        public static WaypointMetadataDTO from(RouteWaypoint.WaypointMetadata metadata) {
            if (metadata == null) {
                return null;
            }
            return new WaypointMetadataDTO(
                    metadata.getSkipReason(),
                    metadata.getFailureReason(),
                    metadata.getSignature(),
                    metadata.getPhotoUrl(),
                    metadata.getAttemptCount(),
                    metadata.getFirstAttempt(),
                    metadata.getSpecialInstructions()
            );
        }
    }

    public static WaypointDTO from(RouteWaypoint waypoint) {
        if (waypoint == null) {
            return null;
        }

        GeoPointDTO locationDto = null;
        if (waypoint.getLocation() != null) {
            locationDto = new GeoPointDTO(
                    waypoint.getLocation().getLatitude(),
                    waypoint.getLocation().getLongitude(),
                    waypoint.getAddress(),
                    null
            );
        }

        return new WaypointDTO(
                waypoint.getWaypointId(),
                waypoint.getSequenceNumber(),
                locationDto,
                waypoint.getOrderId(),
                waypoint.getCustomerId(),
                waypoint.getCustomerName(),
                waypoint.getAddress(),
                waypoint.getWaypointType(),
                waypoint.getStatus(),
                waypoint.getEstimatedArrival(),
                waypoint.getActualArrival(),
                waypoint.getEstimatedDeparture(),
                waypoint.getActualDeparture(),
                waypoint.getServiceDurationSeconds(),
                waypoint.getDistanceFromPreviousMeters(),
                waypoint.getDistanceToNextMeters(),
                waypoint.getTravelTimeFromPreviousSeconds(),
                waypoint.getTravelTimeToNextSeconds(),
                waypoint.getTimeWindowStart(),
                waypoint.getTimeWindowEnd(),
                waypoint.getPriority(),
                waypoint.getNotes(),
                waypoint.getContactPhone(),
                waypoint.getPackageCount(),
                waypoint.getPackageWeightKg(),
                WaypointMetadataDTO.from(waypoint.getMetadata()),
                waypoint.getCreatedAt(),
                waypoint.getUpdatedAt()
        );
    }
}
