package com.gogidix.sales.forecast.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastUpdatedEvent {

    private String forecastId;
    private String tenantId;
    private String name;
    private String status;
    private String rejectionReason;
    private String eventType;
    private String eventId;
}
