package com.gogidix.ecommerce.sms.domain.policy;

import com.gogidix.ecommerce.sms.domain.model.Sms;
import org.springframework.stereotype.Component;

@Component
public class SmsValidationPolicy {

    public void validateForCreation(Sms entity) {
        validateName(entity.getName());
        validateTenantId(entity.getTenantId());
    }

    public void validateForUpdate(Sms entity) {
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