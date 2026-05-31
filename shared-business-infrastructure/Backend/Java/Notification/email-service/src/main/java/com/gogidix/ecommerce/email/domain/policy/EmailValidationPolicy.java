package com.gogidix.ecommerce.email.domain.policy;

import com.gogidix.ecommerce.email.domain.model.Email;
import org.springframework.stereotype.Component;

@Component
public class EmailValidationPolicy {
    public void validate(Email entity) {
        if (entity == null) throw new IllegalArgumentException("Email cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("Email name is required");
        }
    }
}
