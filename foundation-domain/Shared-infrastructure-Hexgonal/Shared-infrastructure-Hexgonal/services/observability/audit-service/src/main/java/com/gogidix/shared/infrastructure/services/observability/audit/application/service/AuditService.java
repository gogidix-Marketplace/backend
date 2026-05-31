package com.gogidix.shared.infrastructure.services.observability.audit.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.observability.audit.application.dto.request.CreateAuditLogRequestDto;
import com.gogidix.shared.infrastructure.services.observability.audit.application.dto.response.AuditLogResponseDto;
import com.gogidix.shared.infrastructure.services.observability.audit.domain.model.AuditLog;
import com.gogidix.shared.infrastructure.services.observability.audit.domain.port.in.AuditPort;
import com.gogidix.shared.infrastructure.services.observability.audit.domain.port.out.AuditLogRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Audit Service implementation.
 */
@Slf4j
@Service
public class AuditService implements AuditPort {

    private final AuditLogRepositoryPort auditLogRepository;
    private final TenantContextHolder tenantContextHolder;

    public AuditService(AuditLogRepositoryPort auditLogRepository, TenantContextHolder tenantContextHolder) {
        this.auditLogRepository = auditLogRepository;
        this.tenantContextHolder = tenantContextHolder;
    }

    @Override
    public AuditLogResponseDto createAuditLog(CreateAuditLogRequestDto request) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        AuditLog auditLog = AuditLog.builder()
                .tenantId(TenantId.of(tenantId))
                .userId(request.userId())
                .username(request.username())
                .action(request.action())
                .entityType(request.entityType())
                .entityId(request.entityId())
                .entityName(request.entityName())
                .description(request.description())
                .ipAddress(request.ipAddress())
                .userAgent(request.userAgent())
                .success(request.success())
                .errorMessage(request.errorMessage())
                .changes(request.changes())
                .metadata(request.metadata())
                .timestamp(LocalDateTime.now())
                .build();

        AuditLog saved = auditLogRepository.save(auditLog);

        log.debug("Audit log created: id={}, action={}, entity={}",
                saved.getId(), saved.getAction(), saved.getEntityType());

        return toResponseDto(saved);
    }

    @Override
    public AuditLogResponseDto getAuditLog(String auditLogId) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        return auditLogRepository.findByIdAndTenantId(auditLogId, tenantId)
                .map(this::toResponseDto)
                .orElse(null);
    }

    @Override
    public List<AuditLogResponseDto> listAuditLogs(String userId, LocalDateTime startDate, LocalDateTime endDate, Integer limit) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        int actualLimit = limit != null && limit > 0 ? limit : 100;

        return auditLogRepository.findByTenantIdWithFilters(tenantId, userId, startDate, endDate, actualLimit).stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public List<AuditLogResponseDto> listEntityAuditLogs(String entityType, String entityId) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        return auditLogRepository.findByTenantIdAndEntity(tenantId, entityType, entityId).stream()
                .map(this::toResponseDto)
                .toList();
    }

    private AuditLogResponseDto toResponseDto(AuditLog auditLog) {
        return AuditLogResponseDto.builder()
                .id(auditLog.getId())
                .tenantId(auditLog.getTenantId() != null ? auditLog.getTenantId().getValue() : null)
                .userId(auditLog.getUserId())
                .username(auditLog.getUsername())
                .action(auditLog.getAction())
                .entityType(auditLog.getEntityType())
                .entityId(auditLog.getEntityId())
                .entityName(auditLog.getEntityName())
                .description(auditLog.getDescription())
                .ipAddress(auditLog.getIpAddress())
                .userAgent(auditLog.getUserAgent())
                .success(auditLog.isSuccess())
                .errorMessage(auditLog.getErrorMessage())
                .changes(auditLog.getChanges())
                .metadata(auditLog.getMetadata())
                .timestamp(auditLog.getTimestamp())
                .build();
    }
}
