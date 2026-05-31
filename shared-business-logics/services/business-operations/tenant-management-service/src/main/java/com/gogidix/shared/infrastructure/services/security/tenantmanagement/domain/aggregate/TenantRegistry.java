package com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.aggregate;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.event.TenantCreatedEvent;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.event.TenantDeletedEvent;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.event.TenantUpdatedEvent;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.model.Tenant;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.port.out.EventPublisherPort;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.port.out.TenantRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Tenant Registry Aggregate Root.
 * Manages tenant lifecycle and publishes domain events.
 */
public class TenantRegistry {

    private static final Logger log = LoggerFactory.getLogger(TenantRegistry.class);

    private final TenantRepositoryPort tenantRepository;
    private final EventPublisherPort eventPublisher;
    private final Map<String, Consumer<TenantCreatedEvent>> tenantCreatedHandlers = new ConcurrentHashMap<>();
    private final Map<String, Consumer<TenantUpdatedEvent>> tenantUpdatedHandlers = new ConcurrentHashMap<>();
    private final Map<String, Consumer<TenantDeletedEvent>> tenantDeletedHandlers = new ConcurrentHashMap<>();

    public TenantRegistry(TenantRepositoryPort tenantRepository, EventPublisherPort eventPublisher) {
        this.tenantRepository = tenantRepository;
        this.eventPublisher = eventPublisher;
    }

    // Event handler registration
    public void onTenantCreated(Consumer<TenantCreatedEvent> handler) {
        String handlerId = UUID.randomUUID().toString();
        tenantCreatedHandlers.put(handlerId, handler);
    }

    public void onTenantUpdated(Consumer<TenantUpdatedEvent> handler) {
        String handlerId = UUID.randomUUID().toString();
        tenantUpdatedHandlers.put(handlerId, handler);
    }

    public void onTenantDeleted(Consumer<TenantDeletedEvent> handler) {
        String handlerId = UUID.randomUUID().toString();
        tenantDeletedHandlers.put(handlerId, handler);
    }

    // Aggregate operations
    public Tenant createTenant(String name, String domain, String primaryContactEmail,
                              String primaryContactName, Tenant.TenantPlan plan,
                              long maxUsers, long maxStorageGB) {
        log.info("Creating tenant: name={}, domain={}, plan={}", name, domain, plan);

        String tenantId = generateTenantId();
        LocalDateTime now = LocalDateTime.now();

        Tenant.TenantStatus status = plan == Tenant.TenantPlan.FREE
            ? Tenant.TenantStatus.TRIAL
            : Tenant.TenantStatus.ACTIVE;

        LocalDateTime trialEndsAt = plan == Tenant.TenantPlan.FREE
            ? now.plusDays(14)
            : null;

        Tenant tenant = Tenant.builder()
            .tenantId(tenantId)
            .name(name)
            .domain(domain)
            .status(status)
            .plan(plan)
            .trialEndsAt(trialEndsAt)
            .primaryContactEmail(primaryContactEmail)
            .primaryContactName(primaryContactName)
            .maxUsers(maxUsers)
            .maxStorageGB(maxStorageGB)
            .settings(Map.of(
                "timezone", "UTC",
                "locale", "en_US",
                "dateFormat", "MM/dd/yyyy"
            ))
            .features(getDefaultFeaturesForPlan(plan))
            .build();

        Tenant saved = tenantRepository.save(tenant);

        TenantCreatedEvent event = new TenantCreatedEvent(saved);
        publishEvent(event);

        log.info("Tenant created successfully: tenantId={}", tenantId);
        return saved;
    }

    public Optional<Tenant> updateTenant(String tenantId, String name, String domain,
                                        String primaryContactEmail, String primaryContactName) {
        log.info("Updating tenant: tenantId={}", tenantId);

        Optional<Tenant> existingOpt = tenantRepository.findByTenantId(tenantId);
        if (existingOpt.isEmpty()) {
            log.warn("Tenant not found for update: tenantId={}", tenantId);
            return Optional.empty();
        }

        Tenant tenant = existingOpt.get();
        tenant.setName(name);
        tenant.setDomain(domain);
        tenant.setPrimaryContactEmail(primaryContactEmail);
        tenant.setPrimaryContactName(primaryContactName);

        Tenant updated = tenantRepository.save(tenant);

        TenantUpdatedEvent event = new TenantUpdatedEvent(updated);
        publishEvent(event);

        log.info("Tenant updated successfully: tenantId={}", tenantId);
        return Optional.of(updated);
    }

