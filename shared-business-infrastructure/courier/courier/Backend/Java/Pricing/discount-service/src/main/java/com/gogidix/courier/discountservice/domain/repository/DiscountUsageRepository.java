package com.gogidix.courier.discountservice.domain.repository;

import com.gogidix.courier.discountservice.domain.entity.DiscountUsage;

import java.time.Instant;
import java.util.List;

public interface DiscountUsageRepository {

    DiscountUsage save(DiscountUsage discountUsage);

    List<DiscountUsage> findByTenantIdAndDiscountCode(String tenantId, String discountCode);

    List<DiscountUsage> findByTenantIdAndUserId(String tenantId, String userId);

    long countByTenantIdAndDiscountCodeAndUserId(String tenantId, String discountCode, String userId);

    List<DiscountUsage> findByTenantIdAndDiscountCodeAndUsedAtBetween(
            String tenantId, String discountCode, Instant start, Instant end);
}
