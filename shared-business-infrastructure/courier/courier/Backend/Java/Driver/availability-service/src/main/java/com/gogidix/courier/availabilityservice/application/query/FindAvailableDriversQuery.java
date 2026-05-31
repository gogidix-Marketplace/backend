package com.gogidix.courier.availabilityservice.application.query;

import java.time.LocalDate;
import java.util.List;

/**
 * Query to find available drivers.
 */
public record FindAvailableDriversQuery(
        String tenantId,
        LocalDate date,
        String zoneId,
        List<String> preferredZones,
        Integer minCapacity,
        Boolean includeLocation
) {
    public FindAvailableDriversQuery {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
        if (date == null) {
            throw new IllegalArgumentException("date is required");
        }
    }
}
