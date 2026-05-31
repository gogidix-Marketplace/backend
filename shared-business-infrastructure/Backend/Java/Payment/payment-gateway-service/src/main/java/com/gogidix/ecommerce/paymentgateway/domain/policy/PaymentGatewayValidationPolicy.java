package com.gogidix.ecommerce.paymentgateway.domain.policy;

import com.gogidix.ecommerce.paymentgateway.domain.model.PaymentGateway;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayValidationPolicy {
    public void validate(PaymentGateway entity) {
        if (entity == null) throw new IllegalArgumentException("PaymentGateway cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("PaymentGateway name is required");
        }
    }
}
