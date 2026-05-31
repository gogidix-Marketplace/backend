package com.gogidix.sales.leadmanagement.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadDeletedEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private Instant occurredAt;
    private String correlationId;
}
