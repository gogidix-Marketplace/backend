package com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.request;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.model.Tenant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Create tenant request DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTenantRequestDto {

    @NotBlank(message = "Tenant ID is required")
    @Size(min = 3, max = 50, message = "Tenant ID must be between 3 and 50 characters")
    private String tenantId;

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100)
    private String name;

    private String domain;

    private String logoUrl;

    private Tenant.TenantStatus status;

    private Tenant.TenantPlan plan;

    private LocalDateTime trialEndsAt;

    private Map<String, Object> settings;

    private Map<String, Object> features;

    private String primaryContactEmail;

    private String primaryContactName;

    private Long maxUsers;

    private Long maxStorageGB;
}
