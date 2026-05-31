package com.gogidix.shared.infrastructure.services.communication.email.application.mapper;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.communication.email.application.dto.request.CreateEmailMessageRequestDto;
import com.gogidix.shared.infrastructure.services.communication.email.application.dto.response.EmailMessageResponseDto;
import com.gogidix.shared.infrastructure.services.communication.email.domain.model.EmailMessage;
import org.springframework.stereotype.Component;

/**
 * Mapper for EmailMessage entity
 */
@Component
public class EmailMessageMapper {

    public EmailMessage toEntity(CreateEmailMessageRequestDto dto, String tenantId) {
        TenantId tid = new TenantId(tenantId);
        EmailMessage entity = new EmailMessage(tid, dto.to(), dto.subject(), dto.body());
        entity.setCc(dto.cc());
        entity.setBcc(dto.bcc());
        entity.setTemplateName(dto.templateName());
        entity.setProvider(dto.provider() != null ? dto.provider() : "SMTP");
        entity.setCampaignId(dto.campaignId());
        return entity;
    }

    public EmailMessageResponseDto toResponseDto(EmailMessage entity) {
        return new EmailMessageResponseDto(
            entity.getId(),
            entity.getTenantId() != null ? entity.getTenantId().getValue() : null,
            entity.getTo(),
            entity.getCc(),
            entity.getBcc(),
            entity.getSubject(),
            entity.getBody(),
            entity.getTemplateName(),
            entity.getStatus(),
            entity.getRetryCount(),
            entity.getMaxRetries(),
            entity.getErrorMessage(),
            entity.getProvider(),
            entity.getCampaignId(),
            entity.getCreatedAt(),
            entity.getSentAt()
        );
    }
}
