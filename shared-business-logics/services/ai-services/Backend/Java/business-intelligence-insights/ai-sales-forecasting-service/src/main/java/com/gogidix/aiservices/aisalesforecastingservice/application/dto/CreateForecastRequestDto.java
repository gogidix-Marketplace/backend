package com.gogidix.aiservices.aisalesforecastingservice.application.dto;

import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;

/**
 * DTO for creating a new segment.
 */
@Schema(description = "Request DTO for creating a new sales forecast")
public record CreateForecastRequestDto(

        @Schema(description = "Name of the segment", example = "High Value ForecastModels", required = true)
        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must not exceed 100 characters")
        String name,

        @Schema(description = "Description of the segment", example = "ForecastModels with lifetime value > $10,000")
        @Size(max = 500, message = "description must not exceed 500 characters")
        String description,

        @Schema(description = "Type of the segment", example = "BEHAVIORAL", required = true)
        @NotNull(message = "segmentType is required")
        ForecastType segmentType,

        @Schema(description = "Criteria for segment membership", example = "{\"minLifetimeValue\": 10000, \"minPurchaseCount\": 5}", required = true)
        @NotNull(message = "criteria is required")
        Map<String, Object> criteria

) {
}
