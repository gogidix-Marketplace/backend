package com.gogidix.courier.discountservice.infrastructure.persistence.adapter;

import com.gogidix.courier.discountservice.domain.entity.DiscountCode;
import com.gogidix.courier.discountservice.domain.repository.DiscountCodeRepository;
import com.gogidix.courier.discountservice.infrastructure.persistence.repository.MongoDiscountCodeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DiscountCodeRepositoryAdapter implements DiscountCodeRepository {

    private final MongoDiscountCodeRepository mongoRepository;

    public DiscountCodeRepositoryAdapter(MongoDiscountCodeRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public DiscountCode save(DiscountCode discountCode) {
        return mongoRepository.save(discountCode);
    }

    @Override
    public Optional<DiscountCode> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<DiscountCode> findByTenantIdAndCode(String tenantId, String code) {
        return mongoRepository.findByTenantIdAndCode(tenantId, code);
    }

    @Override
    public List<DiscountCode> findByTenantId(String tenantId) {
        return mongoRepository.findByTenantId(tenantId);
    }

    @Override
    public List<DiscountCode> findByTenantIdAndStatus(String tenantId, DiscountCode.DiscountStatus status) {
        return mongoRepository.findByTenantIdAndStatus(tenantId, status);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public boolean existsByTenantIdAndCode(String tenantId, String code) {
        return mongoRepository.existsByTenantIdAndCode(tenantId, code);
    }
}
