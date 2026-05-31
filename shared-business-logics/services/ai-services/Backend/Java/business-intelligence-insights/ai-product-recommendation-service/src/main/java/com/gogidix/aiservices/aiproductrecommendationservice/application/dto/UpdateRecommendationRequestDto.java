package com.gogidix.aiservices.aiproductrecommendationservice.application.dto;

import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.util.Map;

/**
 * DTO for updating an existing segment.
 */
@Schema(description = "Request DTO for updating an existing product recommendation")
public record UpdateRecommendationRequestDto(

        @Schema(description = "Updated name of the segment", example = "Premium Products")
        @Size(max = 100, message = "name must not exceed 100 characters")
        String name,

        @Schema(description = "Updated description of the segment", example = "Products with lifetime value > $25,000")
        @Size(max = 500, message = "description must not exceed 500 characters")
        String description,

        @Schema(description = "Updated type of the segment", example = "TRANSACTIONAL")
        RecommendationType segmentType,

        @Schema(description = "Updated criteria for segment membership", example = "{\"minLifetimeValue\": 25000}")
        Map<String, Object> criteria,

        @Schema(description = "Active status of the segment", example = "true")
        Boolean active

) {
}
