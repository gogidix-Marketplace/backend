package com.gogidix.ecommerce.inventory.domain.port.out;

import com.gogidix.ecommerce.inventory.domain.model.Inventory;
import java.util.List;
import java.util.Optional;

public interface InventoryRepositoryPort {
    Inventory save(Inventory entity);
    Optional<Inventory> findById(String id);
    List<Inventory> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
