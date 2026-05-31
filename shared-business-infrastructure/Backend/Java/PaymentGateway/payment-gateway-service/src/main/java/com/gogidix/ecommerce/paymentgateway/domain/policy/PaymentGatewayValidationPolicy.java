package com.gogidix.ecommerce.paymentgateway.domain.policy;

import com.gogidix.ecommerce.paymentgateway.domain.model.PaymentGateway;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayValidationPolicy {

    public void validateForCreation(PaymentGateway entity) {
        validateName(entity.getName());
        validateTenantId(entity.getTenantId());
    }

    public void validateForUpdate(PaymentGateway entity) {
        validateName(entity.getName());
        validateTenantId(entity.getTenantId());
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
    }

    private void validateTenantId(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("Tenant ID must not be blank");
        }
    }
}