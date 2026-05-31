package com.gogidix.finance.globalfinancedashboard.infrastructure.persistence.mongodb;

import com.gogidix.finance.globalfinancedashboard.domain.model.DashboardView;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MongoDB document entity for storing DashboardView domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "dashboard_views")
public class DashboardViewEntity {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("widgets")
    private List<WidgetConfigEmbed> widgets;

    @Field("filters")
    private Map<String, Object> filters;

    @Field("owner_id")
    private String ownerId;

    @Field("is_public")
    private boolean isPublic;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    // Default constructor for MongoDB
    public DashboardViewEntity() {
    }

    // Constructor from domain model
    public DashboardViewEntity(DashboardView view) {
        this.id = view.getId();
        this.tenantId = view.getTenantId();
        this.name = view.getName();
        this.description = view.getDescription();
        this.widgets = convertWidgets(view.getWidgets());
        this.filters = view.getFilters() != null ? new HashMap<>(view.getFilters()) : new HashMap<>();
        this.ownerId = view.getOwnerId();
        this.isPublic = view.isPublic();
        this.createdAt = view.getCreatedAt();
        this.updatedAt = view.getUpdatedAt();
    }

    private List<WidgetConfigEmbed> convertWidgets(List<DashboardView.WidgetConfig> widgets) {
        if (widgets == null) {
            return new ArrayList<>();
        }
        List<WidgetConfigEmbed> embedList = new ArrayList<>();
        for (DashboardView.WidgetConfig widget : widgets) {
            embedList.add(new WidgetConfigEmbed(widget));
        }
        return embedList;
    }

    // Convert to domain model
    public DashboardView toDomainModel() {
        List<DashboardView.WidgetConfig> widgetConfigs = new ArrayList<>();
        if (this.widgets != null) {
            for (WidgetConfigEmbed embed : this.widgets) {
                widgetConfigs.add(embed.toDomainModel());
            }
        }

        DashboardView view = new DashboardView(this.tenantId, this.name, this.ownerId);
        view.setId(this.id);
        view.setDescription(this.description);
        view.setWidgets(widgetConfigs);
        view.setFilters(this.filters != null ? new HashMap<>(this.filters) : new HashMap<>());
        view.setPublic(this.isPublic);
        return view;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<WidgetConfigEmbed> getWidgets() { return widgets; }
    public void setWidgets(List<WidgetConfigEmbed> widgets) { this.widgets = widgets; }

    public Map<String, Object> getFilters() { return filters; }
    public void setFilters(Map<String, Object> filters) { this.filters = filters; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    /**
     * Embedded widget configuration
     */
    public static class WidgetConfigEmbed {
        private String widgetId;
        private String type;
        private String title;
        private int positionX;
        private int positionY;
        private int width;
        private int height;
        private Map<String, Object> config;

        public WidgetConfigEmbed() {
        }

        public WidgetConfigEmbed(DashboardView.WidgetConfig widgetConfig) {
            this.widgetId = widgetConfig.widgetId();
            this.type = widgetConfig.type();
            this.title = widgetConfig.title();
            this.positionX = widgetConfig.positionX();
            this.positionY = widgetConfig.positionY();
            this.width = widgetConfig.width();
            this.height = widgetConfig.height();
            this.config = widgetConfig.config() != null ? new HashMap<>(widgetConfig.config()) : new HashMap<>();
        }

        public DashboardView.WidgetConfig toDomainModel() {
            return new DashboardView.WidgetConfig(
                    widgetId, type, title, positionX, positionY, width, height,
                    config != null ? new HashMap<>(config) : new HashMap<>()
            );
        }

        public String getWidgetId() { return widgetId; }
        public void setWidgetId(String widgetId) { this.widgetId = widgetId; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public int getPositionX() { return positionX; }
        public void setPositionX(int positionX) { this.positionX = positionX; }

        public int getPositionY() { return positionY; }
        public void setPositionY(int positionY) { this.positionY = positionY; }

        public int getWidth() { return width; }
        public void setWidth(int width) { this.width = width; }

        public int getHeight() { return height; }
        public void setHeight(int height) { this.height = height; }

        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
    }
}
