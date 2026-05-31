package com.gogidix.shared.infrastructure.services.communication.webhook.application.mapper;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.communication.webhook.application.dto.request.CreateWebhookRequestDto;
import com.gogidix.shared.infrastructure.services.communication.webhook.application.dto.response.WebhookResponseDto;
import com.gogidix.shared.infrastructure.services.communication.webhook.domain.model.Webhook;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class WebhookMapper {
    public Webhook toEntity(CreateWebhookRequestDto dto, String tenantId) {
        Webhook webhook = new Webhook(new TenantId(tenantId), dto.name(), dto.url(),
            dto.eventType() != null ? dto.eventType() : "generic");
        webhook.setDescription(dto.description());
        webhook.setCreatedBy(dto.createdBy());
        if (dto.eventTypes() != null) webhook.setEventTypes(dto.eventTypes());
        if (dto.httpMethod() != null) webhook.setHttpMethod(dto.httpMethod());
        if (dto.secret() != null) webhook.setSecret(dto.secret());
        if (dto.retryAttempts() != null) webhook.setRetryAttempts(dto.retryAttempts());
        if (dto.retryDelay() != null) webhook.setRetryDelay(dto.retryDelay());
        if (dto.timeout() != null) webhook.setTimeout(dto.timeout());
        if (dto.sslVerificationEnabled() != null) webhook.setSslVerificationEnabled(dto.sslVerificationEnabled());
        return webhook;
    }

    public WebhookResponseDto toResponseDto(Webhook entity) {
        return new WebhookResponseDto(
            entity.getId(),
            entity.getTenantId() != null ? entity.getTenantId().getValue() : null,
            entity.getName(),
            entity.getDescription(),
            entity.getUrl(),
            entity.getStatus(),
            entity.getEventType(),
            entity.getEventTypes() != null ? entity.getEventTypes() : new ArrayList<>(),
            entity.getHttpMethod(),
            entity.getCreatedBy(),
            entity.getRetryAttempts(),
            entity.getRetryDelay(),
            entity.getTimeout(),
            entity.getSslVerificationEnabled(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
