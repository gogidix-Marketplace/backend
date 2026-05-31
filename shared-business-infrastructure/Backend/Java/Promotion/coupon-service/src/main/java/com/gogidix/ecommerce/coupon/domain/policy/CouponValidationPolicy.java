package com.gogidix.ecommerce.coupon.domain.policy;

import com.gogidix.ecommerce.coupon.domain.model.Coupon;
import org.springframework.stereotype.Component;

@Component
public class CouponValidationPolicy {
    public void validate(Coupon entity) {
        if (entity == null) throw new IllegalArgumentException("Coupon cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("Coupon name is required");
        }
    }
}
