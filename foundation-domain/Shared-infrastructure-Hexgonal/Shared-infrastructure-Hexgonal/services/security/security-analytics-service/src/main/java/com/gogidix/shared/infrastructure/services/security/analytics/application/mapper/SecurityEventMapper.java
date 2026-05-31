package com.gogidix.shared.infrastructure.services.security.analytics.application.mapper;
import com.gogidix.shared.infrastructure.services.security.analytics.application.dto.request.CreateSecurityEventRequestDto;
import com.gogidix.shared.infrastructure.services.security.analytics.application.dto.response.SecurityEventResponseDto;
import com.gogidix.shared.infrastructure.services.security.analytics.domain.model.SecurityEvent;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.stereotype.Component;
/**
 * Mapper for SecurityEvent entity.
 */
@Component
public class SecurityEventMapper {
    public SecurityEvent toEntity(CreateSecurityEventRequestDto dto, String tenantId) {
        SecurityEvent entity = new SecurityEvent(
            new TenantId(tenantId),
            dto.eventType(),
            dto.severity()
        );
        entity.setSource(dto.source());
        entity.setDescription(dto.description());
        if (dto.timestamp() != null) {
            entity.setTimestamp(dto.timestamp());
        }
        return entity;
    }
    public SecurityEventResponseDto toResponseDto(SecurityEvent entity) {
        return new SecurityEventResponseDto(
            entity.getId(),
            entity.getTenantId() != null ? entity.getTenantId().getValue() : null,
            entity.getEventId(),
            entity.getEventType(),
            entity.getSeverity(),
            entity.getSource(),
            entity.getDescription(),
            entity.getTimestamp(),
            entity.getCreatedAt()
        );
    }
}
