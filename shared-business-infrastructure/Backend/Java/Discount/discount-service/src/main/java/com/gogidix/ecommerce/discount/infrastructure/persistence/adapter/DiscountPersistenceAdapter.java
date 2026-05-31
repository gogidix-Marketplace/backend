package com.gogidix.ecommerce.discount.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.discount.domain.model.Discount;
import com.gogidix.ecommerce.discount.domain.port.out.DiscountRepositoryPort;
import com.gogidix.ecommerce.discount.infrastructure.persistence.repository.DiscountMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DiscountPersistenceAdapter implements DiscountRepositoryPort {

    private final DiscountMongoRepository mongoRepository;

    public DiscountPersistenceAdapter(DiscountMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Discount save(Discount entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<Discount> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<Discount> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}
