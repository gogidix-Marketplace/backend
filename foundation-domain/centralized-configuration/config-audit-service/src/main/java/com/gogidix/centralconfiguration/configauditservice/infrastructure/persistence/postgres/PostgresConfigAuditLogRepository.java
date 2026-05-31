package com.gogidix.centralconfiguration.configauditservice.infrastructure.persistence.postgres;

import com.gogidix.centralconfiguration.configauditservice.domain.model.AuditAction;
import com.gogidix.centralconfiguration.configauditservice.domain.model.ConfigAuditLog;
import com.gogidix.centralconfiguration.configauditservice.domain.repository.ConfigAuditLogRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * PostgreSQL implementation of ConfigAuditLogRepository.
 */
@Repository
public class PostgresConfigAuditLogRepository implements ConfigAuditLogRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ConfigAuditLog save(ConfigAuditLog auditLog) {
        if (auditLog.getId() == null) {
            entityManager.persist(auditLog);
            return auditLog;
        } else {
            return entityManager.merge(auditLog);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ConfigAuditLog> findByTenantId(String tenantId) {
        return entityManager.createQuery(
                "SELECT a FROM ConfigAuditLog a WHERE a.tenantId = :tenantId ORDER BY a.createdAt DESC", ConfigAuditLog.class)
                .setParameter("tenantId", tenantId)
                .setMaxResults(1000)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ConfigAuditLog> findByTenantIdAndEntityType(String tenantId, String entityType) {
        return entityManager.createQuery(
                "SELECT a FROM ConfigAuditLog a WHERE a.tenantId = :tenantId AND a.entityType = :entityType ORDER BY a.createdAt DESC", ConfigAuditLog.class)
                .setParameter("tenantId", tenantId)
                .setParameter("entityType", entityType)
                .setMaxResults(1000)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ConfigAuditLog> findByTenantIdAndEntityTypeAndEntityId(
            String tenantId, String entityType, String entityId) {
        return entityManager.createQuery(
                "SELECT a FROM ConfigAuditLog a WHERE a.tenantId = :tenantId AND a.entityType = :entityType AND a.entityId = :entityId ORDER BY a.createdAt DESC", ConfigAuditLog.class)
                .setParameter("tenantId", tenantId)
                .setParameter("entityType", entityType)
                .setParameter("entityId", entityId)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ConfigAuditLog> findByTenantIdAndAction(String tenantId, AuditAction action) {
        return entityManager.createQuery(
                "SELECT a FROM ConfigAuditLog a WHERE a.tenantId = :tenantId AND a.action = :action ORDER BY a.createdAt DESC", ConfigAuditLog.class)
                .setParameter("tenantId", tenantId)
                .setParameter("action", action)
                .setMaxResults(1000)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ConfigAuditLog> findByTenantIdAndDateRange(
            String tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return entityManager.createQuery(
                "SELECT a FROM ConfigAuditLog a WHERE a.tenantId = :tenantId AND a.createdAt BETWEEN :startDate AND :endDate ORDER BY a.createdAt DESC", ConfigAuditLog.class)
                .setParameter("tenantId", tenantId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ConfigAuditLog> findByChangedBy(String changedBy) {
        return entityManager.createQuery(
                "SELECT a FROM ConfigAuditLog a WHERE a.changedBy = :changedBy ORDER BY a.createdAt DESC", ConfigAuditLog.class)
                .setParameter("changedBy", changedBy)
                .setMaxResults(1000)
                .getResultList();
    }

    @Override
    public void delete(ConfigAuditLog auditLog) {
        if (entityManager.contains(auditLog)) {
            entityManager.remove(auditLog);
        } else {
            entityManager.remove(entityManager.merge(auditLog));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ConfigAuditLog> findAll(int page, int size) {
        return entityManager.createQuery(
                "SELECT a FROM ConfigAuditLog a ORDER BY a.createdAt DESC", ConfigAuditLog.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }
}
