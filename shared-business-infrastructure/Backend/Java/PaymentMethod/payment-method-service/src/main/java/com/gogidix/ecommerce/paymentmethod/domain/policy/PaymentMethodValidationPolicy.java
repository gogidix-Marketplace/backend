package com.gogidix.ecommerce.paymentmethod.domain.policy;

import com.gogidix.ecommerce.paymentmethod.domain.model.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodValidationPolicy {

    public void validateForCreation(PaymentMethod entity) {
        validateName(entity.getName());
        validateTenantId(entity.getTenantId());
    }

    public void validateForUpdate(PaymentMethod entity) {
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