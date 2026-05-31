package com.gogidix.ecommerce.vendor.onboarding.domain.service;

import com.gogidix.ecommerce.vendor.onboarding.domain.model.Onboarding;
import com.gogidix.ecommerce.vendor.onboarding.domain.repository.OnboardingRepository;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final OnboardingRepository onboardingRepository;

    public List<Onboarding> findAll() { return onboardingRepository.findByTenantId(RequestContextHolder.getTenantId()); }

    public Onboarding findById(String id) { return onboardingRepository.findByTenantIdAndId(RequestContextHolder.getTenantId(), id).orElse(null); }

    public Onboarding create(Onboarding entity) {
        entity.setTenantId(RequestContextHolder.getTenantId());
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        return onboardingRepository.save(entity);
    }

    public Onboarding update(String id, Onboarding entity) {
        Onboarding existing = findById(id);
        if (existing == null) return null;
        entity.setId(existing.getId());
        entity.setTenantId(existing.getTenantId());
        entity.setCreatedAt(existing.getCreatedAt());
        entity.setUpdatedAt(Instant.now());
        return onboardingRepository.save(entity);
    }

    public void delete(String id) {
        Onboarding existing = findById(id);
        if (existing != null) onboardingRepository.delete(existing);
    }
}