package com.gogidix.management.executive.financial.application.dto;

import com.gogidix.management.executive.financial.domain.model.FinancialData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;
/**
 * DTO for FinancialData responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialDataDto {
    private String id;
    private String tenantId;
    private String name;
    private String description;
    private String ownerId;
    private List<WidgetDto> widgets;
    private FinancialData.FinancialDataStatus status;
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
        private FinancialData.Widget.WidgetType type;
        private int position;
        private int row;
        private int column;
        private int width;
        private int height;
        private String config;
        private String dataSource;
    }
}
