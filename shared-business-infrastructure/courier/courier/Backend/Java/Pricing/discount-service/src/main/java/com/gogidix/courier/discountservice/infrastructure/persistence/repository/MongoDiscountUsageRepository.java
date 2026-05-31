package com.gogidix.courier.discountservice.infrastructure.persistence.repository;

import com.gogidix.courier.discountservice.domain.entity.DiscountUsage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface MongoDiscountUsageRepository extends MongoRepository<DiscountUsage, String> {

    List<DiscountUsage> findByTenantIdAndDiscountCode(String tenantId, String discountCode);

    List<DiscountUsage> findByTenantIdAndUserId(String tenantId, String userId);

    long countByTenantIdAndDiscountCodeAndUserId(String tenantId, String discountCode, String userId);

    List<DiscountUsage> findByTenantIdAndDiscountCodeAndUsedAtBetween(
            String tenantId, String discountCode, Instant start, Instant end);
}
