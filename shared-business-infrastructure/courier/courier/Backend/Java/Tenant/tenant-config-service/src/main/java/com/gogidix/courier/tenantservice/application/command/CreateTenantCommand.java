package com.gogidix.courier.tenantservice.application.command;

import java.util.Objects;

/**
 * Command to create a new tenant.
 */
public record CreateTenantCommand(
        String tenantId,
        String name,
        String description,
        String userId
) {
    public CreateTenantCommand {
        tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        name = Objects.requireNonNull(name, "name is required");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("name must not exceed 100 characters");
        }
    }
}
