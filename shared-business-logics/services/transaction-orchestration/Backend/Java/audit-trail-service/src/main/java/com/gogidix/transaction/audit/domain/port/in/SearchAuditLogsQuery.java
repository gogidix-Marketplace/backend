package com.gogidix.transaction.audit.domain.port.in;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Query interface for searching audit logs.
 */
public interface SearchAuditLogsQuery {

    String getTenantId();

    String getEntityType();

    String getEntityId();

    String getAction();

    String getActorId();

    String getActorType();

    String getCorrelationId();

    String getSeverity();

    String getCategory();

    String getStatus();

    LocalDateTime getStartDate();

    LocalDateTime getEndDate();

    List<String> getActions();

    Integer getPage();

    Integer getSize();

    String getSortBy();

    String getSortDirection();

    @Builder
    @Data
    class SearchAuditLogsQueryDto implements SearchAuditLogsQuery {
        private String tenantId;

        private String entityType;

        private String entityId;

        private String action;

        private String actorId;

        private String actorType;

        private String correlationId;

        private String severity;

        private String category;

        private String status;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        private List<String> actions;

        @Builder.Default
        private Integer page = 0;

        @Builder.Default
        private Integer size = 20;

        @Builder.Default
        private String sortBy = "timestamp";

        @Builder.Default
        private String sortDirection = "DESC";
    }
}
