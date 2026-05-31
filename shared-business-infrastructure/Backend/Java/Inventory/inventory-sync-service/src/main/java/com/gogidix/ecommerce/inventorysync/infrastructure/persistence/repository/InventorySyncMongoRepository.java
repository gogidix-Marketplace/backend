package com.gogidix.ecommerce.inventorysync.infrastructure.persistence.repository;

import com.gogidix.ecommerce.inventorysync.domain.model.InventorySync;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InventorySyncMongoRepository extends MongoRepository<InventorySync, String> {
    List<InventorySync> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
