package com.gogidix.aiservices.aimarketbasketanalysisservice.application.command;

/**
 * Command for deleting a market basket.
 */
public record DeleteMarketBasketCommand(
        String segmentId,
        String tenantId,
        String userId
) {
    public DeleteMarketBasketCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
