package com.gogidix.shared.warehousing.warehouse.analytics.application.dto;

import com.gogidix.shared.warehousing.warehouse.analytics.domain.entity.UtilizationReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Utilization Report Data Transfer Object
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UtilizationReportDTO {

    private String id;
    private String tenantId;
    private String warehouseId;
    private String warehouseName;
    private LocalDateTime reportDate;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;

    // Zone utilization
    private List<ZoneUtilizationDTO> zoneUtilizations;

    // Overall utilization metrics
    private Double overallSpaceUtilization;
    private Double overallEquipmentUtilization;
    private Double overallLaborUtilization;

    // Capacity breakdown
    private CapacityBreakdownDTO capacityBreakdown;

    // Trends
    private Double utilizationTrend;
    private String trendDirection;

    // Recommendations
    private List<String> recommendations;

    // Report metadata
    private String status;
    private String generatedBy;

    private Map<String, Object> attributes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ZoneUtilizationDTO {
        private String zoneId;
        private String zoneName;
        private String zoneType;
        private Double utilizationPercentage;
        private Double totalCapacity;
        private Double usedCapacity;
        private Double availableCapacity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CapacityBreakdownDTO {
        private Double storageCapacity;
        private Double storageUsed;
        private Double pickingCapacity;
        private Double pickingUsed;
        private Double packingCapacity;
        private Double packingUsed;
        private Double receivingCapacity;
        private Double receivingUsed;
        private Double shippingCapacity;
        private Double shippingUsed;
    }
}
