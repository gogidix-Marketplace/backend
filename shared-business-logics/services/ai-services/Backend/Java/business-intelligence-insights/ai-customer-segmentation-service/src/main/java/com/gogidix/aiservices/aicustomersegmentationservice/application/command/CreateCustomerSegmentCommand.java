package com.gogidix.aiservices.aicustomersegmentationservice.application.command;

import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentCriteria;

/**
 * Command for creating a new customer segment.
 */
public record CreateCustomerSegmentCommand(
        String tenantId,
        String userId,
        String name,
        String description,
        String segmentType,
        SegmentCriteria criteria
) {
    public CreateCustomerSegmentCommand {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("name must not exceed 100 characters");
        }
        if (criteria == null) {
            throw new IllegalArgumentException("criteria is required");
        }
        // Note: In records, automatic assignment happens after the compact constructor
        // So we can't reassign fields here. Validation only.
        if (segmentType == null || segmentType.isBlank()) {
            throw new IllegalArgumentException("segmentType is required");
        }
    }
}
