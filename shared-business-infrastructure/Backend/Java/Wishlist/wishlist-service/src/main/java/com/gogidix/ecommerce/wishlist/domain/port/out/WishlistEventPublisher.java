package com.gogidix.ecommerce.wishlist.domain.port.out;

import com.gogidix.ecommerce.wishlist.domain.event.WishlistDomainEvent;

public interface WishlistEventPublisher {
    void publish(WishlistDomainEvent event);
}
