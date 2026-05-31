package com.gogidix.ecommerce.vendor.application.mapper;

import com.gogidix.ecommerce.vendor.application.dto.CreateVendorRequest;
import com.gogidix.ecommerce.vendor.application.dto.VendorResponse;
import com.gogidix.ecommerce.vendor.domain.model.Vendor;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Component;

@Component
public class VendorMapper {

    public VendorResponse toResponse(Vendor entity) {
        return new VendorResponse(entity.getId(), entity.getTenantId(), null, entity.getCreatedAt(), entity.getUpdatedAt());
    }

    public Vendor toEntity(CreateVendorRequest request) {
        Vendor entity = new Vendor();
        entity.setTenantId(RequestContextHolder.getTenantId());
        entity.setName(request.name());
        return entity;
    }
}