package com.gogidix.aiservices.aiuserprofilingservice.application.command;

/**
 * Command for deleting a customer segment.
 */
public record DeleteProfileCommand(
        String segmentId,
        String tenantId,
        String userId
) {
    public DeleteProfileCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
