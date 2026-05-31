package com.gogidix.transaction.audit.application.mapper;

import com.gogidix.transaction.audit.application.dto.request.CreateAuditLogRequestDto;
import com.gogidix.transaction.audit.application.dto.response.AuditLogResponseDto;
import com.gogidix.transaction.audit.domain.model.AuditLog;
import com.gogidix.transaction.audit.domain.port.in.CreateAuditLogCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Mapper for AuditLog entity and DTOs.
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AuditLogMapper {

    /**
     * Convert CreateAuditLogRequestDto to CreateAuditLogCommand
     */
    CreateAuditLogCommand.CreateAuditLogCommandDto toCommand(CreateAuditLogRequestDto request);

    /**
     * Convert AuditLog entity to AuditLogResponseDto
     */
    @InheritConfiguration
    AuditLogResponseDto toResponseDto(AuditLog auditLog);

    /**
     * Convert list of AuditLog entities to AuditLogResponseDto list
     */
    List<AuditLogResponseDto> toResponseDtoList(List<AuditLog> auditLogs);

    /**
     * Create AuditLog entity from CreateAuditLogCommand
     * BaseEntity fields (createdAt, updatedAt, etc.) are managed by JPA lifecycle callbacks
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", source = "tenantId")
    @Mapping(target = "entityType", source = "entityType")
    @Mapping(target = "entityId", source = "entityId")
    @Mapping(target = "action", source = "action")
    @Mapping(target = "actorId", source = "actorId")
    @Mapping(target = "actorType", source = "actorType")
    @Mapping(target = "ipAddress", source = "ipAddress")
    @Mapping(target = "userAgent", source = "userAgent")
    @Mapping(target = "correlationId", source = "correlationId")
    @Mapping(target = "oldState", source = "oldState")
    @Mapping(target = "newState", source = "newState")
    @Mapping(target = "changedFields", source = "changedFields")
    @Mapping(target = "businessContext", source = "businessContext")
    @Mapping(target = "severity", source = "severity")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "errorMessage", source = "errorMessage")
    @Mapping(target = "sessionId", source = "sessionId")
    @Mapping(target = "requestId", source = "requestId")
    @Mapping(target = "timestamp", ignore = true)  // Set in service
    @Mapping(target = "createdAt", ignore = true)  // Managed by JPA @PrePersist
    @Mapping(target = "updatedAt", ignore = true)  // Managed by JPA @PreUpdate
    @Mapping(target = "createdBy", ignore = true)  // Managed by JPA auditing
    @Mapping(target = "updatedBy", ignore = true)  // Managed by JPA auditing
    @Mapping(target = "version", ignore = true)    // Managed by JPA @Version
    @Mapping(target = "isActive", ignore = true)   // Has default value
    @Mapping(target = "isDeleted", ignore = true)  // Has default value
    AuditLog toEntity(CreateAuditLogCommand.CreateAuditLogCommandDto command);
}
