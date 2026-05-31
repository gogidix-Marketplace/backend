package com.gogidix.aiservices.aianalyticsdashboard.infrastructure.persistence;

import com.gogidix.aiservices.aianalyticsdashboard.domain.model.WidgetType;

import java.util.Map;

public class WidgetEntity {
    private String widgetId;
    private WidgetType type;
    private String title;
    private String dataSource;
    private Map<String, Object> config;
    private int position;

    public String getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
    }

    public WidgetType getType() {
        return type;
    }

    public void setType(WidgetType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
