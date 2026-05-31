package com.gogidix.shared.infrastructure.services.observability.audit.domain.port.in;

import com.gogidix.shared.infrastructure.services.observability.audit.application.dto.request.CreateAuditLogRequestDto;
import com.gogidix.shared.infrastructure.services.observability.audit.application.dto.response.AuditLogResponseDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Audit use case port.
 */
public interface AuditPort {

    /**
     * Creates a new audit log entry.
     */
    AuditLogResponseDto createAuditLog(CreateAuditLogRequestDto request);

    /**
     * Gets audit log by ID.
     */
    AuditLogResponseDto getAuditLog(String auditLogId);

    /**
     * Lists audit logs for current tenant with filters.
     */
    List<AuditLogResponseDto> listAuditLogs(String userId, LocalDateTime startDate, LocalDateTime endDate, Integer limit);

    /**
     * Lists audit logs for a specific entity.
     */
    List<AuditLogResponseDto> listEntityAuditLogs(String entityType, String entityId);
}
