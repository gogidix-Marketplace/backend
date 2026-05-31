package com.gogidix.ecommerce.oceanshipping.domain.policy;

import com.gogidix.ecommerce.oceanshipping.domain.model.OceanShipping;
import org.springframework.stereotype.Component;

@Component
public class OceanShippingValidationPolicy {
    public void validate(OceanShipping entity) {
        if (entity == null) throw new IllegalArgumentException("OceanShipping cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("OceanShipping name is required");
        }
    }
}
