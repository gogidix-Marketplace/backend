package com.gogidix.courier.tenantservice.application.service;

import com.gogidix.courier.tenantservice.application.command.CreateTenantCommand;
import com.gogidix.courier.tenantservice.application.command.UpdateConfigCommand;
import com.gogidix.courier.tenantservice.application.command.UpdateTenantCommand;
import com.gogidix.courier.tenantservice.application.dto.PagedResponseDto;
import com.gogidix.courier.tenantservice.application.dto.TenantConfigRequest;
import com.gogidix.courier.tenantservice.application.dto.TenantConfigResponse;
import com.gogidix.courier.tenantservice.application.dto.TenantRequest;
import com.gogidix.courier.tenantservice.application.dto.TenantResponse;
import com.gogidix.courier.tenantservice.application.mapper.TenantMapper;
import com.gogidix.courier.tenantservice.domain.entity.Tenant;
import com.gogidix.courier.tenantservice.domain.entity.TenantConfig;
import com.gogidix.courier.tenantservice.domain.event.TenantConfigChangedEvent;
import com.gogidix.courier.tenantservice.domain.event.TenantCreatedEvent;
import com.gogidix.courier.tenantservice.domain.event.TenantUpdatedEvent;
import com.gogidix.courier.tenantservice.domain.repository.TenantRepository;
import com.gogidix.courier.tenantservice.shared.exception.NotFoundException;
import com.gogidix.courier.tenantservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Application service for tenant operations.
 */
@Service
@Transactional(readOnly = true)
public class TenantApplicationService {

    private static final Logger log = LoggerFactory.getLogger(TenantApplicationService.class);
    private static final String CACHE_NAME = "tenants";

    private final TenantRepository repository;
    private final TenantMapper mapper;
    private final TenantEventPublisher eventPublisher;

    public TenantApplicationService(
            TenantRepository repository,
            TenantMapper mapper,
            TenantEventPublisher eventPublisher) {
        this.repository = repository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Create a new tenant.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public TenantResponse createTenant(TenantRequest request, String userId) {
        log.info("Creating tenant: {}", request.tenantId());

        if (repository.existsByTenantId(request.tenantId())) {
            throw new ValidationException("Tenant with tenantId '" + request.tenantId() + "' already exists");
        }

        if (repository.existsByName(request.name())) {
            throw new ValidationException("Tenant with name '" + request.name() + "' already exists");
        }

        Tenant tenant = mapper.toEntity(request);
        tenant.validate();

        Tenant saved = repository.save(tenant);

        // Publish domain event
        eventPublisher.publish(new TenantCreatedEvent(
                saved.getId(),
                saved.getTenantId(),
                saved.getName()
        ));

        log.info("Tenant created with ID: {}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Get a tenant by ID.
     */
    @Cacheable(value = CACHE_NAME, key = "#id")
    public TenantResponse getTenantById(String id) {
        log.debug("Fetching tenant: {}", id);

        Tenant tenant = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tenant", id));

        return mapper.toResponseDto(tenant);
    }

    /**
     * Get a tenant by tenantId.
     */
    @Cacheable(value = CACHE_NAME, key = "'tenantId:' + #tenantId")
    public TenantResponse getTenantByTenantId(String tenantId) {
        log.debug("Fetching tenant by tenantId: {}", tenantId);

        Tenant tenant = repository.findByTenantId(tenantId)
                .orElseThrow(() -> new NotFoundException("Tenant", tenantId));

        return mapper.toResponseDto(tenant);
    }

    /**
     * List tenants with pagination and filtering.
     */
    public PagedResponseDto<TenantResponse> listTenants(
            String name,
            Tenant.TenantStatus status,
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        log.debug("Listing tenants - page: {}, size: {}", page, size);

        List<Tenant> allTenants = repository.findAll();

        // Apply filters
        List<Tenant> filtered = allTenants.stream()
                .filter(t -> name == null || t.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(t -> status == null || t.getStatus() == status)
                .collect(Collectors.toList());

        // Apply sorting
        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        int start = page * size;
        int end = Math.min(start + size, filtered.size());

        List<Tenant> pagedTenants = new ArrayList<>();
        if (start < filtered.size()) {
            pagedTenants = filtered.subList(start, end);
        }

        List<TenantResponse> responses = pagedTenants.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());

        return PagedResponseDto.of(responses, page, size, filtered.size());
    }

    /**
     * Update an existing tenant.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#id")
    public TenantResponse updateTenant(String id, TenantRequest request, String userId) {
        log.info("Updating tenant: {}", id);

        Tenant tenant = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tenant", id));

        // Check if name is being changed and if it conflicts
        if (!tenant.getName().equals(request.name()) && repository.existsByName(request.name())) {
            throw new ValidationException("Tenant with name '" + request.name() + "' already exists");
        }

        mapper.updateEntityFromRequest(tenant, request);
        tenant.validate();

        Tenant saved = repository.save(tenant);

        // Publish domain event
        eventPublisher.publish(new TenantUpdatedEvent(
                saved.getId(),
                saved.getTenantId(),
                saved.getName(),
                saved.getStatus()
        ));

        log.info("Tenant updated: {}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Get tenant configuration.
     */
    @Cacheable(value = CACHE_NAME + "Config", key = "#id")
    public TenantConfigResponse getTenantConfig(String id) {
        log.debug("Fetching config for tenant: {}", id);

        Tenant tenant = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tenant", id));

        return mapper.toConfigResponseDto(tenant.getConfig());
    }

    /**
     * Update tenant configuration.
     */
    @Transactional
    @CacheEvict(value = {CACHE_NAME, CACHE_NAME + "Config"}, key = "#id")
    public TenantConfigResponse updateTenantConfig(String id, TenantConfigRequest request, String userId) {
        log.info("Updating config for tenant: {}", id);

        Tenant tenant = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tenant", id));

        TenantConfig newConfig = mapper.toConfigEntity(request);
        tenant.updateConfig(newConfig);

        Tenant saved = repository.save(tenant);

        // Publish domain event
        Map<String, Object> changedSettings = Map.of(
                "timestamp", System.currentTimeMillis(),
                "updatedBy", userId
        );
        eventPublisher.publish(new TenantConfigChangedEvent(
                saved.getId(),
                saved.getTenantId(),
                saved.getName(),
                changedSettings
        ));

        log.info("Tenant config updated: {}", saved.getId());
        return mapper.toConfigResponseDto(saved.getConfig());
    }

    /**
     * Delete a tenant.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#id")
    public void deleteTenant(String id) {
        log.info("Deleting tenant: {}", id);

        Tenant tenant = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tenant", id));

        // Soft delete by terminating
        tenant.terminate();
        repository.save(tenant);

        log.info("Tenant deleted: {}", id);
    }

    /**
     * Activate a tenant.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#id")
    public TenantResponse activateTenant(String id) {
        log.info("Activating tenant: {}", id);

        Tenant tenant = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tenant", id));

        tenant.activate();
        Tenant saved = repository.save(tenant);

        log.info("Tenant activated: {}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Deactivate a tenant.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#id")
    public TenantResponse deactivateTenant(String id) {
        log.info("Deactivating tenant: {}", id);

        Tenant tenant = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tenant", id));

        tenant.deactivate();
        Tenant saved = repository.save(tenant);

        log.info("Tenant deactivated: {}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Count all tenants.
     */
    public long countTenants() {
        return repository.count();
    }

    /**
     * Count tenants by status.
     */
    public long countTenantsByStatus(Tenant.TenantStatus status) {
        return repository.countByStatus(status);
    }
}
