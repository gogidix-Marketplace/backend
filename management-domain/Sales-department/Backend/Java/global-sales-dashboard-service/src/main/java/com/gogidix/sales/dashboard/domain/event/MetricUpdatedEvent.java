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
public class MetricUpdatedEvent {

    private String dashboardId;
    private String tenantId;
    private String regionCode;
    private String eventType;
    private Instant timestamp;
    private String eventId;
}
