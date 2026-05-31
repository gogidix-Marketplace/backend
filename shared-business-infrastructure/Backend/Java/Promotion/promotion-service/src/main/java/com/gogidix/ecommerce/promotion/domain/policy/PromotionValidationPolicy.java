package com.gogidix.ecommerce.promotion.domain.policy;

import com.gogidix.ecommerce.promotion.domain.model.Promotion;
import org.springframework.stereotype.Component;

@Component
public class PromotionValidationPolicy {
    public void validate(Promotion entity) {
        if (entity == null) throw new IllegalArgumentException("Promotion cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("Promotion name is required");
        }
    }
}
