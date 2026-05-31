package com.gogidix.ecommerce.wishlist.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.wishlist.domain.model.Wishlist;
import com.gogidix.ecommerce.wishlist.domain.port.out.WishlistRepositoryPort;
import com.gogidix.ecommerce.wishlist.infrastructure.persistence.repository.WishlistMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class WishlistPersistenceAdapter implements WishlistRepositoryPort {

    private final WishlistMongoRepository mongoRepository;

    public WishlistPersistenceAdapter(WishlistMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Wishlist save(Wishlist entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<Wishlist> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<Wishlist> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}
