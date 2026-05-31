package com.gogidix.finance.forecasting.domain.event;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastApprovedEvent {

    private String forecastId;
    private String tenantId;
    private String forecastType;
    private String approvedBy;
    private String eventType;
    private Instant timestamp;
    private String eventId;
    private String eventKey;
}
