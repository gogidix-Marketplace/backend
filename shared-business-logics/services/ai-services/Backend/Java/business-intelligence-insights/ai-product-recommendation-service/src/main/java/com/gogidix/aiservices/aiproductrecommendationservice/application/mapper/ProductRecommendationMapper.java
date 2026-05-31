package com.gogidix.aiservices.aiproductrecommendationservice.application.mapper;

import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.ProductRecommendationResponseDto;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.ProductRecommendation;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationCriteria;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper for ProductRecommendation entity and DTOs.
 */
@Component
public class ProductRecommendationMapper {

    /**
     * Convert domain entity to response DTO.
     */
    public ProductRecommendationResponseDto toResponseDto(ProductRecommendation segment) {
        return new ProductRecommendationResponseDto(
                segment.getId(),
                segment.getName(),
                segment.getDescription(),
                segment.getRecommendationType(),
                criteriaToMap(segment.getCriteria()),
                segment.getProductIds() != null ? java.util.Set.copyOf(segment.getProductIds()) : java.util.Set.of(),
                (int) (long) segment.getProductCount(),
                segment.isActive(),
                segment.getTenantId(),
                segment.getCreatedAt(),
                segment.getUpdatedAt(),
                0L // version field - not in domain model yet
        );
    }

    /**
     * Convert RecommendationCriteria to Map for DTO.
     */
    private Map<String, Object> criteriaToMap(RecommendationCriteria criteria) {
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
