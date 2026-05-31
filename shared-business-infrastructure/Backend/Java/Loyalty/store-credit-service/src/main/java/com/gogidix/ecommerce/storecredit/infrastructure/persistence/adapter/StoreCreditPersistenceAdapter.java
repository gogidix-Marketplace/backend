package com.gogidix.ecommerce.storecredit.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.storecredit.domain.model.StoreCredit;
import com.gogidix.ecommerce.storecredit.domain.port.out.StoreCreditRepositoryPort;
import com.gogidix.ecommerce.storecredit.infrastructure.persistence.repository.StoreCreditMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class StoreCreditPersistenceAdapter implements StoreCreditRepositoryPort {

    private final StoreCreditMongoRepository mongoRepository;

    public StoreCreditPersistenceAdapter(StoreCreditMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public StoreCredit save(StoreCredit entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<StoreCredit> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<StoreCredit> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}
