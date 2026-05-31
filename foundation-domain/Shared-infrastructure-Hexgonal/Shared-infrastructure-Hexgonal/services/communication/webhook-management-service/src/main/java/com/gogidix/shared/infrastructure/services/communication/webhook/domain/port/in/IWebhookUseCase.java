package com.gogidix.shared.infrastructure.services.communication.webhook.domain.port.in;

import com.gogidix.shared.infrastructure.services.communication.webhook.application.dto.request.CreateWebhookRequestDto;
import com.gogidix.shared.infrastructure.services.communication.webhook.application.dto.request.UpdateWebhookRequestDto;
import com.gogidix.shared.infrastructure.services.communication.webhook.application.dto.response.WebhookResponseDto;

import java.util.List;

public interface IWebhookUseCase {
    WebhookResponseDto create(CreateWebhookRequestDto dto);
    WebhookResponseDto findById(String id);
    List<WebhookResponseDto> findAll();
    List<WebhookResponseDto> findByStatus(String status);
    List<WebhookResponseDto> findByEventType(String eventType);
    List<WebhookResponseDto> findByCreatedBy(String createdBy);
    WebhookResponseDto update(String id, UpdateWebhookRequestDto dto);
    void delete(String id);
    void activate(String id);
    void deactivate(String id);
    void trigger(String id);
}
