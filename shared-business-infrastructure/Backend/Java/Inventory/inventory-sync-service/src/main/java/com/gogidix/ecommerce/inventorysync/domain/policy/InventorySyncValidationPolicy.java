package com.gogidix.ecommerce.inventorysync.domain.policy;

import com.gogidix.ecommerce.inventorysync.domain.model.InventorySync;
import org.springframework.stereotype.Component;

@Component
public class InventorySyncValidationPolicy {
    public void validate(InventorySync entity) {
        if (entity == null) throw new IllegalArgumentException("InventorySync cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("InventorySync name is required");
        }
    }
}
