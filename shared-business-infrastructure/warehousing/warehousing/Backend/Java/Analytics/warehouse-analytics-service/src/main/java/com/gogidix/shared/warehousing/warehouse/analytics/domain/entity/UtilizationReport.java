package com.gogidix.shared.warehousing.warehouse.analytics.domain.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Utilization Report Entity - Multi-tenant with MongoDB
 *
 * Reports on space and resource utilization in warehouses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "utilization_reports")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'reportDate': 1}", name = "idx_tenant_warehouse_report")
public class UtilizationReport {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    private String warehouseName;

    @Indexed
    private LocalDateTime reportDate;

    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;

    // Zone utilization
    private List<ZoneUtilization> zoneUtilizations;

    // Overall utilization metrics
    private Double overallSpaceUtilization;
    private Double overallEquipmentUtilization;
    private Double overallLaborUtilization;

    // Capacity breakdown
    private CapacityBreakdown capacityBreakdown;

    // Trends
    private Double utilizationTrend;
    private String trendDirection;

    // Recommendations
    private List<String> recommendations;

    // Report metadata
    private ReportStatus status;
    private String generatedBy;

    private Map<String, Object> attributes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ZoneUtilization {
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
    public static class CapacityBreakdown {
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

    public enum ReportStatus {
        DRAFT,
        GENERATING,
        COMPLETED,
        FAILED
    }
}
