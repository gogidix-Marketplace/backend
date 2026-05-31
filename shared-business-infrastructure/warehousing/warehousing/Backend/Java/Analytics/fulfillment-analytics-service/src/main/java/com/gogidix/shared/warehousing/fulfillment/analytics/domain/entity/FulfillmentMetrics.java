package com.gogidix.shared.warehousing.fulfillment.analytics.domain.entity;

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
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "fulfillment_metrics")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'metricDate': 1}", name = "idx_tenant_warehouse_date")
public class FulfillmentMetrics {
    @Id
    private String id;
    @Indexed
    private String tenantId;
    @Indexed
    private String warehouseId;
    private String warehouseName;
    @Indexed
    private LocalDateTime metricDate;

    // Order processing metrics
    private Integer ordersProcessed;
    private Integer ordersPending;
    private Integer ordersCancelled;
    private Double orderCompletionRate;

    // Cycle time metrics
    private Double averageCycleTime;
    private Double averagePickTime;
    private Double averagePackTime;
    private Double averageShipTime;

    // Delivery metrics
    private Double onTimeDeliveryRate;
    private Integer onTimeDeliveries;
    private Integer lateDeliveries;

    // Returns
    private Double returnRate;
    private Integer returnCount;

    // Productivity
    private Double ordersPerHour;
    private Double ordersPerPerson;

    private Map<String, Object> attributes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
