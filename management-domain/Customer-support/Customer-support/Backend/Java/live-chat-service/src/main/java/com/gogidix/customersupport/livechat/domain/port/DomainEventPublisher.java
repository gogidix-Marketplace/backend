package com.gogidix.customersupport.livechat.domain.port;

import com.gogidix.customersupport.livechat.domain.event.*;

public interface DomainEventPublisher {
    void publishChatMessageCreated(ChatMessageCreatedEvent event);
    void publishChatMessageUpdated(ChatMessageUpdatedEvent event);
    void publishChatMessageDeleted(ChatMessageDeletedEvent event);
    void publishChatSessionCreated(ChatSessionCreatedEvent event);
    void publishChatSessionUpdated(ChatSessionUpdatedEvent event);
    void publishChatSessionDeleted(ChatSessionDeletedEvent event);
}
