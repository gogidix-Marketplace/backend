package com.gogidix.ecommerce.wishlist.domain.port.out;

import com.gogidix.ecommerce.wishlist.domain.model.Wishlist;
import java.util.List;
import java.util.Optional;

public interface WishlistRepositoryPort {
    Wishlist save(Wishlist entity);
    Optional<Wishlist> findById(String id);
    List<Wishlist> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
