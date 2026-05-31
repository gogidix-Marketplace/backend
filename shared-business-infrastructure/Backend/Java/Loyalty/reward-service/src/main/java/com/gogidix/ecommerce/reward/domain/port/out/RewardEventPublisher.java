package com.gogidix.ecommerce.reward.domain.port.out;

import com.gogidix.ecommerce.reward.domain.event.RewardDomainEvent;

public interface RewardEventPublisher {
    void publish(RewardDomainEvent event);
}
