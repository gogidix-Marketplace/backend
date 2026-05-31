package com.gogidix.ecommerce.inventory.domain.policy;

import com.gogidix.ecommerce.inventory.domain.model.Inventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryValidationPolicy {
    public void validate(Inventory entity) {
        if (entity == null) throw new IllegalArgumentException("Inventory cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("Inventory name is required");
        }
    }
}
