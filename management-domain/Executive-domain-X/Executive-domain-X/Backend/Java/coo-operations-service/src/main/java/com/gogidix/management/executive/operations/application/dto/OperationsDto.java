package com.gogidix.management.executive.operations.application.dto;

import com.gogidix.management.executive.operations.domain.model.Operations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;
/**
 * DTO for Operations responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationsDto {
    private String id;
    private String tenantId;
    private String name;
    private String description;
    private String ownerId;
    private List<WidgetDto> widgets;
    private Operations.OperationsStatus status;
    private String layout;
    private Instant createdAt;
    private Instant updatedAt;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WidgetDto {
        private String id;
        private String name;
        private Operations.Widget.WidgetType type;
        private int position;
        private int row;
        private int column;
        private int width;
        private int height;
        private String config;
        private String dataSource;
    }
}
