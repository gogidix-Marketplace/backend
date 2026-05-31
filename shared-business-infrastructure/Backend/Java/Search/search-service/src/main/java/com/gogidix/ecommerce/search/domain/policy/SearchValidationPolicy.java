package com.gogidix.ecommerce.search.domain.policy;

import com.gogidix.ecommerce.search.domain.model.Search;
import org.springframework.stereotype.Component;

@Component
public class SearchValidationPolicy {
    public void validate(Search entity) {
        if (entity == null) throw new IllegalArgumentException("Search cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("Search name is required");
        }
    }
}
