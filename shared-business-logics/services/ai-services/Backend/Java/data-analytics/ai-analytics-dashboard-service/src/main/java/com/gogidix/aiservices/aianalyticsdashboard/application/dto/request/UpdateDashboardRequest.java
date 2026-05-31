package com.gogidix.aiservices.aianalyticsdashboard.application.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class UpdateDashboardRequest {
    private String name;

    private String description;

    @Min(value = 30, message = "Refresh interval must be at least 30 seconds")
    @Max(value = 86400, message = "Refresh interval cannot exceed 86400 seconds")
    private Integer refreshInterval;

    private Boolean isPublic;

    private String theme;

    private UpdateDashboardRequest() {
    }

    private UpdateDashboardRequest(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.refreshInterval = builder.refreshInterval;
        this.isPublic = builder.isPublic;
        this.theme = builder.theme;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder()
                .name(this.name)
                .description(this.description)
                .refreshInterval(this.refreshInterval)
                .isPublic(this.isPublic)
                .theme(this.theme);
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

    public Boolean getIsPublic() {
        return isPublic;
    }

    public String getTheme() {
        return theme;
    }

    public static class Builder {
        private String name;
        private String description;
        private Integer refreshInterval;
        private Boolean isPublic;
        private String theme;

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

        public Builder isPublic(Boolean isPublic) {
            this.isPublic = isPublic;
            return this;
        }

        public Builder theme(String theme) {
            this.theme = theme;
            return this;
        }

        public UpdateDashboardRequest build() {
            return new UpdateDashboardRequest(this);
        }
    }
}
