package com.gogidix.shared.warehousing.tenant.application.service;

import com.gogidix.shared.warehousing.tenant.application.command.CreateTenantCommand;
import com.gogidix.shared.warehousing.tenant.application.command.UpdateTenantCommand;
import com.gogidix.shared.warehousing.tenant.application.mapper.TenantDtoMapper;
import com.gogidix.shared.warehousing.tenant.domain.entity.Tenant;
import com.gogidix.shared.warehousing.tenant.domain.events.TenantCreatedEvent;
import com.gogidix.shared.warehousing.tenant.domain.repository.TenantRepository;
import com.gogidix.shared.warehousing.tenant.infrastructure.messaging.TenantEventPublisher;
import com.gogidix.shared.warehousing.tenant.interfaces.rest.dto.TenantResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Tenant Configuration Application Service
 *
 * Handles tenant configuration operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TenantConfigApplicationService {

    private final TenantRepository tenantRepository;
    private final TenantDtoMapper tenantDtoMapper;
    private final TenantEventPublisher eventPublisher;

    /**
     * Create a new tenant
     */
    public TenantResponse createTenant(CreateTenantCommand command) {
        log.info("Creating tenant with ID: {}", command.getTenantId());

        if (tenantRepository.existsByTenantId(command.getTenantId())) {
            throw new IllegalArgumentException("Tenant already exists: " + command.getTenantId());
        }

        Tenant tenant = tenantDtoMapper.toEntity(command);
        Tenant savedTenant = tenantRepository.save(tenant);

        // Publish domain event
        TenantCreatedEvent event = TenantCreatedEvent.builder()
                .tenantId(savedTenant.getTenantId())
                .tenantName(savedTenant.getTenantName())
                .tenantType(savedTenant.getTenantType())
                .storageModel(savedTenant.getStorageModel())
                .businessRules(savedTenant.getBusinessRules())
                .createdAt(savedTenant.getCreatedAt())
                .build();
        eventPublisher.publishTenantCreated(event);

        log.info("Tenant created with ID: {}", savedTenant.getTenantId());
        return tenantDtoMapper.toDTO(savedTenant);
    }

    /**
     * Get tenant by tenant ID
     */
    @Transactional(readOnly = true)
    public TenantResponse getTenantByTenantId(String tenantId) {
        Tenant tenant = tenantRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + tenantId));
        return tenantDtoMapper.toDTO(tenant);
    }

    /**
     * Get all tenants
     */
    @Transactional(readOnly = true)
    public List<TenantResponse> getAllTenants() {
        List<Tenant> tenants = tenantRepository.findAll();
        return tenantDtoMapper.toDTOList(tenants);
    }

    /**
     * Update tenant
     */
    public TenantResponse updateTenant(String tenantId, UpdateTenantCommand command) {
        log.info("Updating tenant: {}", tenantId);

        Tenant tenant = tenantRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + tenantId));

        tenantDtoMapper.updateEntity(tenant, command);
        Tenant updatedTenant = tenantRepository.save(tenant);

        log.info("Tenant updated: {}", tenantId);
        return tenantDtoMapper.toDTO(updatedTenant);
    }

    /**
     * Delete tenant
     */
    public void deleteTenant(String tenantId) {
        log.info("Deleting tenant: {}", tenantId);

        Tenant tenant = tenantRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + tenantId));

        tenantRepository.delete(tenant);
        log.info("Tenant deleted: {}", tenantId);
    }

    /**
     * Get business rules for tenant
     */
    @Transactional(readOnly = true)
    public Object getBusinessRules(String tenantId) {
        Tenant tenant = tenantRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + tenantId));
        return tenant.getBusinessRules();
    }

    /**
     * Update business rules for tenant
     */
    public TenantResponse updateBusinessRules(String tenantId, Object businessRules) {
        log.info("Updating business rules for tenant: {}", tenantId);

        Tenant tenant = tenantRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + tenantId));

        tenant.setBusinessRules((java.util.Map<String, Object>) businessRules);
        Tenant updatedTenant = tenantRepository.save(tenant);

        log.info("Business rules updated for tenant: {}", tenantId);
        return tenantDtoMapper.toDTO(updatedTenant);
    }
}
