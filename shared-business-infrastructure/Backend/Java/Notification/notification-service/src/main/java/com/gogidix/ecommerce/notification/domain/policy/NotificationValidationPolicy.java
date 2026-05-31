package com.gogidix.ecommerce.notification.domain.policy;

import com.gogidix.ecommerce.notification.domain.model.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationValidationPolicy {
    public void validate(Notification entity) {
        if (entity == null) throw new IllegalArgumentException("Notification cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("Notification name is required");
        }
    }
}
