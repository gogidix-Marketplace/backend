package com.gogidix.courier.gpstrackingservice.application.command;

import java.util.List;
import java.util.Objects;

/**
 * Command to start a tracking session.
 */
public record StartTrackingCommand(
        String tenantId,
        String sessionId,
        String driverId,
        List<String> orderIds,
        String deviceType,
        String appVersion,
        String startReason,
        String userId
) {
    public StartTrackingCommand {
        tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        driverId = Objects.requireNonNull(driverId, "driverId is required");
        if (sessionId != null && sessionId.isBlank()) {
            throw new IllegalArgumentException("sessionId cannot be blank if provided");
        }
        if (orderIds != null && orderIds.stream().anyMatch(id -> id == null || id.isBlank())) {
            throw new IllegalArgumentException("orderIds cannot contain null or blank values");
        }
    }
}
