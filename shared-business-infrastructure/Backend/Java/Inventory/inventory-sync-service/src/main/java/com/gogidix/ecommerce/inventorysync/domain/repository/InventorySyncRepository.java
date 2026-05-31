package com.gogidix.ecommerce.inventorysync.domain.repository;

import com.gogidix.ecommerce.inventorysync.domain.model.InventorySync;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventorySyncRepository extends MongoRepository<InventorySync, String> {
    List<InventorySync> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
