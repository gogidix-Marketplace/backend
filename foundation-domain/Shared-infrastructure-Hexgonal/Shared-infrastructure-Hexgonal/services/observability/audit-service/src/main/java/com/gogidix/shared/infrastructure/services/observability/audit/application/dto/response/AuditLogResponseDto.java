package com.gogidix.shared.infrastructure.services.observability.audit.application.dto.response;

import com.gogidix.shared.infrastructure.services.observability.audit.domain.model.AuditLog;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Audit log response DTO.
 */
public record AuditLogResponseDto(
        String id,
        String tenantId,
        String userId,
        String username,
        AuditLog.AuditAction action,
        AuditLog.AuditEntityType entityType,
        String entityId,
        String entityName,
        String description,
        String ipAddress,
        String userAgent,
        boolean success,
        String errorMessage,
        Map<String, Object> changes,
        Map<String, Object> metadata,
        LocalDateTime timestamp
) {
    public static AuditLogResponseDtoBuilder builder() {
        return new AuditLogResponseDtoBuilder();
    }

    public static class AuditLogResponseDtoBuilder {
        private String id;
        private String tenantId;
        private String userId;
        private String username;
        private AuditLog.AuditAction action;
        private AuditLog.AuditEntityType entityType;
        private String entityId;
        private String entityName;
        private String description;
        private String ipAddress;
        private String userAgent;
        private boolean success;
        private String errorMessage;
        private Map<String, Object> changes;
        private Map<String, Object> metadata;
        private LocalDateTime timestamp;

        public AuditLogResponseDtoBuilder id(String id) {
            this.id = id;
            return this;
        }

        public AuditLogResponseDtoBuilder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public AuditLogResponseDtoBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public AuditLogResponseDtoBuilder username(String username) {
            this.username = username;
            return this;
        }

        public AuditLogResponseDtoBuilder action(AuditLog.AuditAction action) {
            this.action = action;
            return this;
        }

        public AuditLogResponseDtoBuilder entityType(AuditLog.AuditEntityType entityType) {
            this.entityType = entityType;
            return this;
        }

        public AuditLogResponseDtoBuilder entityId(String entityId) {
            this.entityId = entityId;
            return this;
        }

        public AuditLogResponseDtoBuilder entityName(String entityName) {
            this.entityName = entityName;
            return this;
        }

        public AuditLogResponseDtoBuilder description(String description) {
            this.description = description;
            return this;
        }

        public AuditLogResponseDtoBuilder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public AuditLogResponseDtoBuilder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public AuditLogResponseDtoBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        public AuditLogResponseDtoBuilder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public AuditLogResponseDtoBuilder changes(Map<String, Object> changes) {
            this.changes = changes;
            return this;
        }

        public AuditLogResponseDtoBuilder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        public AuditLogResponseDtoBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public AuditLogResponseDto build() {
            return new AuditLogResponseDto(
                id, tenantId, userId, username, action, entityType,
                entityId, entityName, description, ipAddress, userAgent,
                success, errorMessage, changes, metadata, timestamp
            );
        }
    }
}
