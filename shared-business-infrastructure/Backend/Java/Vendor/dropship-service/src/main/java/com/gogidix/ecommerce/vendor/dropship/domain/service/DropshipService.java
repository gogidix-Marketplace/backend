package com.gogidix.ecommerce.vendor.dropship.domain.service;

import com.gogidix.ecommerce.vendor.dropship.domain.model.Dropship;
import com.gogidix.ecommerce.vendor.dropship.domain.repository.DropshipRepository;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DropshipService {

    private final DropshipRepository dropshipRepository;

    public List<Dropship> findAll() { return dropshipRepository.findByTenantId(RequestContextHolder.getTenantId()); }

    public Dropship findById(String id) { return dropshipRepository.findByTenantIdAndId(RequestContextHolder.getTenantId(), id).orElse(null); }

    public Dropship create(Dropship entity) {
        entity.setTenantId(RequestContextHolder.getTenantId());
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        return dropshipRepository.save(entity);
    }

    public Dropship update(String id, Dropship entity) {
        Dropship existing = findById(id);
        if (existing == null) return null;
        entity.setId(existing.getId());
        entity.setTenantId(existing.getTenantId());
        entity.setCreatedAt(existing.getCreatedAt());
        entity.setUpdatedAt(Instant.now());
        return dropshipRepository.save(entity);
    }

    public void delete(String id) {
        Dropship existing = findById(id);
        if (existing != null) dropshipRepository.delete(existing);
    }
}