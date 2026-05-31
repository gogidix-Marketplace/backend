package com.gogidix.ecommerce.reward.domain.policy;

import com.gogidix.ecommerce.reward.domain.model.Reward;
import org.springframework.stereotype.Component;

@Component
public class RewardValidationPolicy {
    public void validate(Reward entity) {
        if (entity == null) throw new IllegalArgumentException("Reward cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("Reward name is required");
        }
    }
}
