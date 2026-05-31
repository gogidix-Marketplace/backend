package com.gogidix.ecommerce.inventory.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.inventory.domain.model.Inventory;
import com.gogidix.ecommerce.inventory.domain.port.out.InventoryRepositoryPort;
import com.gogidix.ecommerce.inventory.infrastructure.persistence.repository.InventoryMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class InventoryPersistenceAdapter implements InventoryRepositoryPort {

    private final InventoryMongoRepository mongoRepository;

    public InventoryPersistenceAdapter(InventoryMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Inventory save(Inventory entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<Inventory> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<Inventory> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}
