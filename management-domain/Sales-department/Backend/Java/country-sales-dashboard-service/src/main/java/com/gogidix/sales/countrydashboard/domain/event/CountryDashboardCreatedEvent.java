package com.gogidix.sales.countrydashboard.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryDashboardCreatedEvent {

    private String dashboardId;
    private String tenantId;
    private String countryCode;
    private String countryName;
    private String baseCurrency;
    private java.time.Instant createdAt;
}
