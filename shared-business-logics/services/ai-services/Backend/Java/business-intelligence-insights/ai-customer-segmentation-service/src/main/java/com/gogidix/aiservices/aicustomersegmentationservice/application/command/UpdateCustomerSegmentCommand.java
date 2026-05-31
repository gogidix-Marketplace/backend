package com.gogidix.aiservices.aicustomersegmentationservice.application.command;

import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentCriteria;

/**
 * Command for updating an existing customer segment.
 */
public record UpdateCustomerSegmentCommand(
        String segmentId,
        String tenantId,
        String userId,
        String name,
        String description,
        SegmentCriteria criteria,
        String status
) {
    public UpdateCustomerSegmentCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (name != null && name.length() > 100) {
            throw new IllegalArgumentException("name must not exceed 100 characters");
        }
    }
}
