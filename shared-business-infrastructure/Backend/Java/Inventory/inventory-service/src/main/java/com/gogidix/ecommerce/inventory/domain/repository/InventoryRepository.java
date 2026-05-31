package com.gogidix.ecommerce.inventory.domain.repository;

import com.gogidix.ecommerce.inventory.domain.model.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends MongoRepository<Inventory, String> {
    List<Inventory> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
