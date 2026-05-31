package com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.service;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.request.CreateTenantRequestDto;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.request.UpdateTenantRequestDto;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.response.TenantResponseDto;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.mapper.TenantMapper;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.exception.TenantNotFoundException;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.model.Tenant;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.port.in.TenantManagementPort;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.port.out.TenantRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Tenant Management Service implementation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantManagementService implements TenantManagementPort {

    private final TenantRepositoryPort tenantRepository;
    private final TenantMapper tenantMapper;

    @Override
    @Transactional
    public TenantResponseDto createTenant(CreateTenantRequestDto request) {
        log.info("Creating tenant: {}", request.getName());

        // Check if tenant ID already exists
        if (tenantRepository.existsByTenantId(request.getTenantId())) {
            throw new IllegalArgumentException("Tenant ID already exists: " + request.getTenantId());
        }

        // Check if domain already exists
        if (request.getDomain() != null && tenantRepository.existsByDomain(request.getDomain())) {
            throw new IllegalArgumentException("Domain already exists: " + request.getDomain());
        }

        Tenant tenant = Tenant.builder()
                .tenantId(request.getTenantId())
                .name(request.getName())
                .domain(request.getDomain())
                .logoUrl(request.getLogoUrl())
                .status(request.getStatus() != null ? request.getStatus() : Tenant.TenantStatus.TRIAL)
                .plan(request.getPlan() != null ? request.getPlan() : Tenant.TenantPlan.FREE)
                .trialEndsAt(request.getTrialEndsAt() != null
                    ? request.getTrialEndsAt()
                    : LocalDateTime.now().plusDays(14))
                .settings(request.getSettings())
                .features(request.getFeatures())
                .primaryContactEmail(request.getPrimaryContactEmail())
                .primaryContactName(request.getPrimaryContactName())
                .maxUsers(request.getMaxUsers() != null ? request.getMaxUsers() : 5)
                .maxStorageGB(request.getMaxStorageGB() != null ? request.getMaxStorageGB() : 10)
                .build();

        Tenant saved = tenantRepository.save(tenant);
        log.info("Tenant created: {}", saved.getTenantId());

        return tenantMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public TenantResponseDto updateTenant(String tenantId, UpdateTenantRequestDto request) {
        log.info("Updating tenant: {}", tenantId);

        Tenant tenant = tenantRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new TenantNotFoundException(tenantId));

        // Update fields if provided
        if (request.getName() != null) {
            tenant.setName(request.getName());
        }
        if (request.getLogoUrl() != null) {
            tenant.setLogoUrl(request.getLogoUrl());
        }
        if (request.getPrimaryContactEmail() != null) {
            tenant.setPrimaryContactEmail(request.getPrimaryContactEmail());
        }
        if (request.getPrimaryContactName() != null) {
            tenant.setPrimaryContactName(request.getPrimaryContactName());
        }
        if (request.getMaxUsers() != null) {
            tenant.setMaxUsers(request.getMaxUsers());
        }
        if (request.getMaxStorageGB() != null) {
            tenant.setMaxStorageGB(request.getMaxStorageGB());
        }
        if (request.getSettings() != null) {
            tenant.setSettings(request.getSettings());
        }
        if (request.getFeatures() != null) {
            tenant.setFeatures(request.getFeatures());
        }

        Tenant saved = tenantRepository.save(tenant);
        log.info("Tenant updated: {}", saved.getTenantId());

        return tenantMapper.toResponseDto(saved);
    }

    @Override
    public Optional<TenantResponseDto> getTenant(String tenantId) {
        return tenantRepository.findByTenantId(tenantId)
                .map(tenantMapper::toResponseDto);
    }

    @Override
    public Optional<TenantResponseDto> getTenantByDomain(String domain) {
        return tenantRepository.findByDomain(domain)
                .map(tenantMapper::toResponseDto);
    }

    @Override
    public List<TenantResponseDto> listTenants() {
        return tenantRepository.findAll().stream()
                .map(tenantMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void activateTenant(String tenantId) {
        Tenant tenant = tenantRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new TenantNotFoundException(tenantId));

        tenant.setStatus(Tenant.TenantStatus.ACTIVE);
        tenantRepository.save(tenant);

        log.info("Tenant activated: {}", tenantId);
    }

    @Override
    @Transactional
    public void suspendTenant(String tenantId) {
        Tenant tenant = tenantRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new TenantNotFoundException(tenantId));

        tenant.setStatus(Tenant.TenantStatus.SUSPENDED);
        tenantRepository.save(tenant);

        log.info("Tenant suspended: {}", tenantId);
    }

    @Override
    @Transactional
    public void deleteTenant(String tenantId) {
        Tenant tenant = tenantRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new TenantNotFoundException(tenantId));

        tenantRepository.delete(tenant);
        log.info("Tenant deleted: {}", tenantId);
    }
}
