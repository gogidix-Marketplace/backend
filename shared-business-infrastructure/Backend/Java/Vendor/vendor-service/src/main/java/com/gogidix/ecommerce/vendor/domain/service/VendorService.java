package com.gogidix.ecommerce.vendor.domain.service;

import com.gogidix.ecommerce.vendor.domain.model.Vendor;
import com.gogidix.ecommerce.vendor.domain.repository.VendorRepository;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;

    public List<Vendor> findAll() { return vendorRepository.findByTenantId(RequestContextHolder.getTenantId()); }

    public Vendor findById(String id) { return vendorRepository.findByTenantIdAndId(RequestContextHolder.getTenantId(), id).orElse(null); }

    public Vendor create(Vendor entity) {
        entity.setTenantId(RequestContextHolder.getTenantId());
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        return vendorRepository.save(entity);
    }

    public Vendor update(String id, Vendor entity) {
        Vendor existing = findById(id);
        if (existing == null) return null;
        entity.setId(existing.getId());
        entity.setTenantId(existing.getTenantId());
        entity.setCreatedAt(existing.getCreatedAt());
        entity.setUpdatedAt(Instant.now());
        return vendorRepository.save(entity);
    }

    public void delete(String id) {
        Vendor existing = findById(id);
        if (existing != null) vendorRepository.delete(existing);
    }
}