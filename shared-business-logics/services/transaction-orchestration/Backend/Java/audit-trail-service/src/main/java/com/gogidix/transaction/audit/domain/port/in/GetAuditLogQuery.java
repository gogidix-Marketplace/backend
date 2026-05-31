package com.gogidix.transaction.audit.domain.port.in;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * Query interface for getting a specific audit log entry.
 */
public interface GetAuditLogQuery {

    String getAuditLogId();

    @Builder
    @Data
    class GetAuditLogQueryDto implements GetAuditLogQuery {
        @NotBlank(message = "Audit log ID is required")
        private String auditLogId;
    }
}
