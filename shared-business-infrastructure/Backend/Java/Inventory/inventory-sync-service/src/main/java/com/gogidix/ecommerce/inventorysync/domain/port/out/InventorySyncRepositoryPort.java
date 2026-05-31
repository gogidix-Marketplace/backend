package com.gogidix.ecommerce.inventorysync.domain.port.out;

import com.gogidix.ecommerce.inventorysync.domain.model.InventorySync;
import java.util.List;
import java.util.Optional;

public interface InventorySyncRepositoryPort {
    InventorySync save(InventorySync entity);
    Optional<InventorySync> findById(String id);
    List<InventorySync> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
