package com.gogidix.aiservices.aisalesforecastingservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * DTO for adding/removing customers to/from a segment.
 */
@Schema(description = "Request DTO for modifying customers in a segment")
public record ModifyForecastModelsRequestDto(

        @Schema(description = "List of customer IDs to add or remove", example = "[\"cust_001\", \"cust_002\", \"cust_003\"]", required = true)
        @NotEmpty(message = "customerIds must not be empty")
        @Size(min = 1, max = 1000, message = "customerIds must contain between 1 and 1000 IDs")
        List<String> customerIds

) {
}
