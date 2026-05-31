package com.gogidix.courier.discountservice.infrastructure.persistence.adapter;

import com.gogidix.courier.discountservice.domain.entity.DiscountUsage;
import com.gogidix.courier.discountservice.domain.repository.DiscountUsageRepository;
import com.gogidix.courier.discountservice.infrastructure.persistence.repository.MongoDiscountUsageRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public class DiscountUsageRepositoryAdapter implements DiscountUsageRepository {

    private final MongoDiscountUsageRepository mongoRepository;

    public DiscountUsageRepositoryAdapter(MongoDiscountUsageRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public DiscountUsage save(DiscountUsage discountUsage) {
        return mongoRepository.save(discountUsage);
    }

    @Override
    public List<DiscountUsage> findByTenantIdAndDiscountCode(String tenantId, String discountCode) {
        return mongoRepository.findByTenantIdAndDiscountCode(tenantId, discountCode);
    }

    @Override
    public List<DiscountUsage> findByTenantIdAndUserId(String tenantId, String userId) {
        return mongoRepository.findByTenantIdAndUserId(tenantId, userId);
    }

    @Override
    public long countByTenantIdAndDiscountCodeAndUserId(String tenantId, String discountCode, String userId) {
        return mongoRepository.countByTenantIdAndDiscountCodeAndUserId(tenantId, discountCode, userId);
    }

    @Override
    public List<DiscountUsage> findByTenantIdAndDiscountCodeAndUsedAtBetween(
            String tenantId, String discountCode, Instant start, Instant end) {
        return mongoRepository.findByTenantIdAndDiscountCodeAndUsedAtBetween(tenantId, discountCode, start, end);
    }
}
