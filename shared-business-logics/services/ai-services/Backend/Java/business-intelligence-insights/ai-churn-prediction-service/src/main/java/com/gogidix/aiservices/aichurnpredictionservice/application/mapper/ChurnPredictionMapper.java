package com.gogidix.aiservices.aichurnpredictionservice.application.mapper;

import com.gogidix.aiservices.aichurnpredictionservice.application.dto.ChurnPredictionResponseDto;
import com.gogidix.aiservices.aichurnpredictionservice.domain.model.ChurnPrediction;
import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionCriteria;
import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper for ChurnPrediction entity and DTOs.
 */
@Component
public class ChurnPredictionMapper {

    /**
     * Convert domain entity to response DTO.
     */
    public ChurnPredictionResponseDto toResponseDto(ChurnPrediction segment) {
        return new ChurnPredictionResponseDto(
                segment.getId(),
                segment.getName(),
                segment.getDescription(),
                segment.getPredictionType(),
                criteriaToMap(segment.getCriteria()),
                segment.getCustomerIds() != null ? java.util.Set.copyOf(segment.getCustomerIds()) : java.util.Set.of(),
                (int) (long) segment.getCustomerCount(),
                segment.isActive(),
                segment.getTenantId(),
                segment.getCreatedAt(),
                segment.getUpdatedAt(),
                0L // version field - not in domain model yet
        );
    }

    /**
     * Convert PredictionCriteria to Map for DTO.
     */
    private Map<String, Object> criteriaToMap(PredictionCriteria criteria) {
        if (criteria == null) {
            return new HashMap<>();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("type", criteria.getType().name());
        map.put("operator", criteria.getOperator().name());
        map.put("field", criteria.getField());
        map.put("value", criteria.getValue());
        map.put("logicalOperator", criteria.getLogicalOperator().name());
        return map;
    }
}
