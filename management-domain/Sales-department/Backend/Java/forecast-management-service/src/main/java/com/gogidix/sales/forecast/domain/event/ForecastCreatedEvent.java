package com.gogidix.sales.forecast.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastCreatedEvent {

    private String forecastId;
    private String tenantId;
    private String name;
    private String period;
    private YearMonth startDate;
    private YearMonth endDate;
    private String createdBy;
    private String eventType;
    private String eventId;
}
