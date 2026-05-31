package com.gogidix.sales.communication.domain.event;

import java.time.Instant;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationUpdatedEvent {

    private String conversationId;
    private String tenantId;
    private String updateType;
    private Map<String, Object> changes;
    private Instant timestamp;
}
