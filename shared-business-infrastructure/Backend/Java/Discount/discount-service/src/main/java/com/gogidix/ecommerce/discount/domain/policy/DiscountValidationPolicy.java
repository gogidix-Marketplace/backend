package com.gogidix.ecommerce.discount.domain.policy;

import com.gogidix.ecommerce.discount.domain.model.Discount;
import org.springframework.stereotype.Component;

@Component
public class DiscountValidationPolicy {
    public void validate(Discount entity) {
        if (entity == null) throw new IllegalArgumentException("Discount cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("Discount name is required");
        }
    }
}
