package com.gogidix.ecommerce.inventory.application.mapper;

import com.gogidix.ecommerce.inventory.application.dto.*;
import com.gogidix.ecommerce.inventory.domain.model.Inventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public InventoryResponse toResponse(Inventory entity) {
        if (entity == null) return null;
        return new InventoryResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getProductId(),
            entity.getSku(),
            entity.getQuantity(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public Inventory toEntity(CreateInventoryRequest request) {
        Inventory entity = new Inventory();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setProductId(request.productId());
        entity.setSku(request.sku());
        entity.setQuantity(request.quantity());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(Inventory entity, UpdateInventoryRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.productId() != null) entity.setProductId(request.productId());
        if (request.sku() != null) entity.setSku(request.sku());
        if (request.quantity() != null) entity.setQuantity(request.quantity());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
