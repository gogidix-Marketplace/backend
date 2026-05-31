package com.gogidix.digitalmarketing.analytics.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsReportUpdatedEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private Instant occurredAt;
    private String correlationId;
}
