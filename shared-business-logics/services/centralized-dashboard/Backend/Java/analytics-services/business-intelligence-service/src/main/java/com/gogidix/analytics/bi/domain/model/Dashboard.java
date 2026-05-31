package com.gogidix.analytics.bi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dashboards", indexes = {
    @Index(name = "idx_dashboard_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_dashboard_owner", columnList = "owner_id"),
    @Index(name = "idx_dashboard_is_public", columnList = "is_public"),
    @Index(name = "idx_dashboard_category", columnList = "category")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "slug", unique = true, length = 255)
    private String slug;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "owner_id", nullable = false, length = 100)
    private String ownerId;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "is_public", nullable = false)
    @Builder.Default
    private Boolean isPublic = false;

    @Column(name = "is_favorite", nullable = false)
    @Builder.Default
    private Boolean isFavorite = false;

    @Column(name = "refresh_interval_seconds")
    private Integer refreshIntervalSeconds;

    @Column(name = "layout_config", columnDefinition = "JSONB")
    private String layoutConfig;

    @Column(name = "theme", length = 50)
    @Builder.Default
    private String theme = "default";

    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_viewed_at")
    private LocalDateTime lastViewedAt;

    @Column(name = "view_count", nullable = false)
    @Builder.Default
    private Integer viewCount = 0;

    @OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    @Builder.Default
    private List<DashboardWidget> widgets = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
        if (slug == null) {
            slug = generateSlug(name);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addWidget(DashboardWidget widget) {
        widget.setDashboard(this);
        this.widgets.add(widget);
    }

    public void removeWidget(DashboardWidget widget) {
        widget.setDashboard(null);
        this.widgets.remove(widget);
    }

    public void incrementViewCount() {
        this.viewCount++;
        this.lastViewedAt = LocalDateTime.now();
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
            .replaceAll("[^a-z0-9]+", "-")
            .replaceAll("^-|-$", "") + "-" + System.currentTimeMillis();
    }
}
