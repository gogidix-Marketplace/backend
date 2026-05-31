package com.gogidix.sales.countrydashboard.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryComparisonGeneratedEvent {

    private String dashboardId;
    private String tenantId;
    private String countryCode;
    private String comparisonType;
    private Instant timestamp;
}
