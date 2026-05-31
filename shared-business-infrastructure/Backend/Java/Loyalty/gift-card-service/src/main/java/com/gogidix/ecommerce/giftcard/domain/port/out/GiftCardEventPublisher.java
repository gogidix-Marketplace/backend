package com.gogidix.ecommerce.giftcard.domain.port.out;

import com.gogidix.ecommerce.giftcard.domain.event.GiftCardDomainEvent;

public interface GiftCardEventPublisher {
    void publish(GiftCardDomainEvent event);
}
