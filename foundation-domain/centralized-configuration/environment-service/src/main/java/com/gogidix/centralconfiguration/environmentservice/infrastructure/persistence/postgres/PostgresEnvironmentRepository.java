package com.gogidix.centralconfiguration.environmentservice.infrastructure.persistence.postgres;

import com.gogidix.centralconfiguration.environmentservice.domain.model.Environment;
import com.gogidix.centralconfiguration.environmentservice.domain.repository.EnvironmentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL implementation of EnvironmentRepository.
 */
@Repository
public class PostgresEnvironmentRepository implements EnvironmentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Environment save(Environment environment) {
        if (environment.getId() == null) {
            entityManager.persist(environment);
            return environment;
        } else {
            return entityManager.merge(environment);
        }
    }

    @Override
    public Optional<Environment> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Environment.class, id));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<Environment> findByEnvironmentName(String environmentName) {
        List<Environment> results = entityManager.createQuery(
                "SELECT e FROM Environment e WHERE e.environmentName = :name", Environment.class)
                .setParameter("name", environmentName)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Environment> findByTenantId(String tenantId) {
        return entityManager.createQuery(
                "SELECT e FROM Environment e WHERE e.tenantId = :tenantId ORDER BY e.priority", Environment.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Environment> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return entityManager.createQuery(
                "SELECT e FROM Environment e WHERE e.tenantId = :tenantId AND e.isActive = :active ORDER BY e.priority", Environment.class)
                .setParameter("tenantId", tenantId)
                .setParameter("active", isActive)
                .getResultList();
    }

    @Override
    public void delete(Environment environment) {
        if (entityManager.contains(environment)) {
            entityManager.remove(environment);
        } else {
            entityManager.remove(entityManager.merge(environment));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean existsByEnvironmentName(String environmentName) {
        Long count = (Long) entityManager.createQuery(
                "SELECT COUNT(e) FROM Environment e WHERE e.environmentName = :name")
                .setParameter("name", environmentName)
                .getSingleResult();
        return count > 0;
    }
}
