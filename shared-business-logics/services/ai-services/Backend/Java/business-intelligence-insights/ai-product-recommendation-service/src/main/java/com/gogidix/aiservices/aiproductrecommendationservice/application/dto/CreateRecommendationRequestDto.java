package com.gogidix.aiservices.aiproductrecommendationservice.application.dto;

import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;

/**
 * DTO for creating a new segment.
 */
@Schema(description = "Request DTO for creating a new product recommendation")
public record CreateRecommendationRequestDto(

        @Schema(description = "Name of the segment", example = "High Value Products", required = true)
        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must not exceed 100 characters")
        String name,

        @Schema(description = "Description of the segment", example = "Products with lifetime value > $10,000")
        @Size(max = 500, message = "description must not exceed 500 characters")
        String description,

        @Schema(description = "Type of the segment", example = "BEHAVIORAL", required = true)
        @NotNull(message = "segmentType is required")
        RecommendationType segmentType,

        @Schema(description = "Criteria for segment membership", example = "{\"minLifetimeValue\": 10000, \"minPurchaseCount\": 5}", required = true)
        @NotNull(message = "criteria is required")
        Map<String, Object> criteria

) {
}
