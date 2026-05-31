package com.gogidix.ecommerce.wishlist.domain.repository;

import com.gogidix.ecommerce.wishlist.domain.model.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends MongoRepository<Wishlist, String> {
    List<Wishlist> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
