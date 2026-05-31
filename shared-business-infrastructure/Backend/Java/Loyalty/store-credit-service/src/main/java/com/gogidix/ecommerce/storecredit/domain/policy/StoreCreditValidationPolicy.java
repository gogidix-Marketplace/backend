package com.gogidix.ecommerce.storecredit.domain.policy;

import com.gogidix.ecommerce.storecredit.domain.model.StoreCredit;
import org.springframework.stereotype.Component;

@Component
public class StoreCreditValidationPolicy {
    public void validate(StoreCredit entity) {
        if (entity == null) throw new IllegalArgumentException("StoreCredit cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("StoreCredit name is required");
        }
    }
}
