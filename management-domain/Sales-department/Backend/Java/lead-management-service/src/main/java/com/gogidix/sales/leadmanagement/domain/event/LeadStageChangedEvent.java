package com.gogidix.sales.leadmanagement.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadStageChangedEvent {

    private String leadId;
    private String tenantId;
    private String previousStage;
    private String newStage;
    private String eventType;
    private java.time.Instant timestamp;
    private String eventId;
}
