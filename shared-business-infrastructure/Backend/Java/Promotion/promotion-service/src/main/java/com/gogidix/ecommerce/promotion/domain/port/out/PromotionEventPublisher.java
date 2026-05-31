package com.gogidix.ecommerce.promotion.domain.port.out;

import com.gogidix.ecommerce.promotion.domain.event.PromotionDomainEvent;

public interface PromotionEventPublisher {
    void publish(PromotionDomainEvent event);
}
