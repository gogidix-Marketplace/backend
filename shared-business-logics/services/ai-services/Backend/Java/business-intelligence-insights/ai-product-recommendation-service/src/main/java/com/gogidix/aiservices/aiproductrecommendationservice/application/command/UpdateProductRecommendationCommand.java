package com.gogidix.aiservices.aiproductrecommendationservice.application.command;

import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationCriteria;

/**
 * Command for updating an existing product recommendation.
 */
public record UpdateProductRecommendationCommand(
        String segmentId,
        String tenantId,
        String userId,
        String name,
        String description,
        RecommendationCriteria criteria,
        String status
) {
    public UpdateProductRecommendationCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (name != null && name.length() > 100) {
            throw new IllegalArgumentException("name must not exceed 100 characters");
        }
    }
}
