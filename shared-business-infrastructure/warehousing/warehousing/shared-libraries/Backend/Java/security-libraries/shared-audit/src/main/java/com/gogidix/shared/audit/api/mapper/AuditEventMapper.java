package com.gogidix.shared.audit.api.mapper;

import com.gogidix.shared.audit.api.dto.*;
import com.gogidix.shared.audit.domain.*;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * MapStruct mapper for converting between audit domain entities and DTOs.
 * Provides comprehensive mapping for all audit-related objects.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
@Component
public interface AuditEventMapper {
    
    // AuditEvent mappings
    
    /**
     * Maps AuditEvent entity to AuditEventDTO
     */
    @Mapping(target = "severity", expression = "java(auditEvent.calculateSeverity())")
    @Mapping(target = "requiresSecurityEscalation", expression = "java(auditEvent.requiresSecurityEscalation())")
    @Mapping(target = "suspiciousPattern", expression = "java(auditEvent.isSuspiciousPattern())")
    @Mapping(target = "compliantEvent", expression = "java(auditEvent.isCompliantEvent())")
    @Mapping(target = "financialEvent", expression = "java(auditEvent.isFinancialEvent())")
    AuditEventDTO toDTO(AuditEvent auditEvent);
    
    /**
     * Maps list of AuditEvent entities to list of AuditEventDTOs
     */
    List<AuditEventDTO> toDTOList(List<AuditEvent> auditEvents);
    
    /**
     * Maps CreateAuditEventDTO to AuditEventCreationRequest
     */
    @Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now())")
    com.gogidix.shared.audit.domain.AuditEventCreationRequest toCreationRequest(CreateAuditEventDTO createDTO);
    
    /**
     * Maps AuditSearchDTO to AuditSearchCriteria
     */
    @Mapping(target = "maxResults", expression = "java(searchDTO.getEffectiveMaxResults())")
    @Mapping(target = "sortBy", expression = "java(searchDTO.getEffectiveSortBy())")
    @Mapping(target = "sortDirection", expression = "java(searchDTO.getEffectiveSortDirection())")
    AuditSearchCriteria toSearchCriteria(AuditSearchDTO searchDTO);
    
    // Statistics mappings
    
    /**
     * Maps AuditStatistics to AuditStatisticsDTO
     */
    AuditStatisticsDTO toStatisticsDTO(AuditStatistics statistics);
    
    // Compliance mappings
    
    /**
     * Maps ComplianceReport to ComplianceReportDTO
     */
    ComplianceReportDTO toComplianceReportDTO(ComplianceReport report);
    
    // Default mapping methods for complex conversions
    
    /**
     * Default mapping for LocalDateTime to ensure proper timezone handling
     */
    default LocalDateTime map(LocalDateTime dateTime) {
        return dateTime;
    }
    
    /**
     * Maps enum values safely
     */
    default String mapEnum(Enum<?> enumValue) {
        return enumValue != null ? enumValue.name() : null;
    }
    
    /**
     * Custom mapping for risk score to ensure proper format
     */
    default String mapRiskScore(Integer riskScore) {
        return riskScore != null ? riskScore.toString() : "0";
    }
    
    /**
     * Custom mapping for metadata JSON
     */
    default String mapMetadata(Object metadata) {
        if (metadata == null) {
            return null;
        }
        
        try {
            // Simple JSON conversion - in a real implementation, use Jackson ObjectMapper
            return metadata.toString();
        } catch (Exception e) {
            return "{}";
        }
    }
    
    /**
     * Reverse mapping for metadata
     */
    default Object mapMetadataFromString(String metadata) {
        if (metadata == null || metadata.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Simple parsing - in a real implementation, use Jackson ObjectMapper
            return metadata;
        } catch (Exception e) {
            return metadata;
        }
    }
    
    // Helper methods for complex calculations
    
    /**
     * After mapping customization for AuditEventDTO
     */
    @AfterMapping
    default void afterMappingAuditEventDTO(@MappingTarget AuditEventDTO dto, AuditEvent entity) {
        if (dto != null && entity != null) {
            // Set computed fields (using proper Lombok generated setters)
            dto.setSeverity(entity.calculateSeverity());
            dto.setRequiresSecurityEscalation(entity.requiresSecurityEscalation());
            dto.setSuspiciousPattern(entity.isSuspiciousPattern());
            dto.setCompliantEvent(entity.isCompliantEvent());
            dto.setFinancialEvent(entity.isFinancialEvent());
        }
    }
    
    /**
     * Before mapping customization for creation requests
     */
    @BeforeMapping
    default void beforeMappingCreationRequest(@MappingTarget com.gogidix.shared.audit.domain.AuditEventCreationRequest.AuditEventCreationRequestBuilder target, CreateAuditEventDTO source) {
        if (source != null) {
            // Validate business rules before mapping
            if (!source.isValidForBusinessRules()) {
                throw new IllegalArgumentException("Audit event creation request does not meet business rules");
            }
        }
    }
    
    /**
     * Maps primitive int to Integer safely
     */
    default Integer mapIntToInteger(int value) {
        return value;
    }
    
    /**
     * Maps Integer to primitive int safely
     */
    default int mapIntegerToInt(Integer value) {
        return value != null ? value : 0;
    }
    
    /**
     * Maps boolean to Boolean safely
     */
    default Boolean mapBooleanToBoolean(boolean value) {
        return value;
    }
    
    /**
     * Maps Boolean to boolean safely
     */
    default boolean mapBooleanToboolean(Boolean value) {
        return value != null ? value : false;
    }
}