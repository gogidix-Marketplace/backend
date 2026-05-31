package com.gogidix.finance.consolidation.domain.event;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsolidationFailedEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private Instant timestamp;
}
