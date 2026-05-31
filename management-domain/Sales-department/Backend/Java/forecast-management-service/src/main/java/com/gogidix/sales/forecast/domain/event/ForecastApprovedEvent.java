package com.gogidix.sales.forecast.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastApprovedEvent {

    private String forecastId;
    private String tenantId;
    private String name;
    private String approvedBy;
    private String approvalLevel;
    private String eventType;
    private Instant timestamp;
    private String eventId;
}
