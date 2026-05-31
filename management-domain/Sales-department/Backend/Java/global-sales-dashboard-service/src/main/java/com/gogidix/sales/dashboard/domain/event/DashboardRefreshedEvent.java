package com.gogidix.sales.dashboard.domain.event;

import java.time.Instant;

public class DashboardRefreshedEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private String dashboardId;
    private Instant occurredAt;

    public DashboardRefreshedEvent() {}

    public DashboardRefreshedEvent(String eventId, String eventType, String tenantId, String dashboardId, Instant occurredAt) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.tenantId = tenantId;
        this.dashboardId = dashboardId;
        this.occurredAt = occurredAt;
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getDashboardId() { return dashboardId; }
    public void setDashboardId(String dashboardId) { this.dashboardId = dashboardId; }
    public Instant getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Instant occurredAt) { this.occurredAt = occurredAt; }

    public static DashboardRefreshedEvent completed(String dashboardId, String tenantId, Instant timestamp, String userId, int regionalMetricsCount, int widgetsCount) {
        DashboardRefreshedEvent event = new DashboardRefreshedEvent();
        event.eventId = java.util.UUID.randomUUID().toString();
        event.eventType = "DASHBOARD_REFRESHED";
        event.tenantId = tenantId;
        event.dashboardId = dashboardId;
        event.occurredAt = timestamp;
        return event;
    }
}
