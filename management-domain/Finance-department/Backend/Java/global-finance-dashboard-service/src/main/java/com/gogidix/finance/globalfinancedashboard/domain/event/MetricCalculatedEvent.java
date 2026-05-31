package com.gogidix.finance.globalfinancedashboard.domain.event;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MetricCalculatedEvent extends DashboardEvent {

    private String metricId;
    private String widgetId;
    private String name;
    private String type;
    private BigDecimal value;
    private BigDecimal previousValue;
    private BigDecimal variance;
    private BigDecimal variancePercentage;
    private String trend;
}
