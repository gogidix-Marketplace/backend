package com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.response;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.model.Tenant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Tenant response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantResponseDto {

    private String id;
    private String tenantId;
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
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
