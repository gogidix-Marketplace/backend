package com.gogidix.shared.warehousing.bin.domain.entity;

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
@Document(collection = "bins")
@CompoundIndex(def = "{'tenantId': 1, 'shelfId': 1}", name = "idx_tenant_shelf")
public class Bin {
    @Id
    private String id;
    @Indexed
    private String tenantId;
    @Indexed
    private String shelfId;
    @Indexed
    private String zoneId;
    @Indexed
    private String warehouseId;
    private String binNumber;
    private String binType;
    private String status;
    private String assignedItemSku;
    private Double capacity;
    private Double usedCapacity;
    private Double availableCapacity;
    private Map<String, Object> attributes;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
