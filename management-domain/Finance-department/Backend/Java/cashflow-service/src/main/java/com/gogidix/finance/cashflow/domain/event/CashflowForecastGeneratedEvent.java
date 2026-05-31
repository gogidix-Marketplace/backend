package com.gogidix.finance.cashflow.domain.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashflowForecastGeneratedEvent {

    private String forecastId;
    private String tenantId;
    private String scenario;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal netCashflow;
    private String generatedBy;
    private String eventType;
    private Instant timestamp;
    private String eventId;
}
