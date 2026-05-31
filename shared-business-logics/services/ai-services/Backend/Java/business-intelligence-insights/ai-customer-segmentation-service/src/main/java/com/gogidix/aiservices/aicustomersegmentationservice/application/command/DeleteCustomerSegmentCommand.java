package com.gogidix.aiservices.aicustomersegmentationservice.application.command;

/**
 * Command for deleting a customer segment.
 */
public record DeleteCustomerSegmentCommand(
        String segmentId,
        String tenantId,
        String userId
) {
    public DeleteCustomerSegmentCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
