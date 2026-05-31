package com.gogidix.finance.globalfinancedashboard.domain.model;

import com.gogidix.finance.globalfinancedashboard.domain.event.DashboardCreatedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.DashboardEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.DashboardSharedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.DashboardUpdatedEvent;
import com.gogidix.finance.globalfinancedashboard.shared.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Dashboard Domain Entity
 * Multi-tenant financial dashboard for visualizing metrics and analytics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document(collection = "dashboards")
public class Dashboard {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    private String dashboardId;

    private String name;

    private String description;

    private String owner;

    private String ownerEmail;

    private DashboardType type;

    private DashboardStatus status;

    private LayoutConfig layout;

    private ThemeConfig theme;

    @DBRef(lazy = true)
    @Builder.Default
    private List<Widget> widgets = new ArrayList<>();

    private RefreshInterval refreshInterval;

    private Boolean isDefault;

    private Boolean isPublic;

    private Boolean allowSharing;

    private String shareToken;

    private Instant shareTokenExpiry;

    private Set<String> sharedWith;

    private Set<String> sharedWithGroups;

    private String createdBy;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant lastAccessedAt;

    private Integer viewCount;

    @Builder.Default
    private List<DashboardEvent> domainEvents = new ArrayList<>();

    public enum DashboardType {
        EXECUTIVE_OVERVIEW,
        FINANCIAL_SUMMARY,
        REVENUE_ANALYTICS,
        EXPENSE_TRACKING,
        CASH_FLOW,
        PROFIT_LOSS,
        BALANCE_SHEET,
        BUDGET_VS_ACTUAL,
        FORECASTING,
        CUSTOM
    }

    public enum DashboardStatus {
        DRAFT,
        ACTIVE,
        ARCHIVED,
        DELETED
    }

    public enum RefreshInterval {
        NONE,
        MINUTES_5,
        MINUTES_15,
        MINUTES_30,
        HOUR_1,
        HOURS_6,
        HOURS_12,
        DAY_1,
        WEEK_1
    }

    /**
     * Creates a new dashboard
     */
    public static Dashboard create(String tenantId, String name, String description,
                                   String owner, String ownerEmail, DashboardType type) {
        Dashboard dashboard = Dashboard.builder()
                .dashboardId(generateDashboardId())
                .tenantId(Objects.requireNonNull(tenantId, "tenantId is required"))
                .name(Objects.requireNonNull(name, "name is required"))
                .description(description)
                .owner(Objects.requireNonNull(owner, "owner is required"))
                .ownerEmail(ownerEmail)
                .type(type != null ? type : DashboardType.CUSTOM)
                .status(DashboardStatus.DRAFT)
                .layout(LayoutConfig.createDefault())
                .theme(ThemeConfig.createDefault())
                .widgets(new ArrayList<>())
                .refreshInterval(RefreshInterval.MINUTES_15)
                .isDefault(false)
                .isPublic(false)
                .allowSharing(true)
                .sharedWith(new HashSet<>())
                .sharedWithGroups(new HashSet<>())
                .viewCount(0)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        dashboard.addDomainEvent(DashboardCreatedEvent.builder()
                .dashboardId(dashboard.getDashboardId())
                .tenantId(tenantId)
                .name(name)
                .type(type.name())
                .owner(owner)
                .timestamp(Instant.now())
                .eventType("DASHBOARD_CREATED")
                .build());

        return dashboard;
    }

    /**
     * Updates dashboard basic information
     */
    public void updateInfo(String name, String description, DashboardType type) {
        if (this.status == DashboardStatus.ARCHIVED || this.status == DashboardStatus.DELETED) {
            throw new IllegalStateException("Cannot update archived or deleted dashboard");
        }

        this.name = name != null ? name : this.name;
        this.description = description;
        if (type != null) {
            this.type = type;
        }
        this.updatedAt = Instant.now();

        addDomainEvent(DashboardUpdatedEvent.builder()
                .dashboardId(this.dashboardId)
                .tenantId(this.tenantId)
                .name(this.name)
                .timestamp(Instant.now())
                .eventType("DASHBOARD_INFO_UPDATED")
                .build());
    }

    /**
     * Adds a widget to the dashboard
     */
    public void addWidget(Widget widget) {
        if (this.status == DashboardStatus.ARCHIVED || this.status == DashboardStatus.DELETED) {
            throw new IllegalStateException("Cannot modify archived or deleted dashboard");
        }

        if (this.widgets == null) {
            this.widgets = new ArrayList<>();
        }

        if (this.widgets.size() >= 20) {
            throw new ValidationException("widgets", "Maximum 20 widgets allowed per dashboard");
        }

        widget.setDashboardId(this.dashboardId);
        widget.setTenantId(this.tenantId);
        widget.setPosition(this.widgets.size() + 1);
        this.widgets.add(widget);
        this.updatedAt = Instant.now();

        addDomainEvent(DashboardUpdatedEvent.builder()
                .dashboardId(this.dashboardId)
                .tenantId(this.tenantId)
                .widgetId(widget.getWidgetId())
                .timestamp(Instant.now())
                .eventType("WIDGET_ADDED")
                .build());
    }

