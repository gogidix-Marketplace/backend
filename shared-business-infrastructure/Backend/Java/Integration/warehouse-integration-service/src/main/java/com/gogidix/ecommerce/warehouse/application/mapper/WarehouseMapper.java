package com.gogidix.ecommerce.warehouse.application.mapper;

import com.gogidix.ecommerce.warehouse.application.dto.*;
import com.gogidix.ecommerce.warehouse.domain.model.Warehouse;
import org.springframework.stereotype.Component;

@Component
public class WarehouseMapper {

    public WarehouseResponse toResponse(Warehouse entity) {
        if (entity == null) return null;
        return new WarehouseResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getWarehouseCode(),
            entity.getLocation(),
            entity.getCapacity(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public Warehouse toEntity(CreateWarehouseRequest request) {
        Warehouse entity = new Warehouse();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setWarehouseCode(request.warehouseCode());
        entity.setLocation(request.location());
        entity.setCapacity(request.capacity());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(Warehouse entity, UpdateWarehouseRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.warehouseCode() != null) entity.setWarehouseCode(request.warehouseCode());
        if (request.location() != null) entity.setLocation(request.location());
        if (request.capacity() != null) entity.setCapacity(request.capacity());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
