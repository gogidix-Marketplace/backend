package com.gogidix.ecommerce.warehouse.domain.policy;

import com.gogidix.ecommerce.warehouse.domain.model.Warehouse;
import org.springframework.stereotype.Component;

@Component
public class WarehouseValidationPolicy {
    public void validate(Warehouse entity) {
        if (entity == null) throw new IllegalArgumentException("Warehouse cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("Warehouse name is required");
        }
    }
}
