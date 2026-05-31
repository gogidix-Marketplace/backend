package com.gogidix.shared.infrastructure.services.communication.sms.application.mapper;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.communication.sms.application.dto.request.CreateSmsMessageRequestDto;
import com.gogidix.shared.infrastructure.services.communication.sms.application.dto.response.SmsMessageResponseDto;
import com.gogidix.shared.infrastructure.services.communication.sms.domain.model.SmsMessage;
import org.springframework.stereotype.Component;

/**
 * Mapper for SmsMessage entity
 */
@Component
public class SmsMessageMapper {

    public SmsMessage toEntity(CreateSmsMessageRequestDto dto, String tenantId) {
        TenantId tid = new TenantId(tenantId);
        SmsMessage entity = new SmsMessage(tid, dto.phoneNumber(), dto.message());
        entity.setCountryCode(dto.countryCode());
        entity.setTemplateName(dto.templateName());
        entity.setProvider(dto.provider() != null ? dto.provider() : "TWILIO");
        entity.setCampaignId(dto.campaignId());
        return entity;
    }

    public SmsMessageResponseDto toResponseDto(SmsMessage entity) {
        return new SmsMessageResponseDto(
            entity.getId(),
            entity.getTenantId() != null ? entity.getTenantId().getValue() : null,
            entity.getPhoneNumber(),
            entity.getCountryCode(),
            entity.getStatus(),
            entity.getMessage(),
            entity.getTemplateName(),
            entity.getProvider(),
            entity.getCampaignId(),
            entity.getExternalMessageId(),
            entity.getErrorMessage(),
            entity.getRetryCount(),
            entity.getMaxRetries(),
            entity.getCreatedAt(),
            entity.getSentAt(),
            entity.getDeliveredAt()
        );
    }
}
