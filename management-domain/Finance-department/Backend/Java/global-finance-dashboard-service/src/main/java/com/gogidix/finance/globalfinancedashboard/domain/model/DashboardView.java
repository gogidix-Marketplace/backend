package com.gogidix.finance.globalfinancedashboard.domain.model;

import com.gogidix.finance.globalfinancedashboard.shared.exception.ValidationException;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * Domain Entity - Dashboard View
 * Represents a configured dashboard view for financial metrics
 */
@Data
@Document(collection = "dashboard_views")
public class DashboardView {

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
    private java.util.List<WidgetConfig> widgets;

    @Field("filters")
    private java.util.Map<String, Object> filters;

    @Field("owner_id")
    private String ownerId;

    @Field("is_public")
    private boolean isPublic;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    public record WidgetConfig(
        String widgetId,
        String type,
        String title,
        int positionX,
        int positionY,
        int width,
        int height,
        java.util.Map<String, Object> config
    ) {}

    /**
     * Create new dashboard view
     */
    public DashboardView(String tenantId, String name, String ownerId) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.name = Objects.requireNonNull(name, "name is required");
        this.ownerId = Objects.requireNonNull(ownerId, "ownerId is required");
        this.widgets = new java.util.ArrayList<>();
        this.filters = new java.util.HashMap<>();
        this.isPublic = false;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    protected DashboardView() {}

    // Getters
    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public java.util.List<WidgetConfig> getWidgets() { return widgets; }
    public java.util.Map<String, Object> getFilters() { return filters; }
    public String getOwnerId() { return ownerId; }
    public boolean isPublic() { return isPublic; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    // Setters
    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = Instant.now();
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
        this.updatedAt = Instant.now();
    }

    /**
     * Domain logic: add widget
     */
    public void addWidget(WidgetConfig widget) {
        if (widget == null) {
            throw new ValidationException("widget", "cannot be null");
        }
        this.widgets.add(widget);
        this.updatedAt = Instant.now();
    }

    /**
     * Domain logic: remove widget
     */
    public void removeWidget(String widgetId) {
        this.widgets.removeIf(w -> w.widgetId().equals(widgetId));
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DashboardView that = (DashboardView) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
