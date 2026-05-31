package com.gogidix.ecommerce.coupon.infrastructure.persistence.repository;

import com.gogidix.ecommerce.coupon.domain.model.Coupon;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CouponMongoRepository extends MongoRepository<Coupon, String> {
    List<Coupon> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
