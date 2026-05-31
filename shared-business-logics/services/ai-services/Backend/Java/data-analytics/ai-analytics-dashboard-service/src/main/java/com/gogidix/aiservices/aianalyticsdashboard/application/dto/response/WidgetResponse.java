package com.gogidix.aiservices.aianalyticsdashboard.application.dto.response;

import com.gogidix.aiservices.aianalyticsdashboard.domain.model.WidgetType;

public class WidgetResponse {
    private String widgetId;
    private WidgetType type;
    private String title;
    private String dataSource;
    private Object config;
    private int position;

    private WidgetResponse() {
    }

    private WidgetResponse(Builder builder) {
        this.widgetId = builder.widgetId;
        this.type = builder.type;
        this.title = builder.title;
        this.dataSource = builder.dataSource;
        this.config = builder.config;
        this.position = builder.position;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getWidgetId() {
        return widgetId;
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

    public Object getConfig() {
        return config;
    }

    public int getPosition() {
        return position;
    }

    public static class Builder {
        private String widgetId;
        private WidgetType type;
        private String title;
        private String dataSource;
        private Object config;
        private int position;

        public Builder widgetId(String widgetId) {
            this.widgetId = widgetId;
            return this;
        }

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

        public Builder config(Object config) {
            this.config = config;
            return this;
        }

        public Builder position(int position) {
            this.position = position;
            return this;
        }

        public WidgetResponse build() {
            return new WidgetResponse(this);
        }
    }
}
