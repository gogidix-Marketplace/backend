package com.gogidix.courier.assignmentservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for assigning a driver to a dispatch.
 */
@Schema(description = "Request DTO for assigning a driver")
public record AssignDriverRequest(

        @JsonProperty("driver_id")
        @Schema(description = "Driver identifier", example = "driver-789", required = true)
        @NotBlank(message = "driverId is required")
        String driverId,

        @Schema(description = "Assignment reason", example = "Previous driver rejected assignment")
        String reason,

        @Schema(description = "Assignment score for optimization", example = "0.92")
        Double assignmentScore
) {}
