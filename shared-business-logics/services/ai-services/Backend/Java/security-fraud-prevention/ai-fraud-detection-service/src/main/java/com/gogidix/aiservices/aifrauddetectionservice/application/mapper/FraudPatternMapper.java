package com.gogidix.aiservices.aifrauddetectionservice.application.mapper;

import com.gogidix.aiservices.aifrauddetectionservice.application.dto.FraudPatternDTO;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudPattern;
import com.gogidix.aiservices.aifrauddetectionservice.infrastructure.persistence.mongodb.FraudPatternEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper for FraudPattern related entities and DTOs.
 * Handles conversion between domain model, entity, and DTO.
 */
@Component
public class FraudPatternMapper {

    /**
     * Converts domain model to DTO.
     */
    public FraudPatternDTO toDto(FraudPattern domain) {
        if (domain == null) {
            return null;
        }

        return FraudPatternDTO.builder()
                .id(domain.getPatternId())
                .patternId(domain.getPatternId())
                .patternName(domain.getPatternName())
                .description(domain.getDescription())
                .tenantId(domain.getTenantId())
                .confidenceScore(domain.getConfidenceScore())
                .lastSeen(domain.getLastSeen())
                .occurrenceCount(domain.getOccurrenceCount())
                .build();
    }

    /**
     * Converts DTO to domain model.
     */
    public FraudPattern toEntity(FraudPatternDTO dto) {
        if (dto == null) {
            return null;
        }

        return FraudPattern.builder()
                .patternId(dto.getPatternId())
                .patternName(dto.getPatternName())
                .description(dto.getDescription())
                .tenantId(dto.getTenantId())
                .confidenceScore(dto.getConfidenceScore() != null ? dto.getConfidenceScore() : 0.0)
                .lastSeen(dto.getLastSeen())
                .occurrenceCount(dto.getOccurrenceCount() != null ? dto.getOccurrenceCount() : 0)
                .build();
    }

    /**
     * Converts entity to DTO.
     */
    public FraudPatternDTO toDto(FraudPatternEntity entity) {
        if (entity == null) {
            return null;
        }

        return FraudPatternDTO.builder()
                .id(entity.getId())
                .patternId(entity.getId())
                .patternName(entity.getPatternName())
                .patternType("FRAUD_PATTERN") // Default value as entity doesn't have this field
                .description(entity.getDescription())
                .tenantId(entity.getTenantId())
                .confidenceScore(entity.getConfidenceScore())
                .threshold(null) // Entity doesn't have this field
                .lastSeen(entity.getLastSeen())
                .occurrenceCount(entity.getOccurrenceCount())
                .isActive(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Converts entity to domain model.
     */
    public FraudPattern toDomain(FraudPatternEntity entity) {
        if (entity == null) {
            return null;
        }

        return FraudPattern.builder()
                .patternId(entity.getId())
                .patternName(entity.getPatternName())
                .description(entity.getDescription())
                .tenantId(entity.getTenantId())
                .confidenceScore(entity.getConfidenceScore())
                .lastSeen(entity.getLastSeen())
                .occurrenceCount(entity.getOccurrenceCount())
                .build();
    }

    /**
     * Converts domain model to entity.
     */
    public FraudPatternEntity toEntity(FraudPattern domain) {
        if (domain == null) {
            return null;
        }

        FraudPatternEntity entity = new FraudPatternEntity();
        entity.setId(domain.getPatternId());
        entity.setPatternName(domain.getPatternName());
        entity.setDescription(domain.getDescription());
        entity.setTenantId(domain.getTenantId());
        entity.setConfidenceScore(domain.getConfidenceScore());
        entity.setLastSeen(domain.getLastSeen());
        entity.setOccurrenceCount(domain.getOccurrenceCount());

        return entity;
    }

    /**
     * Converts list of domain models to DTOs.
     */
    public List<FraudPatternDTO> toDtoList(List<FraudPattern> domains) {
        if (domains == null) {
            return List.of();
        }
        return domains.stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Converts list of entities to DTOs.
     */
    public List<FraudPatternDTO> toDtoListFromEntities(List<FraudPatternEntity> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Converts AddFraudPatternCommand to domain model.
     */
    public FraudPattern fromCommand(com.gogidix.aiservices.aifrauddetectionservice.application.command.AddFraudPatternCommand command) {
        if (command == null) {
            return null;
        }

        return FraudPattern.builder()
                .patternName(command.getPatternType())
                .description(command.getDescription())
                .confidenceScore(command.getConfidenceScore())
                .occurrenceCount(0)
                .build();
    }
}
