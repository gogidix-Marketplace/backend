package com.gogidix.aiservices.aitrainingservice.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for fine-tuning a model.
 */
public record FineTuneRequestDto(
        @NotBlank(message = "baseModel is required")
        String baseModel,

        @NotBlank(message = "trainingData is required")
        String trainingDataUrl,

        @Min(value = 1, message = "epochs must be at least 1")
        @Max(value = 1000, message = "epochs must not exceed 1000")
        Integer epochs,

        @DecimalMin(value = "0.0001", message = "learning rate must be at least 0.0001")
        @NotNull(message = "learningRate is required")
        Double learningRate
) {
}
