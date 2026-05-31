package com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Command to create a new tenant.
 * This is a CQRS command object representing the intention to create a tenant.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTenantCommand {

    @NotBlank(message = "Tenant name is required")
    @Size(min = 2, max = 100, message = "Tenant name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Domain is required")
    @Size(min = 3, max = 255, message = "Domain must be between 3 and 255 characters")
    private String domain;

    private String logoUrl;

    @NotBlank(message = "Primary contact email is required")
    @Email(message = "Invalid email format")
    private String primaryContactEmail;

    @NotBlank(message = "Primary contact name is required")
    @Size(max = 100, message = "Primary contact name must not exceed 100 characters")
    private String primaryContactName;

    @NotNull(message = "Max users is required")
    @Positive(message = "Max users must be positive")
    private Long maxUsers;

    @NotNull(message = "Max storage GB is required")
    @Positive(message = "Max storage must be positive")
    private Long maxStorageGB;

    private Map<String, Object> settings;
    private Map<String, Object> features;
}
