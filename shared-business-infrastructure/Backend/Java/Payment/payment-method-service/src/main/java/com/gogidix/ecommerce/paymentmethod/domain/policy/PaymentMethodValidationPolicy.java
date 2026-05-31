package com.gogidix.ecommerce.paymentmethod.domain.policy;

import com.gogidix.ecommerce.paymentmethod.domain.model.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodValidationPolicy {
    public void validate(PaymentMethod entity) {
        if (entity == null) throw new IllegalArgumentException("PaymentMethod cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("PaymentMethod name is required");
        }
    }
}
