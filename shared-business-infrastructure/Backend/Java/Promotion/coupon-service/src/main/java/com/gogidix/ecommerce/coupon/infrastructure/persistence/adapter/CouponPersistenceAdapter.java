package com.gogidix.ecommerce.coupon.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.coupon.domain.model.Coupon;
import com.gogidix.ecommerce.coupon.domain.port.out.CouponRepositoryPort;
import com.gogidix.ecommerce.coupon.infrastructure.persistence.repository.CouponMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CouponPersistenceAdapter implements CouponRepositoryPort {

    private final CouponMongoRepository mongoRepository;

    public CouponPersistenceAdapter(CouponMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Coupon save(Coupon entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<Coupon> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<Coupon> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}
