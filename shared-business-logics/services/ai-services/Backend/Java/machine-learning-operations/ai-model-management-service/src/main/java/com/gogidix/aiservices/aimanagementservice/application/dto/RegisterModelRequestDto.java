package com.gogidix.aiservices.aimanagementservice.application.dto;

import com.gogidix.aiservices.aimanagementservice.domain.model.ModelFramework;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for registering a model.
 */
public record RegisterModelRequestDto(
        @NotBlank(message = "modelName is required")
        String modelName,

        @NotNull(message = "framework is required")
        ModelFramework framework,

        @NotBlank(message = "version is required")
        String version,

        @NotBlank(message = "modelArtifactUrl is required")
        String modelArtifactUrl
) {
}
