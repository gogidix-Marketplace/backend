package com.gogidix.ecommerce.inventorysync.application.mapper;

import com.gogidix.ecommerce.inventorysync.application.dto.*;
import com.gogidix.ecommerce.inventorysync.domain.model.InventorySync;
import org.springframework.stereotype.Component;

@Component
public class InventorySyncMapper {

    public InventorySyncResponse toResponse(InventorySync entity) {
        if (entity == null) return null;
        return new InventorySyncResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getSourceSystem(),
            entity.getTargetSystem(),
            entity.getSyncStatus(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public InventorySync toEntity(CreateInventorySyncRequest request) {
        InventorySync entity = new InventorySync();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setSourceSystem(request.sourceSystem());
        entity.setTargetSystem(request.targetSystem());
        entity.setSyncStatus(request.syncStatus());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(InventorySync entity, UpdateInventorySyncRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.sourceSystem() != null) entity.setSourceSystem(request.sourceSystem());
        if (request.targetSystem() != null) entity.setTargetSystem(request.targetSystem());
        if (request.syncStatus() != null) entity.setSyncStatus(request.syncStatus());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
