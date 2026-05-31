package com.gogidix.courier.gpstrackingservice.application.command;

import java.util.Objects;

/**
 * Command to stop a tracking session.
 */
public record StopTrackingCommand(
        String sessionId,
        String endReason,
        String userId
) {
    public StopTrackingCommand {
        sessionId = Objects.requireNonNull(sessionId, "sessionId is required");
        if (sessionId.isBlank()) {
            throw new IllegalArgumentException("sessionId cannot be blank");
        }
        if (endReason == null || endReason.isBlank()) {
            endReason = "Manual stop";
        }
    }
}
