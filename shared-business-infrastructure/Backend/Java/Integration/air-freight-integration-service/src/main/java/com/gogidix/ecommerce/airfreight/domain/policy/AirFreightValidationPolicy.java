package com.gogidix.ecommerce.airfreight.domain.policy;

import com.gogidix.ecommerce.airfreight.domain.model.AirFreight;
import org.springframework.stereotype.Component;

@Component
public class AirFreightValidationPolicy {
    public void validate(AirFreight entity) {
        if (entity == null) throw new IllegalArgumentException("AirFreight cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("AirFreight name is required");
        }
    }
}
