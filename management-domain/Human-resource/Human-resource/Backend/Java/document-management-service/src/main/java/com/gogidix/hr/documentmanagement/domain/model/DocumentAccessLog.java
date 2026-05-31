package com.gogidix.hr.documentmanagement.domain.model;

import com.gogidix.hr.documentmanagement.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Document Access Log Domain Entity
 * Tracks all access to documents for audit purposes
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "document_access_logs")
public class DocumentAccessLog extends BaseEntity {

    @Indexed
    private String logId;

    @Indexed
    private String tenantId;

    @Indexed
    private String documentId;
    private String documentName;

    @Indexed
    private String accessedBy;
    private String accessedByName;

    @Indexed
    private AccessAction action;

    @Indexed
    private LocalDate accessDate;

    @Indexed
    private Instant accessTimestamp;

    private String ipAddress;
    private String userAgent;
    private String reason;

    @Indexed
    private String countryCode;

    private String department;
    private Boolean authorized;

    private String sessionId;
    private String correlationId;

    /**
     * Creates a new access log entry
     */
    public static DocumentAccessLog create(String tenantId, String documentId, String documentName,
                                          String accessedBy, String accessedByName, AccessAction action,
                                          String ipAddress, String userAgent, String reason,
                                          String countryCode, String department, Boolean authorized) {
        String logId = generateLogId(documentId, accessedBy);

        DocumentAccessLog log = new DocumentAccessLog();
        log.setLogId(logId);
        log.setTenantId(tenantId);
        log.setDocumentId(documentId);
        log.setDocumentName(documentName);
        log.setAccessedBy(accessedBy);
        log.setAccessedByName(accessedByName);
        log.setAction(action);
        log.setAccessDate(LocalDate.now());
        log.setAccessTimestamp(Instant.now());
        log.setIpAddress(ipAddress);
        log.setUserAgent(userAgent);
        log.setReason(reason);
        log.setCountryCode(countryCode);
        log.setDepartment(department);
        log.setAuthorized(authorized != null ? authorized : true);
        return log;
    }

    /**
     * Creates a log entry for document view
     */
    public static DocumentAccessLog viewLog(String tenantId, String documentId, String documentName,
                                           String accessedBy, String accessedByName, String ipAddress) {
        return create(tenantId, documentId, documentName, accessedBy, accessedByName,
                     AccessAction.VIEW, ipAddress, null, null, null, null, true);
    }

    /**
     * Creates a log entry for document download
     */
    public static DocumentAccessLog downloadLog(String tenantId, String documentId, String documentName,
                                               String accessedBy, String accessedByName, String ipAddress,
                                               String reason) {
        return create(tenantId, documentId, documentName, accessedBy, accessedByName,
                     AccessAction.DOWNLOAD, ipAddress, null, reason, null, null, true);
    }

    /**
     * Creates a log entry for document upload
     */
    public static DocumentAccessLog uploadLog(String tenantId, String documentId, String documentName,
                                             String accessedBy, String accessedByName, String ipAddress) {
        return create(tenantId, documentId, documentName, accessedBy, accessedByName,
                     AccessAction.UPLOAD, ipAddress, null, null, null, null, true);
    }

    /**
     * Creates a log entry for document update
     */
    public static DocumentAccessLog updateLog(String tenantId, String documentId, String documentName,
                                             String accessedBy, String accessedByName, String ipAddress,
                                             String reason) {
        return create(tenantId, documentId, documentName, accessedBy, accessedByName,
                     AccessAction.UPDATE, ipAddress, null, reason, null, null, true);
    }

    /**
     * Creates a log entry for document delete
     */
    public static DocumentAccessLog deleteLog(String tenantId, String documentId, String documentName,
                                             String accessedBy, String accessedByName, String ipAddress,
                                             String reason) {
        return create(tenantId, documentId, documentName, accessedBy, accessedByName,
                     AccessAction.DELETE, ipAddress, null, reason, null, null, true);
    }

    /**
     * Creates a log entry for document share
     */
    public static DocumentAccessLog shareLog(String tenantId, String documentId, String documentName,
                                            String accessedBy, String accessedByName, String ipAddress,
                                            String reason) {
        return create(tenantId, documentId, documentName, accessedBy, accessedByName,
                     AccessAction.SHARE, ipAddress, null, reason, null, null, true);
    }

    /**
     * Creates a log entry for unauthorized access attempt
     */
    public static DocumentAccessLog unauthorizedAccessLog(String tenantId, String documentId, String documentName,
                                                         String accessedBy, String accessedByName, String ipAddress,
                                                         AccessAction action, String reason) {
        return create(tenantId, documentId, documentName, accessedBy, accessedByName,
                     action, ipAddress, null, reason, null, null, false);
    }

    /**
     * Checks if access was authorized
     */
    public boolean isAuthorized() {
        return this.authorized != null && this.authorized;
    }

    /**
     * Checks if access was for sensitive action
     */
    public boolean isSensitiveAction() {
        return this.action == AccessAction.DELETE ||
               this.action == AccessAction.SHARE ||
               this.action == AccessAction.DOWNLOAD;
    }

    /**
     * Gets full description of the access
     */
    public String getAccessDescription() {
        if (this.accessedByName != null) {
            return String.format("%s %s document '%s' on %s",
                    this.accessedByName,
                    this.action != null ? this.action.name().toLowerCase() : "accessed",
                    this.documentName,
                    this.accessTimestamp != null ? this.accessTimestamp : this.accessDate);
        }
        return String.format("Document '%s' was %s on %s",
                this.documentName,
                this.action != null ? this.action.name().toLowerCase() : "accessed",
                this.accessTimestamp != null ? this.accessTimestamp : this.accessDate);
    }

    /**
     * Checks if access was within a date range
     */
    public boolean isAccessedBetween(LocalDate fromDate, LocalDate toDate) {
        if (this.accessDate == null) {
            return false;
        }
        boolean afterFrom = fromDate == null || !this.accessDate.isBefore(fromDate);
        boolean beforeTo = toDate == null || !this.accessDate.isAfter(toDate);
        return afterFrom && beforeTo;
    }

    /**
     * Checks if access was by specific user
     */
    public boolean isAccessedBy(String userId) {
        return this.accessedBy != null && this.accessedBy.equals(userId);
    }

    /**
     * Checks if access is of specific action type
     */
    public boolean isAction(AccessAction action) {
        return this.action == action;
    }

    private static String generateLogId(String documentId, String userId) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String docSuffix = documentId != null ? documentId.substring(Math.max(0, documentId.length() - 6)) : "000000";
        String userSuffix = userId != null ? userId.substring(Math.max(0, userId.length() - 4)) : "0000";
        return "LOG-" + docSuffix + "-" + userSuffix + "-" + timestamp.substring(timestamp.length() - 6);
    }
}
