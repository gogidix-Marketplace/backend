package com.gogidix.sales.countrydashboard.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryQuotaAdjustedEvent {

    private String dashboardId;
    private String tenantId;
    private String countryCode;
    private java.math.BigDecimal previousQuota;
    private java.math.BigDecimal newQuota;
    private String reason;
    private java.time.Instant adjustedAt;
}
