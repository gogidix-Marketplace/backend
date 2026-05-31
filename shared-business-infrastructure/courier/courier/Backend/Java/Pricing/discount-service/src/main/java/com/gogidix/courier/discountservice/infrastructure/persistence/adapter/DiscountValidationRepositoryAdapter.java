package com.gogidix.courier.discountservice.infrastructure.persistence.adapter;

import com.gogidix.courier.discountservice.domain.entity.DiscountValidation;
import com.gogidix.courier.discountservice.domain.repository.DiscountValidationRepository;
import com.gogidix.courier.discountservice.infrastructure.persistence.repository.MongoDiscountValidationRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DiscountValidationRepositoryAdapter implements DiscountValidationRepository {

    private final MongoDiscountValidationRepository mongoRepository;

    public DiscountValidationRepositoryAdapter(MongoDiscountValidationRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public DiscountValidation save(DiscountValidation validation) {
        return mongoRepository.save(validation);
    }

    @Override
    public Optional<DiscountValidation> findByTenantIdAndDiscountCodeAndUserId(
            String tenantId, String discountCode, String userId) {
        return mongoRepository.findByTenantIdAndDiscountCodeAndUserId(tenantId, discountCode, userId);
    }

    @Override
    public void deleteExpiredValidations() {
        mongoRepository.deleteByExpiresAtBefore(java.time.Instant.now());
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }
}
