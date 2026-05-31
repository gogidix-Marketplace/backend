package com.gogidix.ecommerce.warehouse.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.warehouse.domain.model.Warehouse;
import com.gogidix.ecommerce.warehouse.domain.port.out.WarehouseRepositoryPort;
import com.gogidix.ecommerce.warehouse.infrastructure.persistence.repository.WarehouseMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class WarehousePersistenceAdapter implements WarehouseRepositoryPort {

    private final WarehouseMongoRepository mongoRepository;

    public WarehousePersistenceAdapter(WarehouseMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Warehouse save(Warehouse entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<Warehouse> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<Warehouse> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}
