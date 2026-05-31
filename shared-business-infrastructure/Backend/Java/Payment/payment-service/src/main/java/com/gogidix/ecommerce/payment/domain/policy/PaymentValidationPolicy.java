package com.gogidix.ecommerce.payment.domain.policy;

import com.gogidix.ecommerce.payment.domain.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentValidationPolicy {
    public void validate(Payment entity) {
        if (entity == null) throw new IllegalArgumentException("Payment cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("Payment name is required");
        }
    }
}