    /**
     * Removes a widget from the dashboard
     */
    public void removeWidget(String widgetId) {
        if (this.status == DashboardStatus.ARCHIVED || this.status == DashboardStatus.DELETED) {
            throw new IllegalStateException("Cannot modify archived or deleted dashboard");
        }

        if (this.widgets != null) {
            this.widgets.removeIf(w -> w.getWidgetId().equals(widgetId));
            repositionWidgets();
            this.updatedAt = Instant.now();
        }

        addDomainEvent(DashboardUpdatedEvent.builder()
                .dashboardId(this.dashboardId)
                .tenantId(this.tenantId)
                .widgetId(widgetId)
                .timestamp(Instant.now())
                .eventType("WIDGET_REMOVED")
                .build());
    }

    /**
     * Updates widget position
     */
    public void updateWidgetPosition(String widgetId, int newPosition) {
        if (this.status == DashboardStatus.ARCHIVED || this.status == DashboardStatus.DELETED) {
            throw new IllegalStateException("Cannot modify archived or deleted dashboard");
        }

        if (this.widgets == null || newPosition < 1 || newPosition > this.widgets.size()) {
            throw new ValidationException("position", "Invalid position");
        }

        Widget widgetToMove = this.widgets.stream()
                .filter(w -> w.getWidgetId().equals(widgetId))
                .findFirst()
                .orElseThrow(() -> new ValidationException("widgetId", "Widget not found"));

        this.widgets.remove(widgetToMove);
        this.widgets.add(newPosition - 1, widgetToMove);
        repositionWidgets();
        this.updatedAt = Instant.now();
    }

    /**
     * Activates the dashboard
     */
    public void activate() {
        if (this.status == DashboardStatus.DELETED) {
            throw new IllegalStateException("Cannot activate deleted dashboard");
        }

        validateDashboard();
        this.status = DashboardStatus.ACTIVE;
        this.updatedAt = Instant.now();

        addDomainEvent(DashboardUpdatedEvent.builder()
                .dashboardId(this.dashboardId)
                .tenantId(this.tenantId)
                .timestamp(Instant.now())
                .eventType("DASHBOARD_ACTIVATED")
                .build());
    }

    /**
     * Archives the dashboard
     */
    public void archive() {
        if (this.status == DashboardStatus.DELETED) {
            throw new IllegalStateException("Cannot archive deleted dashboard");
        }

        this.status = DashboardStatus.ARCHIVED;
        this.updatedAt = Instant.now();

        addDomainEvent(DashboardUpdatedEvent.builder()
                .dashboardId(this.dashboardId)
                .tenantId(this.tenantId)
                .timestamp(Instant.now())
                .eventType("DASHBOARD_ARCHIVED")
                .build());
    }

    /**
     * Sets as default dashboard
     */
    public void setAsDefault() {
        if (this.status != DashboardStatus.ACTIVE) {
            throw new IllegalStateException("Only active dashboards can be set as default");
        }

        this.isDefault = true;
        this.updatedAt = Instant.now();
    }

    /**
     * Makes dashboard public
     */
    public void makePublic() {
        if (!this.allowSharing) {
            throw new IllegalStateException("Sharing is not enabled for this dashboard");
        }

        this.isPublic = true;
        this.updatedAt = Instant.now();
    }

    /**
     * Makes dashboard private
     */
    public void makePrivate() {
        this.isPublic = false;
        this.shareToken = null;
        this.shareTokenExpiry = null;
        this.updatedAt = Instant.now();
    }

    /**
     * Generates a share token for the dashboard
     */
    public String generateShareToken(int expiryHours) {
        if (!this.allowSharing) {
            throw new IllegalStateException("Sharing is not enabled for this dashboard");
        }

        this.shareToken = java.util.UUID.randomUUID().toString();
        this.shareTokenExpiry = Instant.now().plusSeconds(expiryHours * 3600L);
        this.updatedAt = Instant.now();

        addDomainEvent(DashboardSharedEvent.builder()
                .dashboardId(this.dashboardId)
                .tenantId(this.tenantId)
                .shareToken(this.shareToken)
                .expiresAt(this.shareTokenExpiry)
                .timestamp(Instant.now())
                .eventType("DASHBOARD_SHARED")
                .build());

        return this.shareToken;
    }

