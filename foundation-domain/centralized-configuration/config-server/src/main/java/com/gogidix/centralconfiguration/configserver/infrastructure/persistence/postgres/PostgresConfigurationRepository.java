package com.gogidix.centralconfiguration.configserver.infrastructure.persistence.postgres;

import com.gogidix.centralconfiguration.configserver.domain.model.Configuration;
import com.gogidix.centralconfiguration.configserver.domain.repository.ConfigurationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL implementation of ConfigurationRepository.
 * Handles persistence operations for Configuration entities.
 */
@Repository
public class PostgresConfigurationRepository implements ConfigurationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Configuration save(Configuration configuration) {
        if (configuration.getId() == null) {
            entityManager.persist(configuration);
            return configuration;
        } else {
            return entityManager.merge(configuration);
        }
    }

    @Override
    public Optional<Configuration> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Configuration.class, id));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<Configuration> findByTenantIdAndApplicationNameAndProfileAndConfigKey(
            String tenantId, String applicationName, String profile, String configKey) {
        List<Configuration> results = entityManager.createQuery(
                "SELECT c FROM Configuration c WHERE c.tenantId = :tenantId " +
                "AND c.applicationName = :appName AND c.profile = :profile " +
                "AND c.configKey = :configKey AND c.isActive = true", Configuration.class)
                .setParameter("tenantId", tenantId)
                .setParameter("appName", applicationName)
                .setParameter("profile", profile)
                .setParameter("configKey", configKey)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Configuration> findByTenantIdAndApplicationNameAndProfile(
            String tenantId, String applicationName, String profile) {
        return entityManager.createQuery(
                "SELECT c FROM Configuration c WHERE c.tenantId = :tenantId " +
                "AND c.applicationName = :appName AND c.profile = :profile " +
                "AND c.isActive = true ORDER BY c.configKey", Configuration.class)
                .setParameter("tenantId", tenantId)
                .setParameter("appName", applicationName)
                .setParameter("profile", profile)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Configuration> findByTenantId(String tenantId) {
        return entityManager.createQuery(
                "SELECT c FROM Configuration c WHERE c.tenantId = :tenantId " +
                "AND c.isActive = true ORDER BY c.applicationName, c.profile, c.configKey", Configuration.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Configuration> searchByTenantIdAndApplicationName(
            String tenantId, String applicationName, String profile, Boolean isActive) {
        StringBuilder jpql = new StringBuilder("SELECT c FROM Configuration c WHERE c.tenantId = :tenantId");

        if (applicationName != null && !applicationName.isEmpty()) {
            jpql.append(" AND c.applicationName = :appName");
        }
        if (profile != null && !profile.isEmpty()) {
            jpql.append(" AND c.profile = :profile");
        }
        if (isActive != null) {
            jpql.append(" AND c.isActive = :isActive");
        }
        jpql.append(" ORDER BY c.createdAt DESC");

        var query = entityManager.createQuery(jpql.toString(), Configuration.class)
                .setParameter("tenantId", tenantId);

        if (applicationName != null && !applicationName.isEmpty()) {
            query.setParameter("appName", applicationName);
        }
        if (profile != null && !profile.isEmpty()) {
            query.setParameter("profile", profile);
        }
        if (isActive != null) {
            query.setParameter("isActive", isActive);
        }

        return query.getResultList();
    }

    @Override
    public void delete(Configuration configuration) {
        if (entityManager.contains(configuration)) {
            entityManager.remove(configuration);
        } else {
            entityManager.remove(entityManager.merge(configuration));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean existsByTenantIdAndApplicationNameAndProfileAndConfigKey(
            String tenantId, String applicationName, String profile, String configKey) {
        Long count = (Long) entityManager.createQuery(
                "SELECT COUNT(c) FROM Configuration c WHERE c.tenantId = :tenantId " +
                "AND c.applicationName = :appName AND c.profile = :profile " +
                "AND c.configKey = :configKey")
                .setParameter("tenantId", tenantId)
                .setParameter("appName", applicationName)
                .setParameter("profile", profile)
                .setParameter("configKey", configKey)
                .getSingleResult();
        return count > 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Configuration> findAll(int page, int size) {
        return entityManager.createQuery(
                "SELECT c FROM Configuration c WHERE c.isActive = true ORDER BY c.createdAt DESC", Configuration.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }
}
