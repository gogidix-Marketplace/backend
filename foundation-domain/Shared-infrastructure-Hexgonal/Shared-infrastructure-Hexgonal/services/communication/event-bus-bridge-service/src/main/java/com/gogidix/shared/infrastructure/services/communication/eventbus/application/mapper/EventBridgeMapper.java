package com.gogidix.shared.infrastructure.services.communication.eventbus.application.mapper;
import com.gogidix.shared.infrastructure.services.communication.eventbus.application.dto.request.CreateEventBridgeRequestDto;
import com.gogidix.shared.infrastructure.services.communication.eventbus.application.dto.response.EventBridgeResponseDto;
import com.gogidix.shared.infrastructure.services.communication.eventbus.domain.model.EventBridge;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.stereotype.Component;
/**
 * Mapper for EventBridge entity
 */
@Component
public class EventBridgeMapper {
    public EventBridge toEntity(CreateEventBridgeRequestDto dto, String tenantId) {
        TenantId tid = new TenantId(tenantId);
        EventBridge entity = new EventBridge(tid, dto.name(), dto.sourceType(), dto.targetType());
        entity.setSourceTopic(dto.sourceTopic());
        entity.setTargetTopic(dto.targetTopic());
        entity.setSourceExchange(dto.sourceExchange());
        entity.setTargetExchange(dto.targetExchange());
        entity.setSourceQueue(dto.sourceQueue());
        entity.setTargetQueue(dto.targetQueue());
        entity.setEnabled(dto.enabled());
        entity.setFilterExpression(dto.filterExpression());
        entity.setTransformationRules(dto.transformationRules());
        entity.setMaxRetries(dto.maxRetries() > 0 ? dto.maxRetries() : 3);
        return entity;
    }
    public EventBridge updateEntity(EventBridge entity, CreateEventBridgeRequestDto dto) {
        if (dto.name() != null) {
            entity.setName(dto.name());
        }
        if (dto.sourceTopic() != null) {
            entity.setSourceTopic(dto.sourceTopic());
        }
        if (dto.targetTopic() != null) {
            entity.setTargetTopic(dto.targetTopic());
        }
        if (dto.sourceExchange() != null) {
            entity.setSourceExchange(dto.sourceExchange());
        }
        if (dto.targetExchange() != null) {
            entity.setTargetExchange(dto.targetExchange());
        }
        if (dto.sourceQueue() != null) {
            entity.setSourceQueue(dto.sourceQueue());
        }
        if (dto.targetQueue() != null) {
            entity.setTargetQueue(dto.targetQueue());
        }
        entity.setEnabled(dto.enabled());
        if (dto.filterExpression() != null) {
            entity.setFilterExpression(dto.filterExpression());
        }
        if (dto.transformationRules() != null) {
            entity.setTransformationRules(dto.transformationRules());
        }
        if (dto.maxRetries() > 0) {
            entity.setMaxRetries(dto.maxRetries());
        }
        return entity;
    }
    public EventBridgeResponseDto toResponseDto(EventBridge entity) {
        return new EventBridgeResponseDto(
            entity.getId(),
            entity.getTenantId() != null ? entity.getTenantId().getValue() : null,
            entity.getName(),
            entity.getSourceType(),
            entity.getTargetType(),
            entity.getSourceTopic(),
            entity.getTargetTopic(),
            entity.getSourceExchange(),
            entity.getTargetExchange(),
            entity.getSourceQueue(),
            entity.getTargetQueue(),
            entity.isEnabled(),
            entity.getFilterExpression(),
            entity.getTransformationRules(),
            entity.getRetryCount(),
            entity.getMaxRetries(),
            entity.getMessageCount(),
            entity.getErrorCount(),
            entity.getStatus(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
