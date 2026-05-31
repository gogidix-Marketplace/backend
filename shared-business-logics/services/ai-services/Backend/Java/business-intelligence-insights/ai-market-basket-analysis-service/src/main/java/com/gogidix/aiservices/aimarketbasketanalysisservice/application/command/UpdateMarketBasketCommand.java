package com.gogidix.aiservices.aimarketbasketanalysisservice.application.command;

import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketCriteria;

/**
 * Command for updating an existing market basket.
 */
public record UpdateMarketBasketCommand(
        String segmentId,
        String tenantId,
        String userId,
        String name,
        String description,
        BasketCriteria criteria,
        String status
) {
    public UpdateMarketBasketCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (name != null && name.length() > 100) {
            throw new IllegalArgumentException("name must not exceed 100 characters");
        }
    }
}
