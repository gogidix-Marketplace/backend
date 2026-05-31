package com.gogidix.sales.crm.domain.event;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InteractionLoggedEvent {

    private String interactionId;
    private String tenantId;
    private String customerId;
    private String contactId;
    private String type;
    private String direction;
    private String outcome;
    private String eventType;
    private Instant timestamp;
}
