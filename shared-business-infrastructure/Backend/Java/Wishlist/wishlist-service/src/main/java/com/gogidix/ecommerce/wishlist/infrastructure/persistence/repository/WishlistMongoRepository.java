package com.gogidix.ecommerce.wishlist.infrastructure.persistence.repository;

import com.gogidix.ecommerce.wishlist.domain.model.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WishlistMongoRepository extends MongoRepository<Wishlist, String> {
    List<Wishlist> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
