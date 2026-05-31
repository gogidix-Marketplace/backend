package com.gogidix.aiservices.aianalyticsdashboard.application.dto.request;

import com.gogidix.aiservices.aianalyticsdashboard.domain.model.WidgetType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateDashboardRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @Min(value = 30, message = "Refresh interval must be at least 30 seconds")
    @Max(value = 86400, message = "Refresh interval cannot exceed 86400 seconds")
    private Integer refreshInterval;

    private boolean isPublic = false;

    private String theme;

    private List<AddWidgetRequest> widgets;

    private CreateDashboardRequest() {
    }

    private CreateDashboardRequest(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.refreshInterval = builder.refreshInterval;
        this.isPublic = builder.isPublic;
        this.theme = builder.theme;
        this.widgets = builder.widgets;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getRefreshInterval() {
        return refreshInterval;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public String getTheme() {
        return theme;
    }

    public List<AddWidgetRequest> getWidgets() {
        return widgets;
    }

    public static class Builder {
        private String name;
        private String description;
        private Integer refreshInterval;
        private boolean isPublic = false;
        private String theme;
        private List<AddWidgetRequest> widgets;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder refreshInterval(Integer refreshInterval) {
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

        public Builder widgets(List<AddWidgetRequest> widgets) {
            this.widgets = widgets;
            return this;
        }

        public CreateDashboardRequest build() {
            return new CreateDashboardRequest(this);
        }
    }
}
