package com.gogidix.finance.globalfinancedashboard.domain.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardViewedEvent extends DashboardEvent {

    private String dashboardId;
    private String userId;

    public static DashboardViewedEvent create(String dashboardId, String tenantId, String userId,
                                                String arg4, String arg5, String arg6) {
        DashboardViewedEvent event = new DashboardViewedEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setEventType("DASHBOARD_VIEWED");
        event.setTenantId(tenantId);
        event.setTimestamp(Instant.now());
        event.setDashboardId(dashboardId);
        event.setUserId(userId);
        return event;
    }
}
