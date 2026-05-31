package com.gogidix.infrastructure.config.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for rotating a secret value.
 */
public record RotateSecretRequest(
        @NotBlank(message = "New secret value is required")
        String newSecretValue,

        String changeReason
) {
}
