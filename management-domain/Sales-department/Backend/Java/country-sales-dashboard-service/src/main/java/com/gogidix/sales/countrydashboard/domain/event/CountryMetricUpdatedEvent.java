package com.gogidix.sales.countrydashboard.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder")
@NoArgsConstructor
@AllArgsConstructor
public class CountryMetricUpdatedEvent {

    private String dashboardId;
    private String tenantId;
    private String countryCode;
    private String metricType;
    private java.math.BigDecimal currentValue;
    private java.math.BigDecimal previousValue;
    private java.math.BigDecimal changePercentage;
    private java.time.Instant updatedAt;
}
