package com.gogidix.shared.warehousing.zone.domain.entity;

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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "zones")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1}", name = "idx_tenant_warehouse")
public class Zone {
    @Id
    private String id;
    @Indexed
    private String tenantId;
    @Indexed
    private String zoneId;
    @Indexed
    private String warehouseId;
    private String zoneName;
    private String name;
    private String state;
    private String country;
    @Indexed
    private String zoneType;
    private String status;
    private Double capacity;
    private Double usedCapacity;
    private Double availableCapacity;
    private List<String> warehouseIds;
    private List<String> courierPartnerIds;
    private String hubId;
    private Coordinates coordinates;
    private Map<String, Object> attributes;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Coordinates {
        private Double centerLatitude;
        private Double centerLongitude;
        private Double radiusKm;
    }
}
