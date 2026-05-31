package com.gogidix.sales.dealmanagement.domain.event;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DealStageChangedEvent {

    private String eventId;
    private String dealId;
    private String tenantId;
    private String previousStage;
    private String newStage;
    private Integer probability;
    private String changedBy;
    private String eventType;
    private Instant timestamp;
}
