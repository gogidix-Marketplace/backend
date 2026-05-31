package com.gogidix.ecommerce.warehouse.domain.port.out;

import com.gogidix.ecommerce.warehouse.domain.model.Warehouse;
import java.util.List;
import java.util.Optional;

public interface WarehouseRepositoryPort {
    Warehouse save(Warehouse entity);
    Optional<Warehouse> findById(String id);
    List<Warehouse> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
