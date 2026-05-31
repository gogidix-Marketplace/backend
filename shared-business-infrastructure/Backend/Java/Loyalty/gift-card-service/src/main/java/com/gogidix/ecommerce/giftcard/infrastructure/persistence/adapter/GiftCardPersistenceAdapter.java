package com.gogidix.ecommerce.giftcard.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.giftcard.domain.model.GiftCard;
import com.gogidix.ecommerce.giftcard.domain.port.out.GiftCardRepositoryPort;
import com.gogidix.ecommerce.giftcard.infrastructure.persistence.repository.GiftCardMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GiftCardPersistenceAdapter implements GiftCardRepositoryPort {

    private final GiftCardMongoRepository mongoRepository;

    public GiftCardPersistenceAdapter(GiftCardMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public GiftCard save(GiftCard entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<GiftCard> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<GiftCard> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}
