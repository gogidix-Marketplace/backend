package com.gogidix.aiservices.aianalyticsdashboard.domain.aggregate;

import com.gogidix.aiservices.aianalyticsdashboard.domain.model.Widget;

import java.time.Instant;
import java.util.*;

public class Dashboard {
    private static final int MAX_WIDGETS = 20;
    private static final int MIN_REFRESH_INTERVAL = 30;
    private static final int MAX_REFRESH_INTERVAL = 86400;

    private String dashboardId;
    private final String userId;
    private final String name;
    private String description;
    private final List<Widget> widgets;
    private int refreshInterval;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isPublic;
    private String theme;

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
        this.refreshInterval = 300;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.isPublic = false;
        this.theme = "default";
    }

    public static Dashboard create(String name, String userId) {
        return new Dashboard(name, userId);
    }

    public static Dashboard restore(String dashboardId, String userId, String name,
                                   String description, List<Widget> widgets,
                                   int refreshInterval, Instant createdAt,
                                   Instant updatedAt, boolean isPublic, String theme) {
        Dashboard dashboard = new Dashboard(name, userId);
        dashboard.dashboardId = dashboardId;
        dashboard.description = description;
        dashboard.widgets.addAll(widgets);
        dashboard.refreshInterval = refreshInterval;
        dashboard.createdAt = createdAt;
        dashboard.updatedAt = updatedAt;
        dashboard.isPublic = isPublic;
        dashboard.theme = theme;
        return dashboard;
    }

    // Getters
    public String getDashboardId() { return dashboardId; }
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<Widget> getWidgets() { return Collections.unmodifiableList(widgets); }
    public int getRefreshInterval() { return refreshInterval; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public boolean isPublic() { return isPublic; }
    public String getTheme() { return theme; }

    // Setters with validation
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

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
        touch();
    }

    public void setTheme(String theme) {
        if (theme == null || theme.trim().isEmpty()) {
            throw new IllegalArgumentException("Theme cannot be null or empty");
        }
        this.theme = theme;
        touch();
    }

    // Widget management
    public void addWidget(Widget widget) {
        if (widget == null) {
            throw new IllegalArgumentException("Widget cannot be null");
        }
        if (widgets.size() >= MAX_WIDGETS) {
            throw new IllegalStateException("Dashboard cannot have more than " + MAX_WIDGETS + " widgets");
        }
        if (widgets.stream().anyMatch(w -> w.getWidgetId().equals(widget.getWidgetId()))) {
            throw new IllegalStateException("Widget with ID " + widget.getWidgetId() + " already exists");
        }
        widgets.add(widget);
        touch();
    }

    public void removeWidget(String widgetId) {
        boolean removed = widgets.removeIf(w -> w.getWidgetId().equals(widgetId));
        if (removed) {
            touch();
        }
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

    public void reorderWidgets(List<String> widgetIds) {
        if (widgetIds.size() != widgets.size()) {
            throw new IllegalArgumentException("Widget ID list must match current widgets");
        }

        Map<String, Widget> widgetMap = new HashMap<>();
        for (Widget w : widgets) {
            widgetMap.put(w.getWidgetId(), w);
        }

        widgets.clear();
        for (String id : widgetIds) {
            Widget w = widgetMap.get(id);
            if (w == null) {
                throw new IllegalArgumentException("Widget not found: " + id);
            }
            widgets.add(w);
        }
        touch();
    }

    public Widget getWidget(String widgetId) {
        return widgets.stream()
                .filter(w -> w.getWidgetId().equals(widgetId))
                .findFirst()
                .orElse(null);
    }

    public int getWidgetCount() {
        return widgets.size();
    }

    public boolean isFull() {
        return widgets.size() >= MAX_WIDGETS;
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dashboard dashboard = (Dashboard) o;
        return dashboardId.equals(dashboard.dashboardId);
    }

    @Override
    public int hashCode() {
        return dashboardId.hashCode();
    }
}
