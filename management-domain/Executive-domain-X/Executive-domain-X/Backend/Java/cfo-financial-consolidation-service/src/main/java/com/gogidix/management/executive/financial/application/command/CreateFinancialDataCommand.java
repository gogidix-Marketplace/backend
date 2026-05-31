package com.gogidix.management.executive.financial.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Command for creating a new financials
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFinancialDataCommand {

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    @NotBlank(message = "FinancialData name is required")
    private String name;

    private String description;

    @NotBlank(message = "Owner ID is required")
    private String ownerId;

    private String layout;

    private List<WidgetConfig> widgets;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WidgetConfig {
        private String name;
        private String type;
        private int position;
        private int row;
        private int column;
        private int width;
        private int height;
        private String config;
        private String dataSource;
    }
}
