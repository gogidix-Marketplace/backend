package com.gogidix.aiservices.aimanagementservice.application.mapper;

import com.gogidix.aiservices.aimanagementservice.application.dto.ModelResponseDto;
import com.gogidix.aiservices.aimanagementservice.domain.model.MLModel;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between domain models and DTOs.
 */
@Component
public class ModelMapper {

    public ModelResponseDto toResponseDto(MLModel model) {
        return new ModelResponseDto(
                model.getId(),
                model.getModelName(),
                model.getFramework(),
                model.getVersion(),
                model.getModelArtifactUrl(),
                model.getStatus(),
                model.getModelSizeBytes(),
                model.getRegisteredAt(),
                model.getDeployedAt()
        );
    }
}
