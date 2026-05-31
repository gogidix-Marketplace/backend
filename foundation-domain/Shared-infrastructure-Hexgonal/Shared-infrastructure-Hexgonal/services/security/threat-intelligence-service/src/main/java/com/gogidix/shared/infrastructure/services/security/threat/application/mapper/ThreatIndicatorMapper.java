package com.gogidix.shared.infrastructure.services.security.threat.application.mapper;
import com.gogidix.shared.infrastructure.services.security.threat.application.dto.request.CreateThreatIndicatorRequestDto;
import com.gogidix.shared.infrastructure.services.security.threat.application.dto.request.UpdateThreatIndicatorRequestDto;
import com.gogidix.shared.infrastructure.services.security.threat.application.dto.response.ThreatIndicatorResponseDto;
import com.gogidix.shared.infrastructure.services.security.threat.domain.model.ThreatIndicator;
import com.gogidix.shared.infrastructure.services.security.threat.domain.model.ThreatIndicator.ThreatSeverity;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.stereotype.Component;
/**
 * Mapper for ThreatIndicator entity.
 */
@Component
public class ThreatIndicatorMapper {
    public ThreatIndicator toEntity(CreateThreatIndicatorRequestDto dto, String tenantId) {
        ThreatIndicator entity = new ThreatIndicator(
            new TenantId(tenantId),
            dto.indicatorType(),
            dto.value()
        );
        if (dto.severity() != null) {
            try {
                entity.setSeverity(ThreatSeverity.valueOf(dto.severity().toUpperCase()));
            } catch (IllegalArgumentException e) {
                entity.setSeverity(ThreatSeverity.MEDIUM);
            }
        }
        entity.setDescription(dto.description());
        entity.setActive(true);
        return entity;
    }
    public ThreatIndicatorResponseDto toResponseDto(ThreatIndicator entity) {
        return new ThreatIndicatorResponseDto(
            entity.getId(),
            entity.getTenantId() != null ? entity.getTenantId().getValue() : null,
            entity.getIndicatorId(),
            entity.getIndicatorType(),
            entity.getValue(),
            entity.getSeverity() != null ? entity.getSeverity().name() : null,
            entity.getDescription(),
            entity.getActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
    public void updateEntity(UpdateThreatIndicatorRequestDto dto, ThreatIndicator entity) {
        if (dto.indicatorType() != null) {
            entity.setIndicatorType(dto.indicatorType());
        }
        if (dto.value() != null) {
            entity.setValue(dto.value());
        }
        if (dto.severity() != null) {
            try {
                entity.setSeverity(ThreatSeverity.valueOf(dto.severity().toUpperCase()));
            } catch (IllegalArgumentException e) {
                // Keep existing severity
            }
        }
        if (dto.description() != null) {
            entity.setDescription(dto.description());
        }
    }
}
