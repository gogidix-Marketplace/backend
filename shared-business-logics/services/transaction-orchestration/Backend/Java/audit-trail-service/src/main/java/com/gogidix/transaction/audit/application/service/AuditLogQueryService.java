package com.gogidix.transaction.audit.application.service;

import com.gogidix.transaction.audit.application.dto.response.AuditLogResponseDto;
import com.gogidix.transaction.audit.application.dto.response.PagedAuditLogsResponseDto;
import com.gogidix.transaction.audit.application.mapper.AuditLogMapper;
import com.gogidix.transaction.audit.domain.model.AuditLog;
import com.gogidix.transaction.audit.domain.port.in.GetAuditLogQuery;
import com.gogidix.transaction.audit.domain.port.in.SearchAuditLogsQuery;
import com.gogidix.transaction.audit.domain.port.out.AuditLogRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Query service for audit log operations.
 * Handles read operations for audit logs.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogQueryService {

    private final AuditLogRepositoryPort repository;
    private final AuditLogMapper mapper;

    /**
     * Get audit log by ID
     */
    public AuditLogResponseDto getAuditLog(GetAuditLogQuery query) {
        log.debug("Getting audit log: id={}", query.getAuditLogId());

        AuditLog auditLog = repository.findById(query.getAuditLogId())
            .orElseThrow(() -> new IllegalArgumentException("Audit log not found: " + query.getAuditLogId()));

        return mapper.toResponseDto(auditLog);
    }

    /**
     * Search audit logs with filters
     */
    public PagedAuditLogsResponseDto searchAuditLogs(SearchAuditLogsQuery query) {
        log.debug("Searching audit logs: tenantId={}, entityType={}, action={}",
            query.getTenantId(), query.getEntityType(), query.getAction());

        List<AuditLog> auditLogs = repository.searchAuditLogs(
            query.getTenantId(),
            query.getEntityType(),
            query.getEntityId(),
            query.getAction(),
            query.getActorId(),
            query.getSeverity(),
            query.getCategory(),
            query.getStatus(),
            query.getStartDate(),
            query.getEndDate()
        );

        // Apply pagination
        int start = query.getPage() * query.getSize();
        int end = Math.min(start + query.getSize(), auditLogs.size());
        List<AuditLog> pagedLogs = start < auditLogs.size() ? auditLogs.subList(start, end) : List.of();

        List<AuditLogResponseDto> responseDtos = mapper.toResponseDtoList(pagedLogs);

        return PagedAuditLogsResponseDto.builder()
            .auditLogs(responseDtos)
            .page(query.getPage())
            .size(query.getSize())
            .totalElements((long) auditLogs.size())
            .totalPages((int) Math.ceil((double) auditLogs.size() / query.getSize()))
            .hasNext(end < auditLogs.size())
            .hasPrevious(query.getPage() > 0)
            .build();
    }

    /**
     * Get audit logs by entity
     */
    public List<AuditLogResponseDto> getAuditLogsByEntity(String entityType, String entityId) {
        log.debug("Getting audit logs for entity: type={}, id={}", entityType, entityId);

        List<AuditLog> auditLogs = repository.findByEntityTypeAndEntityId(entityType, entityId);
        return mapper.toResponseDtoList(auditLogs);
    }

    /**
     * Get audit logs by tenant and entity
     */
    public List<AuditLogResponseDto> getAuditLogsByTenantAndEntity(String tenantId, String entityType, String entityId) {
        log.debug("Getting audit logs for tenant entity: tenant={}, type={}, id={}", tenantId, entityType, entityId);

        List<AuditLog> auditLogs = repository.findByTenantIdAndEntityTypeAndEntityId(tenantId, entityType, entityId);
        return mapper.toResponseDtoList(auditLogs);
    }

    /**
     * Get audit logs by correlation ID
     */
    public List<AuditLogResponseDto> getAuditLogsByCorrelationId(String correlationId) {
        log.debug("Getting audit logs by correlation ID: {}", correlationId);

        List<AuditLog> auditLogs = repository.findByCorrelationId(correlationId);
        return mapper.toResponseDtoList(auditLogs);
    }

    /**
     * Get audit logs by actor
     */
    public List<AuditLogResponseDto> getAuditLogsByActor(String actorId) {
        log.debug("Getting audit logs by actor: {}", actorId);

        List<AuditLog> auditLogs = repository.findByActorId(actorId);
        return mapper.toResponseDtoList(auditLogs);
    }

    /**
     * Count audit logs by tenant
     */
    public Long countByTenant(String tenantId) {
        return repository.countByTenantId(tenantId);
    }
}
