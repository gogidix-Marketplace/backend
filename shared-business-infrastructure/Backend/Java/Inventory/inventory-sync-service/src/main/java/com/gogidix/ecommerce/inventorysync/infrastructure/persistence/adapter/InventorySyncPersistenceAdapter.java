package com.gogidix.ecommerce.inventorysync.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.inventorysync.domain.model.InventorySync;
import com.gogidix.ecommerce.inventorysync.domain.port.out.InventorySyncRepositoryPort;
import com.gogidix.ecommerce.inventorysync.infrastructure.persistence.repository.InventorySyncMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class InventorySyncPersistenceAdapter implements InventorySyncRepositoryPort {

    private final InventorySyncMongoRepository mongoRepository;

    public InventorySyncPersistenceAdapter(InventorySyncMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public InventorySync save(InventorySync entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<InventorySync> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<InventorySync> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}
