package com.gogidix.ecommerce.warehouse.infrastructure.persistence.repository;

import com.gogidix.ecommerce.warehouse.domain.model.Warehouse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WarehouseMongoRepository extends MongoRepository<Warehouse, String> {
    List<Warehouse> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
