package com.gogidix.courier.tenantservice.application.command;

import java.util.Objects;

/**
 * Command to update an existing tenant.
 */
public record UpdateTenantCommand(
        String id,
        String name,
        String description,
        String userId
) {
    public UpdateTenantCommand {
        id = Objects.requireNonNull(id, "id is required");
        if (name != null && name.length() > 100) {
            throw new IllegalArgumentException("name must not exceed 100 characters");
        }
    }
}
