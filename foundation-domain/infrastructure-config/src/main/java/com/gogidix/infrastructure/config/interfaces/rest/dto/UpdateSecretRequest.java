package com.gogidix.infrastructure.config.interfaces.rest.dto;

import com.gogidix.infrastructure.config.domain.model.Secret;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * DTO for updating an existing secret.
 */
public record UpdateSecretRequest(
        String name,

        String description,

        String secretValue,

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
