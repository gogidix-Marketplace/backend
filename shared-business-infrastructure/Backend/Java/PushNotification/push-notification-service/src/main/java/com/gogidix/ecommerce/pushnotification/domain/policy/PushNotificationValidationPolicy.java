package com.gogidix.ecommerce.pushnotification.domain.policy;

import com.gogidix.ecommerce.pushnotification.domain.model.PushNotification;
import org.springframework.stereotype.Component;

@Component
public class PushNotificationValidationPolicy {

    public void validateForCreation(PushNotification entity) {
        validateName(entity.getName());
        validateTenantId(entity.getTenantId());
    }

    public void validateForUpdate(PushNotification entity) {
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