package com.gogidix.infrastructure.config.interfaces.rest.dto;

import com.gogidix.infrastructure.config.domain.model.Secret;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * DTO for creating a new secret.
 */
public record CreateSecretRequest(
        @NotBlank(message = "Tenant ID is required")
        String tenantId,

        @NotBlank(message = "Secret key is required")
        String secretKey,

        @NotBlank(message = "Name is required")
        String name,

        String description,

        @NotBlank(message = "Secret value is required")
        String secretValue,

        @NotNull(message = "Secret type is required")
        Secret.SecretType secretType,

        Set<String> tags,

        String category,

        String owner,

        Set<String> accessControlList,

        Integer rotationIntervalDays,

        LocalDateTime expiresAt,

        Map<String, Object> metadata,

        String changeReason
) {
}
