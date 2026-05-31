package com.gogidix.aiservices.aiuserprofilingservice.application.command;

import com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileCriteria;

/**
 * Command for updating an existing customer segment.
 */
public record UpdateUserProfileCommand(
        String segmentId,
        String tenantId,
        String userId,
        String name,
        String description,
        ProfileCriteria criteria,
        String status
) {
    public UpdateUserProfileCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (name != null && name.length() > 100) {
            throw new IllegalArgumentException("name must not exceed 100 characters");
        }
    }
}
