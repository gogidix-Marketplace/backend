package com.gogidix.ecommerce.promotion.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.promotion.domain.model.Promotion;
import com.gogidix.ecommerce.promotion.domain.port.out.PromotionRepositoryPort;
import com.gogidix.ecommerce.promotion.infrastructure.persistence.repository.PromotionMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PromotionPersistenceAdapter implements PromotionRepositoryPort {

    private final PromotionMongoRepository mongoRepository;

    public PromotionPersistenceAdapter(PromotionMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Promotion save(Promotion entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<Promotion> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<Promotion> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}
