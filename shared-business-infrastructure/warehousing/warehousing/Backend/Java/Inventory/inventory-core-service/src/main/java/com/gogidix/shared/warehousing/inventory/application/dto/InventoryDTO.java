package com.gogidix.shared.warehousing.inventory.application.dto;

import com.gogidix.shared.warehousing.inventory.domain.entity.Inventory.LocationType;
import com.gogidix.shared.warehousing.inventory.domain.entity.Inventory.TenantType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Inventory Data Transfer Object
 *
 * Used for API responses and internal data transfer
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {

    private String id;
    private String tenantId;
    private TenantType tenantType;
    private String sku;
    private Integer quantity;
    private String locationId;
    private LocationType locationType;
    private Map<String, Object> attributes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
