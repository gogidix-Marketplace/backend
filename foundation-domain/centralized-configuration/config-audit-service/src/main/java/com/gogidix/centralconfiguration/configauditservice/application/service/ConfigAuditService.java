package com.gogidix.centralconfiguration.configauditservice.application.service;

import com.gogidix.centralconfiguration.configauditservice.domain.model.AuditAction;
import com.gogidix.centralconfiguration.configauditservice.domain.model.ConfigAuditLog;
import com.gogidix.centralconfiguration.configauditservice.domain.repository.ConfigAuditLogRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for Config Audit Log operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigAuditService {

    private final ConfigAuditLogRepository auditLogRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static final String DEFAULT_TENANT_ID = "default";

    /**
     * Create an audit log entry
     */
    @Transactional
    public ConfigAuditLog createAuditLog(String tenantId, String entityType, String entityId,
                                         AuditAction action, String oldValue, String newValue,
                                         String changedBy, String userName, String userEmail,
                                         String ipAddress, String userAgent, String changeReason,
                                         String metadata) {
        log.debug("Creating audit log: tenantId={}, type={}, action={}", tenantId, entityType, action);

        String effectiveTenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        ConfigAuditLog auditLog = ConfigAuditLog.builder()
                .tenantId(effectiveTenantId)
                .entityType(entityType)
                .entityId(entityId)
                .action(action)
                .oldValue(oldValue)
                .newValue(newValue)
                .changedBy(changedBy)
                .userName(userName)
                .userEmail(userEmail)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .changeReason(changeReason)
                .metadata(metadata)
                .build();

        return auditLogRepository.save(auditLog);
    }

    /**
     * Get all audit logs for tenant
     */
    public List<ConfigAuditLog> getAuditLogs(String tenantId) {
        String effectiveTenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        return auditLogRepository.findByTenantId(effectiveTenantId);
    }

    /**
     * Get audit logs by entity type
     */
    public List<ConfigAuditLog> getAuditLogsByEntityType(String tenantId, String entityType) {
        String effectiveTenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        return auditLogRepository.findByTenantIdAndEntityType(effectiveTenantId, entityType);
    }

    /**
     * Get audit logs by entity
     */
    public List<ConfigAuditLog> getAuditLogsByEntity(String tenantId, String entityType, String entityId) {
        String effectiveTenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        return auditLogRepository.findByTenantIdAndEntityTypeAndEntityId(
                effectiveTenantId, entityType, entityId);
    }

    /**
     * Get audit logs by date range
     */
    public List<ConfigAuditLog> getAuditLogsByDateRange(String tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        String effectiveTenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        return auditLogRepository.findByTenantIdAndDateRange(effectiveTenantId, startDate, endDate);
    }

    /**
     * Get audit logs by user
     */
    public List<ConfigAuditLog> getAuditLogsByUser(String changedBy) {
        return auditLogRepository.findByChangedBy(changedBy);
    }
}
