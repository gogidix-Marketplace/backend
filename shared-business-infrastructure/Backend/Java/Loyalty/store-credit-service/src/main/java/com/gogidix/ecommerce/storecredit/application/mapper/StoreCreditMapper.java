package com.gogidix.ecommerce.storecredit.application.mapper;

import com.gogidix.ecommerce.storecredit.application.dto.*;
import com.gogidix.ecommerce.storecredit.domain.model.StoreCredit;
import org.springframework.stereotype.Component;

@Component
public class StoreCreditMapper {

    public StoreCreditResponse toResponse(StoreCredit entity) {
        if (entity == null) return null;
        return new StoreCreditResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getCreditAmount(),
            entity.getRemainingBalance(),
            entity.getReason(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public StoreCredit toEntity(CreateStoreCreditRequest request) {
        StoreCredit entity = new StoreCredit();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setCreditAmount(request.creditAmount());
        entity.setRemainingBalance(request.remainingBalance());
        entity.setReason(request.reason());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(StoreCredit entity, UpdateStoreCreditRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.creditAmount() != null) entity.setCreditAmount(request.creditAmount());
        if (request.remainingBalance() != null) entity.setRemainingBalance(request.remainingBalance());
        if (request.reason() != null) entity.setReason(request.reason());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
