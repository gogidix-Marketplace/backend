package com.gogidix.ecommerce.inventory.infrastructure.persistence.repository;

import com.gogidix.ecommerce.inventory.domain.model.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InventoryMongoRepository extends MongoRepository<Inventory, String> {
    List<Inventory> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
