package com.gogidix.management.executive.audit.application.dto;

import com.gogidix.management.executive.audit.domain.model.Audit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;
/**
 * DTO for Audit responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditDto {
    private String id;
    private String tenantId;
    private String name;
    private String description;
    private String ownerId;
    private List<WidgetDto> widgets;
    private Audit.AuditStatus status;
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
        private Audit.Widget.WidgetType type;
        private int position;
        private int row;
        private int column;
        private int width;
        private int height;
        private String config;
        private String dataSource;
    }
}
