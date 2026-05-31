package com.gogidix.ecommerce.vendor.analytics.application.mapper;

import com.gogidix.ecommerce.vendor.analytics.application.dto.CreateVendorAnalyticsRequest;
import com.gogidix.ecommerce.vendor.analytics.application.dto.VendorAnalyticsResponse;
import com.gogidix.ecommerce.vendor.analytics.domain.model.VendorAnalytics;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Component;

@Component
public class VendorAnalyticsMapper {

    public VendorAnalyticsResponse toResponse(VendorAnalytics entity) {
        return new VendorAnalyticsResponse(entity.getId(), entity.getTenantId(), null, entity.getCreatedAt(), entity.getUpdatedAt());
    }

    public VendorAnalytics toEntity(CreateVendorAnalyticsRequest request) {
        VendorAnalytics entity = new VendorAnalytics();
        entity.setTenantId(RequestContextHolder.getTenantId());
        entity.setName(request.name());
        return entity;
    }
}