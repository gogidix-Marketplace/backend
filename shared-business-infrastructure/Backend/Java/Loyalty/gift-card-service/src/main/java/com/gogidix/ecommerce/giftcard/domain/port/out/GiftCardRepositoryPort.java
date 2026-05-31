package com.gogidix.ecommerce.giftcard.domain.port.out;

import com.gogidix.ecommerce.giftcard.domain.model.GiftCard;
import java.util.List;
import java.util.Optional;

public interface GiftCardRepositoryPort {
    GiftCard save(GiftCard entity);
    Optional<GiftCard> findById(String id);
    List<GiftCard> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
