package com.gogidix.ecommerce.wishlist.domain.policy;

import com.gogidix.ecommerce.wishlist.domain.model.Wishlist;
import org.springframework.stereotype.Component;

@Component
public class WishlistValidationPolicy {
    public void validate(Wishlist entity) {
        if (entity == null) throw new IllegalArgumentException("Wishlist cannot be null");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("Wishlist name is required");
        }
    }
}
