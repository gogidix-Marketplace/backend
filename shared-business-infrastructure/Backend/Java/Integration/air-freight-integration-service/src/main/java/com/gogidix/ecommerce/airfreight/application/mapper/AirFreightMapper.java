package com.gogidix.ecommerce.airfreight.application.mapper;

import com.gogidix.ecommerce.airfreight.application.dto.*;
import com.gogidix.ecommerce.airfreight.domain.model.AirFreight;
import org.springframework.stereotype.Component;

@Component
public class AirFreightMapper {

    public AirFreightResponse toResponse(AirFreight entity) {
        if (entity == null) return null;
        return new AirFreightResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getCarrierCode(),
            entity.getOrigin(),
            entity.getDestination(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public AirFreight toEntity(CreateAirFreightRequest request) {
        AirFreight entity = new AirFreight();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setCarrierCode(request.carrierCode());
        entity.setOrigin(request.origin());
        entity.setDestination(request.destination());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(AirFreight entity, UpdateAirFreightRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.carrierCode() != null) entity.setCarrierCode(request.carrierCode());
        if (request.origin() != null) entity.setOrigin(request.origin());
        if (request.destination() != null) entity.setDestination(request.destination());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
