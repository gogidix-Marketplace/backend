package com.gogidix.aiservices.aianalyticsdashboard.domain.model;

import java.time.Instant;
import java.util.*;

public class Dashboard {
    private static final int MAX_WIDGETS = 20;
    private static final int MIN_REFRESH_INTERVAL = 30;
    private static final int MAX_REFRESH_INTERVAL = 86400; // 24 hours

    private final String dashboardId;
    private final String userId;
    private final String name;
    private String description;
    private final List<Widget> widgets;
    private int refreshInterval;
    private final Instant createdAt;
    private Instant updatedAt;

    private Dashboard(String name, String userId) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Dashboard name cannot be null or empty");
        }
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        this.dashboardId = UUID.randomUUID().toString();
        this.userId = userId;
        this.name = name;
        this.description = null;
        this.widgets = new ArrayList<>();
        this.refreshInterval = 300; // 5 minutes default
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public static Dashboard create(String name, String userId) {
        return new Dashboard(name, userId);
    }

    public String getDashboardId() { return dashboardId; }
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<Widget> getWidgets() { return Collections.unmodifiableList(widgets); }
    public int getRefreshInterval() { return refreshInterval; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setDescription(String description) {
        this.description = description;
        touch();
    }

    public void setRefreshInterval(int interval) {
        if (interval < MIN_REFRESH_INTERVAL) {
            throw new IllegalArgumentException("Refresh interval must be at least " + MIN_REFRESH_INTERVAL + " seconds");
        }
        if (interval > MAX_REFRESH_INTERVAL) {
            throw new IllegalArgumentException("Refresh interval cannot exceed " + MAX_REFRESH_INTERVAL + " seconds");
        }
        this.refreshInterval = interval;
        touch();
    }

    public void addWidget(Widget widget) {
        if (widget == null) {
            throw new IllegalArgumentException("Widget cannot be null");
        }
        if (widgets.size() >= MAX_WIDGETS) {
            throw new IllegalStateException("Dashboard cannot have more than " + MAX_WIDGETS + " widgets");
        }
        widgets.add(widget);
        touch();
    }

    public void removeWidget(String widgetId) {
        widgets.removeIf(w -> w.getWidgetId().equals(widgetId));
        touch();
    }

    public void updateWidget(Widget widget) {
        for (int i = 0; i < widgets.size(); i++) {
            if (widgets.get(i).getWidgetId().equals(widget.getWidgetId())) {
                widgets.set(i, widget);
                touch();
                return;
            }
        }
        throw new IllegalArgumentException("Widget not found: " + widget.getWidgetId());
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }
}
