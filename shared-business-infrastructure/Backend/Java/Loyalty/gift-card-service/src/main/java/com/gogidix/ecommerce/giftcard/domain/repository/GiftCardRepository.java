package com.gogidix.ecommerce.giftcard.domain.repository;

import com.gogidix.ecommerce.giftcard.domain.model.GiftCard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftCardRepository extends MongoRepository<GiftCard, String> {
    List<GiftCard> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
