package com.gogidix.ecommerce.vendor.dropship.application.mapper;

import com.gogidix.ecommerce.vendor.dropship.application.dto.CreateDropshipRequest;
import com.gogidix.ecommerce.vendor.dropship.application.dto.DropshipResponse;
import com.gogidix.ecommerce.vendor.dropship.domain.model.Dropship;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Component;

@Component
public class DropshipMapper {

    public DropshipResponse toResponse(Dropship entity) {
        return new DropshipResponse(entity.getId(), entity.getTenantId(), null, entity.getCreatedAt(), entity.getUpdatedAt());
    }

    public Dropship toEntity(CreateDropshipRequest request) {
        Dropship entity = new Dropship();
        entity.setTenantId(RequestContextHolder.getTenantId());
        entity.setName(request.name());
        return entity;
    }
}