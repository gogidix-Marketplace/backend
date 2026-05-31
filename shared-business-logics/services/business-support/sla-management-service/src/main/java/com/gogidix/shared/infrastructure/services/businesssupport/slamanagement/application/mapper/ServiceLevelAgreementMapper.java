package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.mapper;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.request.CreateServiceLevelAgreementRequestDto;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.request.UpdateServiceLevelAgreementRequestDto;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.response.ServiceLevelAgreementResponseDto;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.model.ServiceLevelAgreement;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.stereotype.Component;
/**
 * Mapper for ServiceLevelAgreement entity
 */
@Component
public class ServiceLevelAgreementMapper {
    public ServiceLevelAgreement toEntity(CreateServiceLevelAgreementRequestDto dto, String tenantId) {
        return new ServiceLevelAgreement(
            new TenantId(tenantId),
            dto.name(),
            dto.description(),
            dto.serviceType(),
            dto.responseTimeThreshold(),
            dto.uptimePercentage(),
            dto.penaltyPercentage(),
            dto.validFrom(),
            dto.validUntil()
        );
    }
    public ServiceLevelAgreementResponseDto toResponseDto(ServiceLevelAgreement entity) {
        return new ServiceLevelAgreementResponseDto(
            entity.getId(),
            entity.getTenantId() != null ? entity.getTenantId().getValue() : null,
            entity.getName(),
            entity.getDescription(),
            entity.getServiceType(),
            entity.getResponseTimeThreshold(),
            entity.getUptimePercentage(),
            entity.getPenaltyPercentage(),
            entity.getStatus(),
            entity.getValidFrom(),
            entity.getValidUntil(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
    public void updateEntity(ServiceLevelAgreement entity, UpdateServiceLevelAgreementRequestDto dto) {
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setServiceType(dto.serviceType());
        entity.setResponseTimeThreshold(dto.responseTimeThreshold());
        entity.setUptimePercentage(dto.uptimePercentage());
        entity.setPenaltyPercentage(dto.penaltyPercentage());
        if (dto.validFrom() != null) {
            entity.setValidFrom(dto.validFrom());
        }
        if (dto.validUntil() != null) {
            entity.setValidUntil(dto.validUntil());
        }
        if (dto.status() != null) {
            entity.setStatus(dto.status());
        }
    }
}
