package com.gogidix.transaction.audit.application.service;

import com.gogidix.transaction.audit.application.dto.request.CreateAuditLogRequestDto;
import com.gogidix.transaction.audit.application.dto.response.AuditLogResponseDto;
import com.gogidix.transaction.audit.application.mapper.AuditLogMapper;
import com.gogidix.transaction.audit.domain.model.AuditLog;
import com.gogidix.transaction.audit.domain.port.in.CreateAuditLogCommand;
import com.gogidix.transaction.audit.domain.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Command service for audit log operations.
 * Handles write operations for audit logs.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogCommandService {

    private final AuditLogRepository repository;
    private final AuditLogMapper mapper;

    /**
     * Create a new audit log entry
     */
    @Transactional
    public AuditLogResponseDto createAuditLog(CreateAuditLogRequestDto request) {
        log.info("Creating audit log: entityType={}, entityId={}, action={}",
            request.getEntityType(), request.getEntityId(), request.getAction());

        CreateAuditLogCommand.CreateAuditLogCommandDto command = mapper.toCommand(request);
        AuditLog auditLog = mapper.toEntity(command);

        // Set defaults if not provided
        if (auditLog.getSeverity() == null) {
            auditLog.setSeverity("INFO");
        }
        if (auditLog.getStatus() == null) {
            auditLog.setStatus("SUCCESS");
        }
        if (auditLog.getTimestamp() == null) {
            auditLog.setTimestamp(java.time.LocalDateTime.now());
        }

        AuditLog saved = repository.save(auditLog);
        log.info("Created audit log: id={}", saved.getId());

        return mapper.toResponseDto(saved);
    }

    /**
     * Delete an audit log entry
     */
    @Transactional
    public void deleteAuditLog(UUID auditLogId) {
        log.info("Deleting audit log: id={}", auditLogId);

        AuditLog auditLog = repository.findById(auditLogId)
            .orElseThrow(() -> new IllegalArgumentException("Audit log not found: " + auditLogId));

        repository.delete(auditLog);
        log.info("Deleted audit log: id={}", auditLogId);
    }
}
