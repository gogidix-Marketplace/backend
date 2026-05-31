package com.gogidix.ecommerce.email.domain.policy;

import com.gogidix.ecommerce.email.domain.model.Email;
import org.springframework.stereotype.Component;

@Component
public class EmailValidationPolicy {

    public void validateForCreation(Email entity) {
        validateName(entity.getName());
        validateTenantId(entity.getTenantId());
    }

    public void validateForUpdate(Email entity) {
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