package com.gogidix.aiservices.aimodeltrainingservice.application.dto;

import com.gogidix.aiservices.aimodeltrainingservice.domain.model.TrainingAlgorithm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

/**
 * Request DTO for starting a training job.
 */
public record StartTrainingJobRequestDto(
        @NotBlank(message = "modelType is required")
        String modelType,

        @NotBlank(message = "trainingData is required")
        String trainingDataUrl,

        Map<String, Object> hyperparameters,

        @NotNull(message = "algorithm is required")
        TrainingAlgorithm algorithm
) {
}
