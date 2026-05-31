package com.gogidix.sales.countrydashboard.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConversionAppliedEvent {

    private String dashboardId;
    private String tenantId;
    private String fromCurrency;
    private String toCurrency;
    private java.math.BigDecimal originalAmount;
    private java.math.BigDecimal convertedAmount;
    private java.math.BigDecimal exchangeRate;
    private java.time.Instant appliedAt;
}
