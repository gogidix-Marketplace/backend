package com.gogidix.centralconfiguration.featureflagservice.infrastructure.persistence.postgres;

import com.gogidix.centralconfiguration.featureflagservice.domain.model.FeatureFlag;
import com.gogidix.centralconfiguration.featureflagservice.domain.repository.FeatureFlagRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL implementation of FeatureFlagRepository.
 */
@Repository
public class PostgresFeatureFlagRepository implements FeatureFlagRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public FeatureFlag save(FeatureFlag featureFlag) {
        if (featureFlag.getId() == null) {
            entityManager.persist(featureFlag);
            return featureFlag;
        } else {
            return entityManager.merge(featureFlag);
        }
    }

    @Override
    public Optional<FeatureFlag> findById(Long id) {
        return Optional.ofNullable(entityManager.find(FeatureFlag.class, id));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<FeatureFlag> findByFlagKey(String flagKey) {
        List<FeatureFlag> results = entityManager.createQuery(
                "SELECT f FROM FeatureFlag f WHERE f.flagKey = :flagKey", FeatureFlag.class)
                .setParameter("flagKey", flagKey)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<FeatureFlag> findByTenantId(String tenantId) {
        return entityManager.createQuery(
                "SELECT f FROM FeatureFlag f WHERE f.tenantId = :tenantId ORDER BY f.createdAt DESC", FeatureFlag.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<FeatureFlag> findByTenantIdAndIsEnabled(String tenantId, Boolean isEnabled) {
        return entityManager.createQuery(
                "SELECT f FROM FeatureFlag f WHERE f.tenantId = :tenantId AND f.isEnabled = :enabled ORDER BY f.createdAt DESC", FeatureFlag.class)
                .setParameter("tenantId", tenantId)
                .setParameter("enabled", isEnabled)
                .getResultList();
    }

    @Override
    public void delete(FeatureFlag featureFlag) {
        if (entityManager.contains(featureFlag)) {
            entityManager.remove(featureFlag);
        } else {
            entityManager.remove(entityManager.merge(featureFlag));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean existsByFlagKey(String flagKey) {
        Long count = (Long) entityManager.createQuery(
                "SELECT COUNT(f) FROM FeatureFlag f WHERE f.flagKey = :flagKey")
                .setParameter("flagKey", flagKey)
                .getSingleResult();
        return count > 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<FeatureFlag> findAll(int page, int size) {
        return entityManager.createQuery(
                "SELECT f FROM FeatureFlag f ORDER BY f.createdAt DESC", FeatureFlag.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }
}