    public boolean updateTenantStatus(String tenantId, Tenant.TenantStatus newStatus) {
        log.info("Updating tenant status: tenantId={}, newStatus={}", tenantId, newStatus);

        Optional<Tenant> existingOpt = tenantRepository.findByTenantId(tenantId);
        if (existingOpt.isEmpty()) {
            log.warn("Tenant not found for status update: tenantId={}", tenantId);
            return false;
        }

        Tenant tenant = existingOpt.get();
        Tenant.TenantStatus oldStatus = tenant.getStatus();
        tenant.setStatus(newStatus);

        tenantRepository.save(tenant);

        TenantUpdatedEvent event = new TenantUpdatedEvent(tenant);
        publishEvent(event);

        log.info("Tenant status updated: tenantId={}, {}->{}", tenantId, oldStatus, newStatus);
        return true;
    }

    public boolean deleteTenant(String tenantId) {
        log.info("Deleting tenant: tenantId={}", tenantId);

        Optional<Tenant> existingOpt = tenantRepository.findByTenantId(tenantId);
        if (existingOpt.isEmpty()) {
            log.warn("Tenant not found for deletion: tenantId={}", tenantId);
            return false;
        }

        Tenant tenant = existingOpt.get();
        tenantRepository.deleteByTenantId(tenantId);

        TenantDeletedEvent event = new TenantDeletedEvent(
            tenantId,
            tenant.getName()
        );
        publishEvent(event);

        log.info("Tenant deleted successfully: tenantId={}", tenantId);
        return true;
    }

    public Optional<Tenant> getTenant(String tenantId) {
        return tenantRepository.findByTenantId(tenantId);
    }

    public Optional<Tenant> getTenantByDomain(String domain) {
        return tenantRepository.findByDomain(domain);
    }

    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    public List<Tenant> getTenantsByStatus(Tenant.TenantStatus status) {
        return tenantRepository.findByStatus(status);
    }

    public List<Tenant> getTenantsByPlan(Tenant.TenantPlan plan) {
        return tenantRepository.findByPlan(plan);
    }

    public boolean isTenantActive(String tenantId) {
        Optional<Tenant> tenant = getTenant(tenantId);
        return tenant.isPresent() && tenant.get().isActive();
    }

    // Utility methods
    private String generateTenantId() {
        return "tenant-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private Map<String, Object> getDefaultFeaturesForPlan(Tenant.TenantPlan plan) {
        return switch (plan) {
            case FREE -> Map.of(
                "maxUsers", 5L,
                "maxStorageGB", 1L,
                "apiAccess", false,
                "customBranding", false,
                "prioritySupport", false
            );
            case STARTER -> Map.of(
                "maxUsers", 25L,
                "maxStorageGB", 10L,
                "apiAccess", true,
                "customBranding", false,
                "prioritySupport", false
            );
            case PROFESSIONAL -> Map.of(
                "maxUsers", 100L,
                "maxStorageGB", 100L,
                "apiAccess", true,
                "customBranding", true,
                "prioritySupport", true,
                "advancedAnalytics", true
            );
            case ENTERPRISE -> Map.of(
                "maxUsers", -1L, // Unlimited
                "maxStorageGB", -1L, // Unlimited
                "apiAccess", true,
                "customBranding", true,
                "prioritySupport", true,
                "advancedAnalytics", true,
                "dedicatedSupport", true,
                "customIntegrations", true,
                "sso", true,
                "auditLogs", true
            );
            case CUSTOM -> Map.of(
                "maxUsers", -1L,
                "maxStorageGB", -1L,
                "apiAccess", true,
                "customBranding", true,
                "prioritySupport", true
            );
        };
    }

    private void publishEvent(TenantCreatedEvent event) {
        try {
            eventPublisher.publish(event);
            tenantCreatedHandlers.values().forEach(handler -> handler.accept(event));
        } catch (Exception e) {
            log.error("Failed to publish TenantCreatedEvent: {}", event.getTenantId(), e);
        }
    }

    private void publishEvent(TenantUpdatedEvent event) {
        try {
            eventPublisher.publish(event);
            tenantUpdatedHandlers.values().forEach(handler -> handler.accept(event));
        } catch (Exception e) {
            log.error("Failed to publish TenantUpdatedEvent: {}", event.getTenantId(), e);
        }
    }

    private void publishEvent(TenantDeletedEvent event) {
        try {
            eventPublisher.publish(event);
            tenantDeletedHandlers.values().forEach(handler -> handler.accept(event));
        } catch (Exception e) {
            log.error("Failed to publish TenantDeletedEvent: {}", event.getTenantId(), e);
        }
    }
}
