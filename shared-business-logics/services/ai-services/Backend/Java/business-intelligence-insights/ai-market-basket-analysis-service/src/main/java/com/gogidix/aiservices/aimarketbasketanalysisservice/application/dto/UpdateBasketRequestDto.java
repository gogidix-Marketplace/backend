package com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto;

import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.util.Map;

/**
 * DTO for updating an existing segment.
 */
@Schema(description = "Request DTO for updating an existing market basket")
public record UpdateBasketRequestDto(

        @Schema(description = "Updated name of the segment", example = "Premium Customers")
        @Size(max = 100, message = "name must not exceed 100 characters")
        String name,

        @Schema(description = "Updated description of the segment", example = "Customers with lifetime value > $25,000")
        @Size(max = 500, message = "description must not exceed 500 characters")
        String description,

        @Schema(description = "Updated type of the segment", example = "TRANSACTIONAL")
        BasketType segmentType,

        @Schema(description = "Updated criteria for segment membership", example = "{\"minLifetimeValue\": 25000}")
        Map<String, Object> criteria,

        @Schema(description = "Active status of the segment", example = "true")
        Boolean active

) {
}
