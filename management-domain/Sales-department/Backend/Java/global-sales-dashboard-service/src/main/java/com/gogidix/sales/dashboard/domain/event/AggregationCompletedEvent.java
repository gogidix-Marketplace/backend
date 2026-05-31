package com.gogidix.sales.dashboard.domain.event;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AggregationCompletedEvent {

    private String aggregationId;
    private String tenantId;
    private String eventType;
    private Instant timestamp;
    private String eventId;
    private String dashboardId;
}
