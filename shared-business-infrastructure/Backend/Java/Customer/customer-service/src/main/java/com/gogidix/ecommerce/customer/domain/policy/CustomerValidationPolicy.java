package com.gogidix.ecommerce.customer.domain.policy;

import com.gogidix.ecommerce.customer.domain.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerValidationPolicy {
    public void validate(Customer entity) {
        if (entity == null) throw new IllegalArgumentException("Customer cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("Customer name is required");
        }
    }
}
