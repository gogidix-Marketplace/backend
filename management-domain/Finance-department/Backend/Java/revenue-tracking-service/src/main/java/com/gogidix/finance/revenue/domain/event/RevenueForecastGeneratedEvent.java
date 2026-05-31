package com.gogidix.finance.revenue.domain.event;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueForecastGeneratedEvent {

    private String forecastId;
    private String tenantId;
    private String forecastType;
    private Integer horizonMonths;
    private BigDecimal totalForecasted;
    private String currency;
    private String method;
    private Instant forecastDate;
    private String eventType;
    private Instant timestamp;
    private String eventId;
}
