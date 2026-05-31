package com.gogidix.finance.globalfinancedashboard.domain.event;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardCreatedEvent extends DashboardEvent {

    private String name;

    private String dashboardId;
    private String type;
    private String owner;
}
