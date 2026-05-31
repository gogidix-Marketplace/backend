package com.gogidix.aiservices.aiuserprofilingservice.application.command;

/**
 * Command for deleting a customer segment.
 */
public record DeleteUserProfileCommand(
        String segmentId,
        String tenantId,
        String userId
) {
    public DeleteUserProfileCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
