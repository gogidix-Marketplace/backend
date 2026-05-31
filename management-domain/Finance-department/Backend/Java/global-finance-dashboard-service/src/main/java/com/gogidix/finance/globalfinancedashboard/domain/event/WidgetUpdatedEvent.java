package com.gogidix.finance.globalfinancedashboard.domain.event;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class WidgetUpdatedEvent extends DashboardEvent {

    private String name;

    private String widgetId;
    private String dashboardId;
}
