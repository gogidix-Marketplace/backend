package com.gogidix.finance.globalfinancedashboard.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gogidix.finance.globalfinancedashboard.domain.model.DashboardView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Dashboard View Response DTO
 * Represents a dashboard view response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardViewResponseDto {

    private String id;
    private String tenantId;
    private String name;
    private String description;
    private List<WidgetConfigDto> widgets;
    private Map<String, Object> filters;
    private String ownerId;
    private boolean isPublic;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    /**
     * Widget configuration DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class WidgetConfigDto {
        private String widgetId;
        private String type;
        private String title;
        private int positionX;
        private int positionY;
        private int width;
        private int height;
        private Map<String, Object> config;
    }

    /**
     * Converts from domain entity to DTO
     */
    public static DashboardViewResponseDto fromEntity(DashboardView view) {
        return DashboardViewResponseDto.builder()
                .id(view.getId())
                .tenantId(view.getTenantId())
                .name(view.getName())
                .description(view.getDescription())
                .widgets(view.getWidgets() != null
                        ? view.getWidgets().stream()
                            .map(w -> WidgetConfigDto.builder()
                                    .widgetId(w.widgetId())
                                    .type(w.type())
                                    .title(w.title())
                                    .positionX(w.positionX())
                                    .positionY(w.positionY())
                                    .width(w.width())
                                    .height(w.height())
                                    .config(w.config())
                                    .build())
                            .toList()
                        : List.of())
                .filters(view.getFilters())
                .ownerId(view.getOwnerId())
                .isPublic(view.isPublic())
                .createdAt(view.getCreatedAt())
                .updatedAt(view.getUpdatedAt())
                .build();
    }
}
