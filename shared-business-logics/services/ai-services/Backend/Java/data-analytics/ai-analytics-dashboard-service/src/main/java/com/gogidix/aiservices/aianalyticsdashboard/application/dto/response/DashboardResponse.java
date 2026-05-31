package com.gogidix.aiservices.aianalyticsdashboard.application.dto.response;

import com.gogidix.aiservices.aianalyticsdashboard.domain.model.WidgetType;

import java.time.Instant;
import java.util.List;

public class DashboardResponse {
    private String dashboardId;
    private String userId;
    private String name;
    private String description;
    private List<WidgetResponse> widgets;
    private int refreshInterval;
    private boolean isPublic;
    private String theme;
    private Instant createdAt;
    private Instant updatedAt;

    private DashboardResponse() {
    }

    private DashboardResponse(Builder builder) {
        this.dashboardId = builder.dashboardId;
        this.userId = builder.userId;
        this.name = builder.name;
        this.description = builder.description;
        this.widgets = builder.widgets;
        this.refreshInterval = builder.refreshInterval;
        this.isPublic = builder.isPublic;
        this.theme = builder.theme;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getDashboardId() {
        return dashboardId;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<WidgetResponse> getWidgets() {
        return widgets;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public String getTheme() {
        return theme;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public static class Builder {
        private String dashboardId;
        private String userId;
        private String name;
        private String description;
        private List<WidgetResponse> widgets;
        private int refreshInterval;
        private boolean isPublic;
        private String theme;
        private Instant createdAt;
        private Instant updatedAt;

        public Builder dashboardId(String dashboardId) {
            this.dashboardId = dashboardId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder widgets(List<WidgetResponse> widgets) {
            this.widgets = widgets;
            return this;
        }

        public Builder refreshInterval(int refreshInterval) {
            this.refreshInterval = refreshInterval;
            return this;
        }

        public Builder isPublic(boolean isPublic) {
            this.isPublic = isPublic;
            return this;
        }

        public Builder theme(String theme) {
            this.theme = theme;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public DashboardResponse build() {
            return new DashboardResponse(this);
        }
    }
}
