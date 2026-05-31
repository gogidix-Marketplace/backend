package com.gogidix.courier.discountservice.domain.repository;

import com.gogidix.courier.discountservice.domain.entity.DiscountCode;

import java.util.List;
import java.util.Optional;

public interface DiscountCodeRepository {

    DiscountCode save(DiscountCode discountCode);

    Optional<DiscountCode> findById(String id);

    Optional<DiscountCode> findByTenantIdAndCode(String tenantId, String code);

    List<DiscountCode> findByTenantId(String tenantId);

    List<DiscountCode> findByTenantIdAndStatus(String tenantId, DiscountCode.DiscountStatus status);

    void deleteById(String id);

    boolean existsByTenantIdAndCode(String tenantId, String code);
}
