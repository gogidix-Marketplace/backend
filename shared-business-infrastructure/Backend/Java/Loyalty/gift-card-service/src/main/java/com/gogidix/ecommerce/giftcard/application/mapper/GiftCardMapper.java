package com.gogidix.ecommerce.giftcard.application.mapper;

import com.gogidix.ecommerce.giftcard.application.dto.*;
import com.gogidix.ecommerce.giftcard.domain.model.GiftCard;
import org.springframework.stereotype.Component;

@Component
public class GiftCardMapper {

    public GiftCardResponse toResponse(GiftCard entity) {
        if (entity == null) return null;
        return new GiftCardResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getCardNumber(),
            entity.getBalance(),
            entity.getInitialAmount(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public GiftCard toEntity(CreateGiftCardRequest request) {
        GiftCard entity = new GiftCard();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setCardNumber(request.cardNumber());
        entity.setBalance(request.balance());
        entity.setInitialAmount(request.initialAmount());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(GiftCard entity, UpdateGiftCardRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.cardNumber() != null) entity.setCardNumber(request.cardNumber());
        if (request.balance() != null) entity.setBalance(request.balance());
        if (request.initialAmount() != null) entity.setInitialAmount(request.initialAmount());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
