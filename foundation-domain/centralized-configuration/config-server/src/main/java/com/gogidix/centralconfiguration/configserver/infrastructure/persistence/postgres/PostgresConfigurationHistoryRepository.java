package com.gogidix.centralconfiguration.configserver.infrastructure.persistence.postgres;

import com.gogidix.centralconfiguration.configserver.domain.model.ConfigurationHistory;
import com.gogidix.centralconfiguration.configserver.domain.repository.ConfigurationHistoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PostgreSQL implementation of ConfigurationHistoryRepository.
 * Handles persistence operations for ConfigurationHistory entities.
 */
@Repository
public class PostgresConfigurationHistoryRepository implements ConfigurationHistoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ConfigurationHistory save(ConfigurationHistory history) {
        if (history.getId() == null) {
            entityManager.persist(history);
            return history;
        } else {
            return entityManager.merge(history);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ConfigurationHistory> findByConfigurationId(Long configurationId) {
        return entityManager.createQuery(
                "SELECT h FROM ConfigurationHistory h WHERE h.configurationId = :configId " +
                "ORDER BY h.createdAt DESC", ConfigurationHistory.class)
                .setParameter("configId", configurationId)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ConfigurationHistory> findByTenantIdAndApplicationName(
            String tenantId, String applicationName, int page, int size) {
        return entityManager.createQuery(
                "SELECT h FROM ConfigurationHistory h WHERE h.tenantId = :tenantId " +
                "AND h.applicationName = :appName ORDER BY h.createdAt DESC", ConfigurationHistory.class)
                .setParameter("tenantId", tenantId)
                .setParameter("appName", applicationName)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ConfigurationHistory> findByTenantId(String tenantId, int page, int size) {
        return entityManager.createQuery(
                "SELECT h FROM ConfigurationHistory h WHERE h.tenantId = :tenantId " +
                "ORDER BY h.createdAt DESC", ConfigurationHistory.class)
                .setParameter("tenantId", tenantId)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }
}
