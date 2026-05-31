package com.gogidix.ecommerce.warehouse.domain.repository;

import com.gogidix.ecommerce.warehouse.domain.model.Warehouse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository extends MongoRepository<Warehouse, String> {
    List<Warehouse> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
