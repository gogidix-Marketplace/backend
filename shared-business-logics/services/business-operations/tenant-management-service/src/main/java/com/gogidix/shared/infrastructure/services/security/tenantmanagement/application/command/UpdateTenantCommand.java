package com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Command to update an existing tenant.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTenantCommand {

    @NotBlank(message = "Tenant ID is required")
    private String id;

    @Size(min = 2, max = 100, message = "Tenant name must be between 2 and 100 characters")
    private String name;

    private String logoUrl;

    private String primaryContactEmail;

    private String primaryContactName;

    @Positive(message = "Max users must be positive")
    private Long maxUsers;

    @Positive(message = "Max storage must be positive")
    private Long maxStorageGB;

    private Map<String, Object> settings;
    private Map<String, Object> features;
}
