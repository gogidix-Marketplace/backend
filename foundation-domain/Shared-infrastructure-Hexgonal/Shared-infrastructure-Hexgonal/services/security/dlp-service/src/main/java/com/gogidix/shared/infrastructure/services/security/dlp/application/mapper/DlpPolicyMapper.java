package com.gogidix.shared.infrastructure.services.security.dlp.application.mapper;

import com.gogidix.shared.infrastructure.services.security.dlp.application.dto.request.CreateDlpPolicyRequestDto;
import com.gogidix.shared.infrastructure.services.security.dlp.application.dto.request.UpdateDlpPolicyRequestDto;
import com.gogidix.shared.infrastructure.services.security.dlp.application.dto.response.DlpPolicyResponseDto;
import com.gogidix.shared.infrastructure.services.security.dlp.domain.model.DlpPolicy;
import com.gogidix.shared.infrastructure.services.security.dlp.domain.model.DlpPolicy.PolicyStatus;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.stereotype.Component;

/**
 * Mapper for DlpPolicy entity.
 */
@Component
public class DlpPolicyMapper {

    public DlpPolicy toEntity(CreateDlpPolicyRequestDto dto, String tenantId) {
        DlpPolicy entity = new DlpPolicy(new TenantId(tenantId), dto.policyName());

        entity.setDescription(dto.description());
        entity.setSensitiveDataPatterns(dto.sensitiveDataPatterns());
        entity.setAction(dto.action());
        entity.setStatus(PolicyStatus.DRAFT);

        return entity;
    }

    public DlpPolicyResponseDto toResponseDto(DlpPolicy entity) {
        return new DlpPolicyResponseDto(
            entity.getId(),
            entity.getTenantId() != null ? entity.getTenantId().getValue() : null,
            entity.getPolicyId(),
            entity.getPolicyName(),
            entity.getDescription(),
            entity.getStatus() != null ? entity.getStatus().name() : null,
            entity.getSensitiveDataPatterns(),
            entity.getAction(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public void updateEntity(UpdateDlpPolicyRequestDto dto, DlpPolicy entity) {
        if (dto.policyName() != null) {
            entity.setPolicyName(dto.policyName());
        }
        if (dto.description() != null) {
            entity.setDescription(dto.description());
        }
        if (dto.sensitiveDataPatterns() != null) {
            entity.setSensitiveDataPatterns(dto.sensitiveDataPatterns());
        }
        if (dto.action() != null) {
            entity.setAction(dto.action());
        }
    }
}
