package com.gogidix.ecommerce.giftcard.infrastructure.persistence.repository;

import com.gogidix.ecommerce.giftcard.domain.model.GiftCard;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GiftCardMongoRepository extends MongoRepository<GiftCard, String> {
    List<GiftCard> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
