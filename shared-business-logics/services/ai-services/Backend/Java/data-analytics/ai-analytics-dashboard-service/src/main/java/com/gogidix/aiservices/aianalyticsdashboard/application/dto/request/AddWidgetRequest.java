package com.gogidix.aiservices.aianalyticsdashboard.application.dto.request;

import com.gogidix.aiservices.aianalyticsdashboard.domain.model.WidgetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public class AddWidgetRequest {
    @NotNull(message = "Widget type is required")
    private WidgetType type;

    @NotBlank(message = "Widget title is required")
    private String title;

    @NotBlank(message = "Data source is required")
    private String dataSource;

    private Map<String, Object> config;

    private Integer position;

    private AddWidgetRequest() {
    }

    private AddWidgetRequest(Builder builder) {
        this.type = builder.type;
        this.title = builder.title;
        this.dataSource = builder.dataSource;
        this.config = builder.config;
        this.position = builder.position;
    }

    public static Builder builder() {
        return new Builder();
    }

    public WidgetType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDataSource() {
        return dataSource;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public Integer getPosition() {
        return position;
    }

    public static class Builder {
        private WidgetType type;
        private String title;
        private String dataSource;
        private Map<String, Object> config;
        private Integer position;

        public Builder type(WidgetType type) {
            this.type = type;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder dataSource(String dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public Builder config(Map<String, Object> config) {
            this.config = config;
            return this;
        }

        public Builder position(Integer position) {
            this.position = position;
            return this;
        }

        public AddWidgetRequest build() {
            return new AddWidgetRequest(this);
        }
    }
}
