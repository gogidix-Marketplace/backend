package com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto;

import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

/**
 * DTO for market basket response.
 */
@Schema(description = "Response DTO for market basket")
public record MarketBasketResponseDto(

        @Schema(description = "Unique segment identifier", example = "seg_1234567890abcdef")
        String id,

        @Schema(description = "Name of the segment", example = "High Value Customers")
        String name,

        @Schema(description = "Description of the segment", example = "Customers with lifetime value > $10,000")
        String description,

        @Schema(description = "Type of the segment", example = "BEHAVIORAL")
        BasketType segmentType,

        @Schema(description = "Criteria for segment membership")
        Map<String, Object> criteria,

        @Schema(description = "Customer IDs in this segment")
        Set<String> customerIds,

        @Schema(description = "Number of customers in the segment", example = "1523")
        int customerCount,

        @Schema(description = "Active status of the segment", example = "true")
        boolean active,

        @Schema(description = "Tenant identifier", example = "tenant_001")
        String tenantId,

        @Schema(description = "Creation timestamp", example = "2024-01-15T10:30:00Z")
        Instant createdAt,

        @Schema(description = "Last update timestamp", example = "2024-01-20T14:45:00Z")
        Instant updatedAt,

        @Schema(description = "Version for optimistic locking", example = "1")
        Long version

) {
    /**
     * Get the segment ID (alias for id for test compatibility).
     * @return the unique segment identifier
     */
    public String segmentId() {
        return id;
    }
}
