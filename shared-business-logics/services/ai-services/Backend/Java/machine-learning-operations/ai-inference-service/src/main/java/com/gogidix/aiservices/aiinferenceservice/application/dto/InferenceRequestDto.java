package com.gogidix.aiservices.aiinferenceservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

/**
 * Request DTO for inference.
 */
public record InferenceRequestDto(
        @NotBlank(message = "modelId is required")
        String modelId,

        @NotNull(message = "inputData is required")
        Map<String, Object> inputData,

        boolean batch,
        int timeout
) {
}
