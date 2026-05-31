package com.gogidix.hr.globalcompliance.infrastructure.persistence.mongodb.mappers;

import com.gogidix.hr.globalcompliance.domain.model.AuditTrail;
import com.gogidix.hr.globalcompliance.infrastructure.persistence.mongodb.entities.AuditTrailEntity;

/**
 * Mapper for converting between AuditTrail domain model and AuditTrailEntity
 */
public class AuditTrailEntityMapper {

    public static AuditTrailEntity toEntity(AuditTrail domain) {
        if (domain == null) {
            return null;
        }
        return AuditTrailEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .auditId(domain.getAuditId())
                .requirementId(domain.getRequirementId())
                .checkId(domain.getCheckId())
                .issueId(domain.getIssueId())
                .reportId(domain.getReportId())
                .action(domain.getAction())
                .actionedBy(domain.getActionedBy())
                .actionedByName(domain.getActionedByName())
                .actionDate(domain.getActionDate())
                .actionTimestamp(domain.getActionTimestamp())
                .previousValue(domain.getPreviousValue())
                .newValue(domain.getNewValue())
                .reason(domain.getReason())
                .ipAddress(domain.getIpAddress())
                .userAgent(domain.getUserAgent())
                .entityType(domain.getEntityType())
                .entityId(domain.getEntityId())
                .countryCode(domain.getCountryCode())
                .department(domain.getDepartment())
                .changes(domain.getChanges())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    public static AuditTrail toDomain(AuditTrailEntity entity) {
        if (entity == null) {
            return null;
        }
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setId(entity.getId());
        auditTrail.setTenantId(entity.getTenantId());
        auditTrail.setAuditId(entity.getAuditId());
        auditTrail.setRequirementId(entity.getRequirementId());
        auditTrail.setCheckId(entity.getCheckId());
        auditTrail.setIssueId(entity.getIssueId());
        auditTrail.setReportId(entity.getReportId());
        auditTrail.setAction(entity.getAction());
        auditTrail.setActionedBy(entity.getActionedBy());
        auditTrail.setActionedByName(entity.getActionedByName());
        auditTrail.setActionDate(entity.getActionDate());
        auditTrail.setActionTimestamp(entity.getActionTimestamp());
        auditTrail.setPreviousValue(entity.getPreviousValue());
        auditTrail.setNewValue(entity.getNewValue());
        auditTrail.setReason(entity.getReason());
        auditTrail.setIpAddress(entity.getIpAddress());
        auditTrail.setUserAgent(entity.getUserAgent());
        auditTrail.setEntityType(entity.getEntityType());
        auditTrail.setEntityId(entity.getEntityId());
        auditTrail.setCountryCode(entity.getCountryCode());
        auditTrail.setDepartment(entity.getDepartment());
        auditTrail.setChanges(entity.getChanges());
        auditTrail.setCreatedAt(entity.getCreatedAt());
        auditTrail.setUpdatedAt(entity.getUpdatedAt());
        return auditTrail;
    }
}
