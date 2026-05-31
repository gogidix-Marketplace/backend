package com.gogidix.management.executive.operations.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Operations aggregate root for Executive Domain
 * Represents a configured executive strategy with KPI widgets
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "coo-operations-service")
public class Operations extends BaseEntity {

    private String name;
    private String description;
    @Indexed
    private String ownerId;

    private List<Widget> widgets;

    private OperationsStatus status;
    private String layout;
    private String createdBy;
    private String updatedBy;

    public enum OperationsStatus {
        DRAFT, ACTIVE, ARCHIVED
    }

    @Data
    @lombok.Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Widget {
        private String id;
        private String name;
        private WidgetType type;
        private int position;
        private int row;
        private int column;
        private int width;
        private int height;
        private String config;
        private String dataSource;

        public enum WidgetType {
            KPI_CARD, CHART, TABLE, METRIC, TREND, GAUGE, PROGRESS
        }
    }

    public void addWidget(Widget widget) {
        if (this.widgets == null) {
            this.widgets = new ArrayList<>();
        }
        this.widgets.add(widget);
        this.updatedAt = Instant.now();
    }

    public void removeWidget(String widgetId) {
        if (this.widgets != null) {
            this.widgets.removeIf(w -> {
                String wId = w.getId();
                return wId != null && wId.equals(widgetId);
            });
            this.updatedAt = Instant.now();
        }
    }

    public List<Widget> getWidgets() {
        if (this.widgets == null) {
            this.widgets = new ArrayList<>();
        }
        return this.widgets;
    }

    public boolean isActive() {
        return status == OperationsStatus.ACTIVE && deletedAt == null;
    }
}
