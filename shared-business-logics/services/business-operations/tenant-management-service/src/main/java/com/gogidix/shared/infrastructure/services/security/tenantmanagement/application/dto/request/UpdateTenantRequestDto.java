package com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Update tenant request DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTenantRequestDto {

    private String name;

    private String logoUrl;

    private String primaryContactEmail;

    private String primaryContactName;

    private Long maxUsers;

    private Long maxStorageGB;

    private Map<String, Object> settings;

    private Map<String, Object> features;
}
