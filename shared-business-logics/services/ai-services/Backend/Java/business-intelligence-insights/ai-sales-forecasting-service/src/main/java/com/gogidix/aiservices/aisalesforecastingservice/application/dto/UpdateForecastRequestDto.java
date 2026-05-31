package com.gogidix.aiservices.aisalesforecastingservice.application.dto;

import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.util.Map;

/**
 * DTO for updating an existing segment.
 */
@Schema(description = "Request DTO for updating an existing sales forecast")
public record UpdateForecastRequestDto(

        @Schema(description = "Updated name of the segment", example = "Premium ForecastModels")
        @Size(max = 100, message = "name must not exceed 100 characters")
        String name,

        @Schema(description = "Updated description of the segment", example = "ForecastModels with lifetime value > $25,000")
        @Size(max = 500, message = "description must not exceed 500 characters")
        String description,

        @Schema(description = "Updated type of the segment", example = "TRANSACTIONAL")
        ForecastType segmentType,

        @Schema(description = "Updated criteria for segment membership", example = "{\"minLifetimeValue\": 25000}")
        Map<String, Object> criteria,

        @Schema(description = "Active status of the segment", example = "true")
        Boolean active

) {
}
