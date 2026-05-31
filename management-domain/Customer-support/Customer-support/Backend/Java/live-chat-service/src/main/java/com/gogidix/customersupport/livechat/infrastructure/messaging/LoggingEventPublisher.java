package com.gogidix.customersupport.livechat.infrastructure.messaging;

import com.gogidix.customersupport.livechat.domain.event.*;
import com.gogidix.customersupport.livechat.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishChatMessageCreated(ChatMessageCreatedEvent event) {
        log.info("ChatMessage created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishChatMessageUpdated(ChatMessageUpdatedEvent event) {
        log.info("ChatMessage updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishChatMessageDeleted(ChatMessageDeletedEvent event) {
        log.info("ChatMessage deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishChatSessionCreated(ChatSessionCreatedEvent event) {
        log.info("ChatSession created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishChatSessionUpdated(ChatSessionUpdatedEvent event) {
        log.info("ChatSession updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishChatSessionDeleted(ChatSessionDeletedEvent event) {
        log.info("ChatSession deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
