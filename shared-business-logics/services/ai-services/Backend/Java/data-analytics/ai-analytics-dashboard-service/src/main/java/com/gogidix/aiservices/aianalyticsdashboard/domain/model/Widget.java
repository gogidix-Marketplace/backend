package com.gogidix.aiservices.aianalyticsdashboard.domain.model;

import java.util.Map;

public class Widget {
    private final String widgetId;
    private final WidgetType type;
    private final String title;
    private final String dataSource;
    private final Map<String, Object> config;
    private final int position;

    private Widget(Builder builder) {
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

    public String getWidgetId() { return widgetId; }
    public WidgetType getType() { return type; }
    public String getTitle() { return title; }
    public String getDataSource() { return dataSource; }
    public Map<String, Object> getConfig() { return config; }
    public int getPosition() { return position; }

    public static class Builder {
        private String widgetId;
        private WidgetType type;
        private String title;
        private String dataSource;
        private Map<String, Object> config;
        private int position;

        public Builder widgetId(String widgetId) { this.widgetId = widgetId; return this; }
        public Builder type(WidgetType type) { this.type = type; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder dataSource(String dataSource) { this.dataSource = dataSource; return this; }
        public Builder config(Map<String, Object> config) { this.config = config; return this; }
        public Builder position(int position) { this.position = position; return this; }

        public Widget build() {
            if (widgetId == null || widgetId.trim().isEmpty()) {
                throw new IllegalArgumentException("Widget ID cannot be null or empty");
            }
            if (type == null) {
                throw new IllegalArgumentException("Widget type cannot be null");
            }
            return new Widget(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Widget widget = (Widget) o;
        return widgetId.equals(widget.widgetId);
    }

    @Override
    public int hashCode() {
        return widgetId.hashCode();
    }
}
