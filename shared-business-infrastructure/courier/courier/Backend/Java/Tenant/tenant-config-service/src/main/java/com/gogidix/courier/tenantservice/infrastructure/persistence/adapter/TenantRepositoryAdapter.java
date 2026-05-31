package com.gogidix.courier.tenantservice.infrastructure.persistence.adapter;

import com.gogidix.courier.tenantservice.domain.entity.Tenant;
import com.gogidix.courier.tenantservice.domain.repository.TenantRepository;
import com.gogidix.courier.tenantservice.infrastructure.persistence.repository.MongoTenantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB implementation of TenantRepository.
 */
@Component
public class TenantRepositoryAdapter implements TenantRepository {

    private final MongoTenantRepository mongoRepository;

    public TenantRepositoryAdapter(MongoTenantRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Tenant save(Tenant tenant) {
        return mongoRepository.save(tenant);
    }

    @Override
    public Optional<Tenant> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<Tenant> findByTenantId(String tenantId) {
        return mongoRepository.findByTenantId(tenantId);
    }

    @Override
    public List<Tenant> findAll() {
        return mongoRepository.findAll();
    }

    @Override
    public List<Tenant> findByStatus(Tenant.TenantStatus status) {
        return mongoRepository.findByStatus(status);
    }

    @Override
    public Optional<Tenant> findByName(String name) {
        return mongoRepository.findByName(name);
    }

    @Override
    public boolean existsByTenantId(String tenantId) {
        return mongoRepository.existsByTenantId(tenantId);
    }

    @Override
    public boolean existsByName(String name) {
        return mongoRepository.existsByName(name);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public long count() {
        return mongoRepository.count();
    }

    @Override
    public long countByStatus(Tenant.TenantStatus status) {
        return mongoRepository.countByStatus(status);
    }

    @Override
    public List<Tenant> findAllPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Tenant> result = mongoRepository.findAll(pageable);
        return result.getContent();
    }
}
