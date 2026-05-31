package com.gogidix.aiservices.aiproductrecommendationservice.application.command;

import java.util.List;

/**
 * Command for adding customers to a segment.
 */
public record AddProductsToRecommendationCommand(
        String segmentId,
        String tenantId,
        String userId,
        List<String> customerIds
) {
    public AddProductsToRecommendationCommand {
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
            throw new IllegalArgumentException("Cannot add more than 1000 customers at once");
        }
    }
}
