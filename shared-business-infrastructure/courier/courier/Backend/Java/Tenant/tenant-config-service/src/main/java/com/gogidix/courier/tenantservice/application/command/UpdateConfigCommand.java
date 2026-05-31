package com.gogidix.courier.tenantservice.application.command;

import com.gogidix.courier.tenantservice.domain.entity.TenantConfig;
import java.util.Map;
import java.util.Objects;

/**
 * Command to update tenant configuration.
 */
public record UpdateConfigCommand(
        String id,
        TenantConfig config,
        Map<String, Object> customSettings,
        String userId
) {
    public UpdateConfigCommand {
        id = Objects.requireNonNull(id, "id is required");
    }
}
