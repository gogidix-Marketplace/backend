package com.gogidix.aiservices.aifrauddetectionservice.application.mapper;

import com.gogidix.aiservices.aifrauddetectionservice.application.dto.FraudAnalysisDTO;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAnalysisResult;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAction;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.RiskLevel;
import com.gogidix.aiservices.aifrauddetectionservice.infrastructure.persistence.mongodb.FraudAnalysisResultEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper for FraudAnalysis related entities and DTOs.
 * Handles conversion between domain model, entity, and DTO.
 */
@Component
public class FraudAnalysisMapper {

    /**
     * Converts domain model to DTO.
     */
    public FraudAnalysisDTO toDto(FraudAnalysisResult domain) {
        if (domain == null) {
            return null;
        }

        return FraudAnalysisDTO.builder()
                .id(domain.getAnalysisId())
                .analysisId(domain.getAnalysisId())
                .transactionId(domain.getTransactionId())
                .userId(domain.getUserId())
                .tenantId(domain.getTenantId())
                .fraudScore(domain.getFraudScore())
                .riskLevel(domain.getRiskLevel())
                .recommendedAction(domain.getRecommendedAction())
                .reasons(domain.getReasons())
                .timestamp(domain.getTimestamp())
                .modelVersion(domain.getModelVersion())
                .build();
    }

    /**
     * Converts DTO to domain model.
     */
    public FraudAnalysisResult toEntity(FraudAnalysisDTO dto) {
        if (dto == null) {
            return null;
        }

        return FraudAnalysisResult.builder()
                .analysisId(dto.getAnalysisId())
                .transactionId(dto.getTransactionId())
                .userId(dto.getUserId())
                .tenantId(dto.getTenantId())
                .fraudScore(dto.getFraudScore())
                .riskLevel(dto.getRiskLevel())
                .recommendedAction(dto.getRecommendedAction())
                .reasons(dto.getReasons())
                .timestamp(dto.getTimestamp())
                .modelVersion(dto.getModelVersion())
                .build();
    }

    /**
     * Converts entity to DTO.
     */
    public FraudAnalysisDTO toDto(FraudAnalysisResultEntity entity) {
        if (entity == null) {
            return null;
        }

        return FraudAnalysisDTO.builder()
                .id(entity.getId())
                .analysisId(entity.getAnalysisId())
                .transactionId(entity.getTransactionId())
                .userId(entity.getUserId())
                .tenantId(entity.getTenantId())
                .fraudScore(entity.getFraudScore())
                .riskLevel(entity.getRiskLevel() != null ? RiskLevel.valueOf(entity.getRiskLevel()) : null)
                .recommendedAction(entity.getRecommendedAction() != null ? FraudAction.valueOf(entity.getRecommendedAction()) : null)
                .reasons(entity.getReasons())
                .timestamp(entity.getTimestamp())
                .modelVersion(entity.getModelVersion())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Converts entity to domain model.
     */
    public FraudAnalysisResult toDomain(FraudAnalysisResultEntity entity) {
        if (entity == null) {
            return null;
        }

        return FraudAnalysisResult.builder()
                .analysisId(entity.getAnalysisId())
                .transactionId(entity.getTransactionId())
                .userId(entity.getUserId())
                .tenantId(entity.getTenantId())
                .fraudScore(entity.getFraudScore())
                .riskLevel(entity.getRiskLevel() != null ? RiskLevel.valueOf(entity.getRiskLevel()) : null)
                .recommendedAction(entity.getRecommendedAction() != null ? FraudAction.valueOf(entity.getRecommendedAction()) : null)
                .reasons(entity.getReasons())
                .timestamp(entity.getTimestamp())
                .modelVersion(entity.getModelVersion())
                .build();
    }

    /**
     * Converts domain model to entity.
     */
    public FraudAnalysisResultEntity toEntity(FraudAnalysisResult domain) {
        if (domain == null) {
            return null;
        }

        FraudAnalysisResultEntity entity = new FraudAnalysisResultEntity();
        entity.setAnalysisId(domain.getAnalysisId());
        entity.setTransactionId(domain.getTransactionId());
        entity.setUserId(domain.getUserId());
        entity.setTenantId(domain.getTenantId());
        entity.setFraudScore(domain.getFraudScore());
        entity.setRiskLevel(domain.getRiskLevel() != null ? domain.getRiskLevel().name() : null);
        entity.setRecommendedAction(domain.getRecommendedAction() != null ? domain.getRecommendedAction().name() : null);
        entity.setReasons(domain.getReasons());
        entity.setTimestamp(domain.getTimestamp());
        entity.setModelVersion(domain.getModelVersion());

        return entity;
    }

    /**
     * Converts list of domain models to DTOs.
     */
    public List<FraudAnalysisDTO> toDtoList(List<FraudAnalysisResult> domains) {
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
    public List<FraudAnalysisDTO> toDtoListFromEntities(List<FraudAnalysisResultEntity> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Converts CreateFraudAnalysisCommand to domain model.
     */
    public FraudAnalysisResult fromCommand(com.gogidix.aiservices.aifrauddetectionservice.application.command.CreateFraudAnalysisCommand command) {
        if (command == null) {
            return null;
        }

        return FraudAnalysisResult.builder()
                .transactionId(command.getTransactionId())
                .build();
    }
}
