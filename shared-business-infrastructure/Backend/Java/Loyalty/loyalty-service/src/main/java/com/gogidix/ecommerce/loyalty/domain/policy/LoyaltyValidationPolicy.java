package com.gogidix.ecommerce.loyalty.domain.policy;

import com.gogidix.ecommerce.loyalty.domain.model.Loyalty;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyValidationPolicy {
    public void validate(Loyalty entity) {
        if (entity == null) throw new IllegalArgumentException("Loyalty cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("Loyalty name is required");
        }
    }
}
