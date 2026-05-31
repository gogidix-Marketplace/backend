package com.gogidix.aiservices.aifeaturestoreservice.application.mapper;

import com.gogidix.aiservices.aifeaturestoreservice.application.dto.FeatureDefinitionResponseDto;
import com.gogidix.aiservices.aifeaturestoreservice.domain.model.FeatureDefinition;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between domain models and DTOs.
 */
@Component
public class FeatureDefinitionMapper {

    public FeatureDefinitionResponseDto toResponseDto(FeatureDefinition definition) {
        return new FeatureDefinitionResponseDto(
                definition.getFeatureName(),
                definition.getFeatureType(),
                definition.getDescription(),
                definition.getVersion(),
                definition.getMetadata(),
                definition.getCreatedAt(),
                definition.getUpdatedAt()
        );
    }
}
