package com.gogidix.aiservices.aichurnpredictionservice.application.dto;

import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.util.Map;

/**
 * DTO for updating an existing segment.
 */
@Schema(description = "Request DTO for updating an existing churn prediction")
public record UpdatePredictionRequestDto(

        @Schema(description = "Updated name of the segment", example = "Premium Customers")
        @Size(max = 100, message = "name must not exceed 100 characters")
        String name,

        @Schema(description = "Updated description of the segment", example = "Customers with lifetime value > $25,000")
        @Size(max = 500, message = "description must not exceed 500 characters")
        String description,

        @Schema(description = "Updated type of the segment", example = "TRANSACTIONAL")
        PredictionType segmentType,

        @Schema(description = "Updated criteria for segment membership", example = "{\"minLifetimeValue\": 25000}")
        Map<String, Object> criteria,

        @Schema(description = "Active status of the segment", example = "true")
        Boolean active

) {
}
