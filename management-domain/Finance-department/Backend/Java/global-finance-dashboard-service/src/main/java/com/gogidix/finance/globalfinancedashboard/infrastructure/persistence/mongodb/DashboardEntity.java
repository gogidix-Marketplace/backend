package com.gogidix.finance.globalfinancedashboard.infrastructure.persistence.mongodb;

import com.gogidix.finance.globalfinancedashboard.domain.model.Dashboard;
import com.gogidix.finance.globalfinancedashboard.domain.model.Widget;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * MongoDB document entity for storing Dashboard domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "dashboards")
public class DashboardEntity {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    private String dashboardId;
    private String name;
    private String description;
    private String owner;
    private String ownerEmail;
    private String type;
    private String status;
    private Dashboard.LayoutConfig layout;
    private Dashboard.ThemeConfig theme;
    private List<WidgetEmbed> widgets;
    private String refreshInterval;
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

    // Default constructor for MongoDB
    public DashboardEntity() {
    }

    // Constructor from domain model
    public DashboardEntity(Dashboard dashboard) {
        this.id = dashboard.getId();
        this.tenantId = dashboard.getTenantId();
        this.dashboardId = dashboard.getDashboardId();
        this.name = dashboard.getName();
        this.description = dashboard.getDescription();
        this.owner = dashboard.getOwner();
        this.ownerEmail = dashboard.getOwnerEmail();
        this.type = dashboard.getType() != null ? dashboard.getType().name() : null;
        this.status = dashboard.getStatus() != null ? dashboard.getStatus().name() : null;
        this.layout = dashboard.getLayout();
        this.theme = dashboard.getTheme();
        this.widgets = convertWidgets(dashboard.getWidgets());
        this.refreshInterval = dashboard.getRefreshInterval() != null ? dashboard.getRefreshInterval().name() : null;
        this.isDefault = dashboard.getIsDefault();
        this.isPublic = dashboard.getIsPublic();
        this.allowSharing = dashboard.getAllowSharing();
        this.shareToken = dashboard.getShareToken();
        this.shareTokenExpiry = dashboard.getShareTokenExpiry();
        this.sharedWith = dashboard.getSharedWith() != null ? new HashSet<>(dashboard.getSharedWith()) : new HashSet<>();
        this.sharedWithGroups = dashboard.getSharedWithGroups() != null ? new HashSet<>(dashboard.getSharedWithGroups()) : new HashSet<>();
        this.createdBy = dashboard.getCreatedBy();
        this.createdAt = dashboard.getCreatedAt();
        this.updatedAt = dashboard.getUpdatedAt();
        this.lastAccessedAt = dashboard.getLastAccessedAt();
        this.viewCount = dashboard.getViewCount();
    }

    private List<WidgetEmbed> convertWidgets(List<Widget> widgets) {
        if (widgets == null) {
            return new ArrayList<>();
        }
        List<WidgetEmbed> embedList = new ArrayList<>();
        for (Widget widget : widgets) {
            // Widget is a separate aggregate, we only store references
            embedList.add(new WidgetEmbed(widget.getWidgetId(), widget.getPosition()));
        }
        return embedList;
    }

    // Convert to domain model
    public Dashboard toDomainModel() {
        return Dashboard.builder()
                .id(this.id)
                .tenantId(this.tenantId)
                .dashboardId(this.dashboardId)
                .name(this.name)
                .description(this.description)
                .owner(this.owner)
                .ownerEmail(this.ownerEmail)
                .type(this.type != null ? Dashboard.DashboardType.valueOf(this.type) : null)
                .status(this.status != null ? Dashboard.DashboardStatus.valueOf(this.status) : null)
                .layout(this.layout)
                .theme(this.theme)
                .refreshInterval(this.refreshInterval != null ? Dashboard.RefreshInterval.valueOf(this.refreshInterval) : null)
                .isDefault(this.isDefault)
                .isPublic(this.isPublic)
                .allowSharing(this.allowSharing)
                .shareToken(this.shareToken)
                .shareTokenExpiry(this.shareTokenExpiry)
                .sharedWith(this.sharedWith != null ? new HashSet<>(this.sharedWith) : new HashSet<>())
                .sharedWithGroups(this.sharedWithGroups != null ? new HashSet<>(this.sharedWithGroups) : new HashSet<>())
                .createdBy(this.createdBy)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .lastAccessedAt(this.lastAccessedAt)
                .viewCount(this.viewCount)
                .build();
    }

    // Embedded widget reference class
    public static class WidgetEmbed {
        private String widgetId;
        private Integer position;

        public WidgetEmbed() {
        }

        public WidgetEmbed(String widgetId, Integer position) {
            this.widgetId = widgetId;
            this.position = position;
        }

        public String getWidgetId() {
            return widgetId;
        }

        public void setWidgetId(String widgetId) {
            this.widgetId = widgetId;
        }

        public Integer getPosition() {
            return position;
        }

        public void setPosition(Integer position) {
            this.position = position;
        }
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getDashboardId() { return dashboardId; }
    public void setDashboardId(String dashboardId) { this.dashboardId = dashboardId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Dashboard.LayoutConfig getLayout() { return layout; }
    public void setLayout(Dashboard.LayoutConfig layout) { this.layout = layout; }

    public Dashboard.ThemeConfig getTheme() { return theme; }
    public void setTheme(Dashboard.ThemeConfig theme) { this.theme = theme; }

    public List<WidgetEmbed> getWidgets() { return widgets; }
    public void setWidgets(List<WidgetEmbed> widgets) { this.widgets = widgets; }

    public String getRefreshInterval() { return refreshInterval; }
    public void setRefreshInterval(String refreshInterval) { this.refreshInterval = refreshInterval; }

    public Boolean getIsDefault() { return isDefault; }
    public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }

    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }

    public Boolean getAllowSharing() { return allowSharing; }
    public void setAllowSharing(Boolean allowSharing) { this.allowSharing = allowSharing; }

    public String getShareToken() { return shareToken; }
    public void setShareToken(String shareToken) { this.shareToken = shareToken; }

    public Instant getShareTokenExpiry() { return shareTokenExpiry; }
    public void setShareTokenExpiry(Instant shareTokenExpiry) { this.shareTokenExpiry = shareTokenExpiry; }

    public Set<String> getSharedWith() { return sharedWith; }
    public void setSharedWith(Set<String> sharedWith) { this.sharedWith = sharedWith; }

    public Set<String> getSharedWithGroups() { return sharedWithGroups; }
    public void setSharedWithGroups(Set<String> sharedWithGroups) { this.sharedWithGroups = sharedWithGroups; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Instant getLastAccessedAt() { return lastAccessedAt; }
    public void setLastAccessedAt(Instant lastAccessedAt) { this.lastAccessedAt = lastAccessedAt; }

    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
}
