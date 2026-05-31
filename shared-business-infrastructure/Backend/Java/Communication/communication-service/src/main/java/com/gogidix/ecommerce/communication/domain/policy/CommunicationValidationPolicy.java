package com.gogidix.ecommerce.communication.domain.policy;

import com.gogidix.ecommerce.communication.domain.model.Communication;
import org.springframework.stereotype.Component;

@Component
public class CommunicationValidationPolicy {
    public void validate(Communication entity) {
        if (entity == null) throw new IllegalArgumentException("Communication cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("Communication name is required");
        }
    }
}
