package com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.mapper;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.response.TenantResponseDto;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.model.Tenant;
import org.springframework.stereotype.Component;

/**
 * Mapper for Tenant entity and DTOs.
 */
@Component
public class TenantMapper {

    public TenantResponseDto toResponseDto(Tenant tenant) {
        if (tenant == null) {
            return null;
        }

        return TenantResponseDto.builder()
                .id(tenant.getId())
                .tenantId(tenant.getTenantId())
                .name(tenant.getName())
                .domain(tenant.getDomain())
                .logoUrl(tenant.getLogoUrl())
                .status(tenant.getStatus())
                .plan(tenant.getPlan())
                .trialEndsAt(tenant.getTrialEndsAt())
                .settings(tenant.getSettings())
                .features(tenant.getFeatures())
                .primaryContactEmail(tenant.getPrimaryContactEmail())
                .primaryContactName(tenant.getPrimaryContactName())
                .maxUsers(tenant.getMaxUsers())
                .maxStorageGB(tenant.getMaxStorageGB())
                .active(tenant.isActive())
                .createdAt(tenant.getCreatedAt())
                .updatedAt(tenant.getUpdatedAt())
                .build();
    }
}
