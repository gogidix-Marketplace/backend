package com.gogidix.analytics.bi.domain.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Input port: Command to create a dashboard.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDashboardCommand {

    @NotBlank(message = "Dashboard name is required")
    private String name;

    private String description;

    private String category;

    @NotNull(message = "Owner ID is required")
    private String ownerId;

    @Builder.Default
    private Boolean isPublic = false;

    private Integer refreshIntervalSeconds;

    private String layoutConfig;

    @Builder.Default
    private String theme = "default";

    private String tags;

    private List<WidgetCommand> widgets;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WidgetCommand {
        @NotBlank(message = "Widget name is required")
        private String widgetName;

        @NotNull(message = "Widget type is required")
        private com.gogidix.analytics.bi.domain.model.DashboardWidget.WidgetType widgetType;

        private Integer position;

        private Integer rowIndex;

        private Integer columnIndex;

        private Integer rowSpan;

        private Integer columnSpan;

        private String dataSource;

        private String visualizationConfig;

        private String queryDefinition;

        private Integer refreshIntervalSeconds;

        @Builder.Default
        private Boolean enabled = true;
    }
}
