package com.gogidix.finance.globalfinancedashboard.domain.event;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSharedEvent extends DashboardEvent {

    private String dashboardId;
    private String sharedWith;
    private String sharedWithGroup;
    private String shareToken;
    private Instant expiresAt;
}
