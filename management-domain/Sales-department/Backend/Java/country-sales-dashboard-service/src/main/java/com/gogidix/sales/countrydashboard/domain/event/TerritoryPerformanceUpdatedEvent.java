package com.gogidix.sales.countrydashboard.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TerritoryPerformanceUpdatedEvent {

    private String dashboardId;
    private String tenantId;
    private String territoryId;
    private String territoryName;
    private String countryCode;
    private java.math.BigDecimal revenue;
    private java.math.BigDecimal quota;
    private java.math.BigDecimal achievementPercentage;
    private Integer rank;
    private java.time.Instant updatedAt;
}
