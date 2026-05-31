package com.gogidix.sales.dealmanagement.infrastructure.persistence.mongo;

import com.gogidix.sales.dealmanagement.domain.model.DealProduct;
import com.gogidix.sales.dealmanagement.domain.repository.DealProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Deal Product Repository Implementation
 */
@Repository
@RequiredArgsConstructor
public class MongoDealProductRepository implements DealProductRepository {

    private final ProductMongoRepository mongoRepository;

    @Override
    public DealProduct save(DealProduct product) {
        return mongoRepository.save(product);
    }

    @Override
    public Optional<DealProduct> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<DealProduct> findByProductIdAndTenantId(String productId, String tenantId) {
        return mongoRepository.findByProductIdAndTenantId(productId, tenantId);
    }

    @Override
    public List<DealProduct> findByDealIdAndTenantId(String dealId, String tenantId) {
        return mongoRepository.findByDealIdAndTenantId(dealId, tenantId);
    }

    @Override
    public List<DealProduct> findAllByTenantId(String tenantId) {
        return mongoRepository.findAllByTenantId(tenantId);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public void deleteByProductIdAndTenantId(String productId, String tenantId) {
        mongoRepository.deleteByProductIdAndTenantId(productId, tenantId);
    }

    @Override
    public void deleteByDealIdAndTenantId(String dealId, String tenantId) {
        mongoRepository.deleteByDealIdAndTenantId(dealId, tenantId);
    }

    @Override
    public boolean existsByProductIdAndTenantId(String productId, String tenantId) {
        return mongoRepository.existsByProductIdAndTenantId(productId, tenantId);
    }

    /**
     * Spring Data MongoDB inner interface
     */
    interface ProductMongoRepository extends MongoRepository<DealProduct, String> {

        Optional<DealProduct> findByProductIdAndTenantId(String productId, String tenantId);

        List<DealProduct> findByDealIdAndTenantId(String dealId, String tenantId);

        List<DealProduct> findAllByTenantId(String tenantId);

        void deleteByProductIdAndTenantId(String productId, String tenantId);

        void deleteByDealIdAndTenantId(String dealId, String tenantId);

        boolean existsByProductIdAndTenantId(String productId, String tenantId);
    }
}
