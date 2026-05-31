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
public class MessageSentEvent {

    private String messageId;
    private String conversationId;
    private String tenantId;
    private String senderId;
    private String subject;
    private String channel;
    private Integer recipientCount;
    private String externalMessageId;
    private Integer size;
    private Integer retryCount;
    private String errorMessage;
    private String readBy;
    private String eventType;
    private Instant timestamp;
}
