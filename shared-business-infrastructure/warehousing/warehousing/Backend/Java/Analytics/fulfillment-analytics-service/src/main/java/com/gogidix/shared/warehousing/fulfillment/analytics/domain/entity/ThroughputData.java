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
@Document(collection = "throughput_data")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'timestamp': 1}", name = "idx_tenant_warehouse_timestamp")
public class ThroughputData {
    @Id
    private String id;
    @Indexed
    private String tenantId;
    @Indexed
    private String warehouseId;
    private String warehouseName;
    @Indexed
    private LocalDateTime timestamp;

    // Throughput metrics
    private Double ordersPerHour;
    private Double itemsPerHour;
    private Double ordersPerMinute;
    private Double itemsPerMinute;

    // Labor productivity
    private Double laborProductivity;
    private Integer ordersPerPerson;
    private Double itemsPerPerson;

    // Equipment utilization
    private Double conveyorUtilization;
    private Double forkliftUtilization;
    private Double scannerUtilization;

    // Capacity metrics
    private Double capacityUtilization;
    private Integer activeOrders;
    private Integer queueLength;

    private Map<String, Object> attributes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
