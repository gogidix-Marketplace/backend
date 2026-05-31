package com.gogidix.aiservices.aimarketbasketanalysisservice.application.command;

import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketType;
import java.util.Map;

/**
 * Command for creating a new market basket.
 */
public record CreateBasketCommand(
        String name,
        String description,
        BasketType segmentType,
        Map<String, Object> criteria,
        String tenantId,
        String userId
) {
    public CreateBasketCommand {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("name must not exceed 100 characters");
        }
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("description must not exceed 500 characters");
        }
        if (segmentType == null) {
            throw new IllegalArgumentException("segmentType is required");
        }
        if (criteria == null || criteria.isEmpty()) {
            throw new IllegalArgumentException("criteria cannot be null or empty");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
