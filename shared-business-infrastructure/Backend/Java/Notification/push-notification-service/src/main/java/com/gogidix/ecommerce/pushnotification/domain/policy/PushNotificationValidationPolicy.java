package com.gogidix.ecommerce.pushnotification.domain.policy;

import com.gogidix.ecommerce.pushnotification.domain.model.PushNotification;
import org.springframework.stereotype.Component;

@Component
public class PushNotificationValidationPolicy {
    public void validate(PushNotification entity) {
        if (entity == null) throw new IllegalArgumentException("PushNotification cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("PushNotification name is required");
        }
    }
}
