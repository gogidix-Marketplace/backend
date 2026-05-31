package com.gogidix.management.executive.analytics.application.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 * Dashboard KPI Data Transfer Object
 */
@Data
public class KPIDashboardDTO {
    private String executiveLevel;
    private String category;
    private String period;
    private List<KPIDashboardItemDTO> kpis;
    private Map<String, Object> summary;
    private int totalKPIs;
    private int onTrack;
    private int needsAttention;
    @Data
    public static class KPIDashboardItemDTO {
        private String id;
        private String name;
        private BigDecimal value;
        private String unit;
        private BigDecimal percentChange;
        private String status;
        private String trend;
        private Boolean visible;
    }
}
