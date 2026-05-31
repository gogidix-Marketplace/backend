package com.gogidix.aiservices.aiproductrecommendationservice.application.command;

/**
 * Command for deleting a product recommendation.
 */
public record DeleteProductRecommendationCommand(
        String segmentId,
        String tenantId,
        String userId
) {
    public DeleteProductRecommendationCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
