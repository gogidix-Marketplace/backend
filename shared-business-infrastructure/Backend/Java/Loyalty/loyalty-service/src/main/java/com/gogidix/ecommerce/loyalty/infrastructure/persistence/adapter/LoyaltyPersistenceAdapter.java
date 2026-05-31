package com.gogidix.ecommerce.loyalty.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.loyalty.domain.model.Loyalty;
import com.gogidix.ecommerce.loyalty.domain.port.out.LoyaltyRepositoryPort;
import com.gogidix.ecommerce.loyalty.infrastructure.persistence.repository.LoyaltyMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LoyaltyPersistenceAdapter implements LoyaltyRepositoryPort {

    private final LoyaltyMongoRepository mongoRepository;

    public LoyaltyPersistenceAdapter(LoyaltyMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Loyalty save(Loyalty entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<Loyalty> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<Loyalty> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}
