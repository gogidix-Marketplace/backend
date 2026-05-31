package com.gogidix.ecommerce.analytics.domain.policy;

import com.gogidix.ecommerce.analytics.domain.model.Analytics;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsValidationPolicy {
    public void validate(Analytics entity) {
        if (entity == null) throw new IllegalArgumentException("Analytics cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("Analytics name is required");
        }
    }
}
