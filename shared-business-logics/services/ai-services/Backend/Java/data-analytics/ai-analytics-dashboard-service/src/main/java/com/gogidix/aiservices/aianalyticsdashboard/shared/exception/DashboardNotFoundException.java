package com.gogidix.aiservices.aianalyticsdashboard.shared.exception;

public class DashboardNotFoundException extends AnalyticsException {
    private final String dashboardId;

    public DashboardNotFoundException(String message) {
        super(message);
        this.dashboardId = null;
    }

    public DashboardNotFoundException(String dashboardId, boolean useId) {
        super("Dashboard not found: " + dashboardId);
        this.dashboardId = dashboardId;
    }

    public String getDashboardId() {
        return dashboardId;
    }
}
