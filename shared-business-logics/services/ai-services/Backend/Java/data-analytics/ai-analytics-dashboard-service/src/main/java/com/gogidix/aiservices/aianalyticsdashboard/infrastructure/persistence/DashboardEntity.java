package com.gogidix.aiservices.aianalyticsdashboard.infrastructure.persistence;

import com.gogidix.aiservices.aianalyticsdashboard.domain.model.WidgetType;

import java.time.Instant;
import java.util.List;

public class DashboardEntity {
    private String dashboardId;
    private String userId;
    private String name;
    private String description;
    private List<WidgetEntity> widgets;
    private int refreshInterval;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isPublic;
    private String theme;

    public String getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(String dashboardId) {
        this.dashboardId = dashboardId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<WidgetEntity> getWidgets() {
        return widgets;
    }

    public void setWidgets(List<WidgetEntity> widgets) {
        this.widgets = widgets;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
