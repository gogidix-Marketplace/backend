package com.gogidix.sales.communication.domain.event;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationCreatedEvent {

    private String conversationId;
    private String tenantId;
    private String ownerId;
    private String title;
    private String type;
    private String eventType;
    private Instant timestamp;
}
