package com.gogidix.ecommerce.giftcard.domain.policy;

import com.gogidix.ecommerce.giftcard.domain.model.GiftCard;
import org.springframework.stereotype.Component;

@Component
public class GiftCardValidationPolicy {
    public void validate(GiftCard entity) {
        if (entity == null) throw new IllegalArgumentException("GiftCard cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("GiftCard name is required");
        }
    }
}
