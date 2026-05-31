package com.gogidix.aiservices.aicustomersegmentationservice.application.command;

import java.util.List;

/**
 * Command for removing customers from a segment.
 */
public record RemoveCustomersFromSegmentCommand(
        String segmentId,
        String tenantId,
        String userId,
        List<String> customerIds
) {
    public RemoveCustomersFromSegmentCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
        if (customerIds == null || customerIds.isEmpty()) {
            throw new IllegalArgumentException("customerIds cannot be null or empty");
        }
        if (customerIds.size() > 1000) {
            throw new IllegalArgumentException("Cannot remove more than 1000 customers at once");
        }
    }
}
