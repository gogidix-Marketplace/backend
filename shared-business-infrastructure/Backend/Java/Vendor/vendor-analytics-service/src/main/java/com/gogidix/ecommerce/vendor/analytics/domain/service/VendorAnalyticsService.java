package com.gogidix.ecommerce.vendor.analytics.domain.service;

import com.gogidix.ecommerce.vendor.analytics.domain.model.VendorAnalytics;
import com.gogidix.ecommerce.vendor.analytics.domain.repository.VendorAnalyticsRepository;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorAnalyticsService {

    private final VendorAnalyticsRepository vendorAnalyticsRepository;

    public List<VendorAnalytics> findAll() { return vendorAnalyticsRepository.findByTenantId(RequestContextHolder.getTenantId()); }

    public VendorAnalytics findById(String id) { return vendorAnalyticsRepository.findByTenantIdAndId(RequestContextHolder.getTenantId(), id).orElse(null); }

    public VendorAnalytics create(VendorAnalytics entity) {
        entity.setTenantId(RequestContextHolder.getTenantId());
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        return vendorAnalyticsRepository.save(entity);
    }

    public VendorAnalytics update(String id, VendorAnalytics entity) {
        VendorAnalytics existing = findById(id);
        if (existing == null) return null;
        entity.setId(existing.getId());
        entity.setTenantId(existing.getTenantId());
        entity.setCreatedAt(existing.getCreatedAt());
        entity.setUpdatedAt(Instant.now());
        return vendorAnalyticsRepository.save(entity);
    }

    public void delete(String id) {
        VendorAnalytics existing = findById(id);
        if (existing != null) vendorAnalyticsRepository.delete(existing);
    }
}