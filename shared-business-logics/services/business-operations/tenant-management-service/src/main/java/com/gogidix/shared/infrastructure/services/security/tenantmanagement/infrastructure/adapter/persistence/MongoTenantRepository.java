package com.gogidix.shared.infrastructure.services.security.tenantmanagement.infrastructure.adapter.persistence;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.exception.TenantNotFoundException;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.model.Tenant;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.port.out.TenantRepositoryPort;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.shared.requestcontext.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB implementation of the TenantRepositoryPort.
 * Enforces multi-tenant data isolation at the database level.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MongoTenantRepository implements TenantRepositoryPort {

    private final MongoTemplate mongoTemplate;

    @Override
    public Tenant save(Tenant tenant) {
        // Ensure tenantId is set from context if not already present
        String tenantId = TenantContextHolder.getTenantId();
        if (tenant.getTenantId() == null || tenant.getTenantId().isBlank()) {
            tenant.setTenantId(tenantId);
        }

        log.debug("Saving tenant: id={}, tenantId={}", tenant.getId(), tenant.getTenantId());
        return mongoTemplate.save(tenant);
    }

    @Override
    public Optional<Tenant> findByTenantId(String tenantIdValue) {
        String tenantId = TenantContextHolder.getTenantId();
        log.debug("Finding tenant by tenantId={}, context tenantId={}", tenantIdValue, tenantId);

        // For tenant lookup by ID, we must ensure the context tenantId matches
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantIdValue)
                                .and("tenantId").is(tenantId));

        Tenant result = mongoTemplate.findOne(query, Tenant.class);
        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsByTenantId(String tenantIdValue) {
        String tenantId = TenantContextHolder.getTenantId();
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantIdValue)
                                .and("tenantId").is(tenantId));
        return mongoTemplate.exists(query, Tenant.class);
    }

    @Override
    public boolean existsByDomain(String domain) {
        String tenantId = TenantContextHolder.getTenantId();
        Query query = new Query();
        query.addCriteria(Criteria.where("domain").is(domain)
                                .and("tenantId").is(tenantId));
        return mongoTemplate.exists(query, Tenant.class);
    }

    @Override
    public Optional<Tenant> findByDomain(String domain) {
        String tenantId = TenantContextHolder.getTenantId();
        Query query = new Query();
        query.addCriteria(Criteria.where("domain").is(domain)
                                .and("tenantId").is(tenantId));
        return Optional.ofNullable(mongoTemplate.findOne(query, Tenant.class));
    }

    @Override
    public List<Tenant> findAll() {
        String tenantId = TenantContextHolder.getTenantId();
        log.debug("Finding all tenants for tenantId={}", tenantId);

        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));

        return mongoTemplate.find(query, Tenant.class);
    }

    @Override
    public void delete(Tenant tenant) {
        String tenantId = TenantContextHolder.getTenantId();
        log.debug("Deleting tenant: id={}, tenantId={}", tenant.getId(), tenantId);

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(tenant.getId())
                                .and("tenantId").is(tenantId));

        var result = mongoTemplate.remove(query, Tenant.class);
        if (result.getDeletedCount() == 0) {
            throw new TenantNotFoundException("Tenant not found with id: " + tenant.getId());
        }
    }

    @Override
    public void deleteByTenantId(String tenantIdValue) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantIdValue));
        mongoTemplate.remove(query, Tenant.class);
    }

    @Override
    public List<Tenant> findByStatus(Tenant.TenantStatus status) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(status));
        return mongoTemplate.find(query, Tenant.class);
    }

    @Override
    public List<Tenant> findByPlan(Tenant.TenantPlan plan) {
        Query query = new Query();
        query.addCriteria(Criteria.where("plan").is(plan));
        return mongoTemplate.find(query, Tenant.class);
    }
}
