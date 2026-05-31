package com.gogidix.ecommerce.vendor.dashboard.application.mapper;

import com.gogidix.ecommerce.vendor.dashboard.application.dto.CreateVendorDashboardRequest;
import com.gogidix.ecommerce.vendor.dashboard.application.dto.VendorDashboardResponse;
import com.gogidix.ecommerce.vendor.dashboard.domain.model.VendorDashboard;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Component;

@Component
public class VendorDashboardMapper {

    public VendorDashboardResponse toResponse(VendorDashboard entity) {
        return new VendorDashboardResponse(entity.getId(), entity.getTenantId(), null, entity.getCreatedAt(), entity.getUpdatedAt());
    }

    public VendorDashboard toEntity(CreateVendorDashboardRequest request) {
        VendorDashboard entity = new VendorDashboard();
        entity.setTenantId(RequestContextHolder.getTenantId());
        entity.setName(request.name());
        return entity;
    }
}