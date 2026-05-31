package com.gogidix.courier.availabilityservice.application.dto;

/**
 * DTO for available driver response.
 */
public record AvailableDriverResponse(
        String driverId,
        String status,
        Integer currentLoad,
        Integer maxCapacity,
        String availabilityDate,
        LocationInfo location
) {
    public record LocationInfo(
            Double latitude,
            Double longitude,
            String lastUpdated
    ) {
    }
}
