package com.gogidix.ecommerce.coupon.domain.port.out;

import com.gogidix.ecommerce.coupon.domain.model.Coupon;
import java.util.List;
import java.util.Optional;

public interface CouponRepositoryPort {
    Coupon save(Coupon entity);
    Optional<Coupon> findById(String id);
    List<Coupon> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
