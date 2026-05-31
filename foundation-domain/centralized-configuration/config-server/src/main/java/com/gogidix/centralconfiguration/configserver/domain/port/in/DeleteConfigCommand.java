package com.gogidix.centralconfiguration.configserver.domain.port.in;

import lombok.Builder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Command for deleting a configuration entry.
 */
@Builder
public record DeleteConfigCommand(
        @NotNull(message = "Configuration ID is required")
        Long configurationId,

        @NotBlank(message = "Tenant ID is required")
        String tenantId,

        String deletedBy,

        String reason
) {
}
