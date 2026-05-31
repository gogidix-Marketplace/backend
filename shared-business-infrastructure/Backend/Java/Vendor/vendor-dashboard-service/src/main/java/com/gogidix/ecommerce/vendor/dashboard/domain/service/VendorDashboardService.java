package com.gogidix.ecommerce.vendor.dashboard.domain.service;

import com.gogidix.ecommerce.vendor.dashboard.domain.model.VendorDashboard;
import com.gogidix.ecommerce.vendor.dashboard.domain.repository.VendorDashboardRepository;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorDashboardService {

    private final VendorDashboardRepository vendorDashboardRepository;

    public List<VendorDashboard> findAll() { return vendorDashboardRepository.findByTenantId(RequestContextHolder.getTenantId()); }

    public VendorDashboard findById(String id) { return vendorDashboardRepository.findByTenantIdAndId(RequestContextHolder.getTenantId(), id).orElse(null); }

    public VendorDashboard create(VendorDashboard entity) {
        entity.setTenantId(RequestContextHolder.getTenantId());
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        return vendorDashboardRepository.save(entity);
    }

    public VendorDashboard update(String id, VendorDashboard entity) {
        VendorDashboard existing = findById(id);
        if (existing == null) return null;
        entity.setId(existing.getId());
        entity.setTenantId(existing.getTenantId());
        entity.setCreatedAt(existing.getCreatedAt());
        entity.setUpdatedAt(Instant.now());
        return vendorDashboardRepository.save(entity);
    }

    public void delete(String id) {
        VendorDashboard existing = findById(id);
        if (existing != null) vendorDashboardRepository.delete(existing);
    }
}