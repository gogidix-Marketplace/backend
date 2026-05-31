package com.gogidix.sales.dealmanagement.domain.repository;

import com.gogidix.sales.dealmanagement.domain.model.DealProduct;

import java.util.List;
import java.util.Optional;

/**
 * Deal Product Repository Interface
 * Defines the contract for deal product persistence operations
 */
public interface DealProductRepository {

    DealProduct save(DealProduct product);

    Optional<DealProduct> findById(String id);

    Optional<DealProduct> findByProductIdAndTenantId(String productId, String tenantId);

    List<DealProduct> findByDealIdAndTenantId(String dealId, String tenantId);

    List<DealProduct> findAllByTenantId(String tenantId);

    void deleteById(String id);

    void deleteByProductIdAndTenantId(String productId, String tenantId);

    void deleteByDealIdAndTenantId(String dealId, String tenantId);

    boolean existsByProductIdAndTenantId(String productId, String tenantId);
}
