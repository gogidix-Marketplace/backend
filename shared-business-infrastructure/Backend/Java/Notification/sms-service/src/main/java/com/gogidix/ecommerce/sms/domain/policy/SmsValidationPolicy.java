package com.gogidix.ecommerce.sms.domain.policy;

import com.gogidix.ecommerce.sms.domain.model.Sms;
import org.springframework.stereotype.Component;

@Component
public class SmsValidationPolicy {
    public void validate(Sms entity) {
        if (entity == null) throw new IllegalArgumentException("Sms cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("Sms name is required");
        }
    }
}
