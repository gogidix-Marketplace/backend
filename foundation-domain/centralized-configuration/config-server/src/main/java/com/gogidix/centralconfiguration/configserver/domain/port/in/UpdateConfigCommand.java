package com.gogidix.centralconfiguration.configserver.domain.port.in;

import lombok.Builder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Command for updating an existing configuration entry.
 */
@Builder
public record UpdateConfigCommand(
        @NotNull(message = "Configuration ID is required")
        Long configurationId,

        @NotBlank(message = "Tenant ID is required")
        String tenantId,

        String configValue,

        Boolean isEncrypted,

        String description,

        String updatedBy,

        String changeReason
) {
}
