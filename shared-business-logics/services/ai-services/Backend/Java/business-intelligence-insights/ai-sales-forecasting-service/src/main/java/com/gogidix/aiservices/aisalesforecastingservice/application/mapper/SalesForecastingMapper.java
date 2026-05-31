package com.gogidix.aiservices.aisalesforecastingservice.application.mapper;

import com.gogidix.aiservices.aisalesforecastingservice.application.dto.SalesForecastingResponseDto;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.SalesForecast;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastCriteria;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper for SalesForecast entity and DTOs.
 */
@Component
public class SalesForecastingMapper {

    /**
     * Convert domain entity to response DTO.
     */
    public SalesForecastingResponseDto toResponseDto(SalesForecast segment) {
        return new SalesForecastingResponseDto(
                segment.getId(),
                segment.getName(),
                segment.getDescription(),
                segment.getForecastType(),
                criteriaToMap(segment.getCriteria()),
                segment.getForecastModelIds() != null ? java.util.Set.copyOf(segment.getForecastModelIds()) : java.util.Set.of(),
                (int) (long) segment.getForecastModelCount(),
                segment.isActive(),
                segment.getTenantId(),
                segment.getCreatedAt(),
                segment.getUpdatedAt(),
                0L // version field - not in domain model yet
        );
    }

    /**
     * Convert ForecastCriteria to Map for DTO.
     */
    private Map<String, Object> criteriaToMap(ForecastCriteria criteria) {
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
