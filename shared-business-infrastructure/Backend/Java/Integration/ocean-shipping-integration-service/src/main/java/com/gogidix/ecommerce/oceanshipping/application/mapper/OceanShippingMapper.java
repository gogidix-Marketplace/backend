package com.gogidix.ecommerce.oceanshipping.application.mapper;

import com.gogidix.ecommerce.oceanshipping.application.dto.*;
import com.gogidix.ecommerce.oceanshipping.domain.model.OceanShipping;
import org.springframework.stereotype.Component;

@Component
public class OceanShippingMapper {

    public OceanShippingResponse toResponse(OceanShipping entity) {
        if (entity == null) return null;
        return new OceanShippingResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getCarrierName(),
            entity.getShippingRoute(),
            entity.getEstimatedTransitTime(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public OceanShipping toEntity(CreateOceanShippingRequest request) {
        OceanShipping entity = new OceanShipping();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setCarrierName(request.carrierName());
        entity.setShippingRoute(request.shippingRoute());
        entity.setEstimatedTransitTime(request.estimatedTransitTime());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(OceanShipping entity, UpdateOceanShippingRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.carrierName() != null) entity.setCarrierName(request.carrierName());
        if (request.shippingRoute() != null) entity.setShippingRoute(request.shippingRoute());
        if (request.estimatedTransitTime() != null) entity.setEstimatedTransitTime(request.estimatedTransitTime());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