    /**
     * Shares dashboard with a user
     */
    public void shareWith(String userId) {
        if (!this.allowSharing) {
            throw new IllegalStateException("Sharing is not enabled for this dashboard");
        }

        if (this.sharedWith == null) {
            this.sharedWith = new HashSet<>();
        }

        this.sharedWith.add(userId);
        this.updatedAt = Instant.now();

        addDomainEvent(DashboardSharedEvent.builder()
                .dashboardId(this.dashboardId)
                .tenantId(this.tenantId)
                .sharedWith(userId)
                .timestamp(Instant.now())
                .eventType("DASHBOARD_SHARED_WITH_USER")
                .build());
    }

    /**
     * Shares dashboard with a group
     */
    public void shareWithGroup(String groupId) {
        if (!this.allowSharing) {
            throw new IllegalStateException("Sharing is not enabled for this dashboard");
        }

        if (this.sharedWithGroups == null) {
            this.sharedWithGroups = new HashSet<>();
        }

        this.sharedWithGroups.add(groupId);
        this.updatedAt = Instant.now();

        addDomainEvent(DashboardSharedEvent.builder()
                .dashboardId(this.dashboardId)
                .tenantId(this.tenantId)
                .sharedWithGroup(groupId)
                .timestamp(Instant.now())
                .eventType("DASHBOARD_SHARED_WITH_GROUP")
                .build());
    }

    /**
     * Removes share from a user
     */
    public void unshareFrom(String userId) {
        if (this.sharedWith != null) {
            this.sharedWith.remove(userId);
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Removes share from a group
     */
    public void unshareFromGroup(String groupId) {
        if (this.sharedWithGroups != null) {
            this.sharedWithGroups.remove(groupId);
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Checks if share token is valid
     */
    public boolean isShareTokenValid(String token) {
        return this.shareToken != null
                && this.shareToken.equals(token)
                && (this.shareTokenExpiry == null || this.shareTokenExpiry.isAfter(Instant.now()));
    }

    /**
     * Updates layout configuration
     */
    public void updateLayout(LayoutConfig layout) {
        if (this.status == DashboardStatus.ARCHIVED || this.status == DashboardStatus.DELETED) {
            throw new IllegalStateException("Cannot modify archived or deleted dashboard");
        }

        this.layout = layout;
        this.updatedAt = Instant.now();
    }

    /**
     * Updates theme configuration
     */
    public void updateTheme(ThemeConfig theme) {
        if (this.status == DashboardStatus.ARCHIVED || this.status == DashboardStatus.DELETED) {
            throw new IllegalStateException("Cannot modify archived or deleted dashboard");
        }

        this.theme = theme;
        this.updatedAt = Instant.now();
    }

    /**
     * Updates refresh interval
     */
    public void updateRefreshInterval(RefreshInterval interval) {
        this.refreshInterval = interval;
        this.updatedAt = Instant.now();
    }

    /**
     * Records dashboard access
     */
    public void recordAccess() {
        this.lastAccessedAt = Instant.now();
        if (this.viewCount == null) {
            this.viewCount = 0;
        }
        this.viewCount++;
    }

    /**
     * Checks if a user has access to the dashboard
     */
    public boolean hasAccess(String userId, Set<String> userGroupIds) {
        if (this.isPublic) {
            return true;
        }

        if (this.owner.equals(userId)) {
            return true;
        }

        if (this.sharedWith != null && this.sharedWith.contains(userId)) {
            return true;
        }

        if (userGroupIds != null && this.sharedWithGroups != null) {
            return userGroupIds.stream().anyMatch(this.sharedWithGroups::contains);
        }

        return false;
    }

    private void repositionWidgets() {
        for (int i = 0; i < this.widgets.size(); i++) {
            this.widgets.get(i).setPosition(i + 1);
        }
    }

    private void validateDashboard() {
        if (this.name == null || this.name.isBlank()) {
            throw new ValidationException("name", "Dashboard name is required");
        }
    }

    private static String generateDashboardId() {
        return "DASH-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public void addDomainEvent(DashboardEvent event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }

    /**
     * Layout configuration for dashboard widgets
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LayoutConfig {
        private String layoutType;
        private Integer columns;
        private Integer rowHeight;
        private Integer margin;
        private String padding;
        private Boolean isDraggable;
        private Boolean isResizable;

        public static LayoutConfig createDefault() {
            return LayoutConfig.builder()
                    .layoutType("GRID")
                    .columns(3)
                    .rowHeight(150)
                    .margin(10)
                    .padding("10px")
                    .isDraggable(true)
                    .isResizable(true)
                    .build();
        }
    }

    /**
     * Theme configuration for dashboard
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ThemeConfig {
        private String themeName;
        private String primaryColor;
        private String secondaryColor;
        private String backgroundColor;
        private String textColor;
        private Boolean darkMode;

        public static ThemeConfig createDefault() {
            return ThemeConfig.builder()
                    .themeName("default")
                    .primaryColor("#1976d2")
                    .secondaryColor("#dc004e")
                    .backgroundColor("#f5f5f5")
                    .textColor("#333333")
                    .darkMode(false)
                    .build();
        }
    }
}
