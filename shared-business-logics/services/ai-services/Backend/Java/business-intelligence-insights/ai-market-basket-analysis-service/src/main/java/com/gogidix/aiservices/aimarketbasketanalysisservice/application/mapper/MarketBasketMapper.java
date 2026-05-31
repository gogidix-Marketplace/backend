package com.gogidix.aiservices.aimarketbasketanalysisservice.application.mapper;

import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.MarketBasketResponseDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.MarketBasket;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketCriteria;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper for MarketBasket entity and DTOs.
 */
@Component
public class MarketBasketMapper {

    /**
     * Convert domain entity to response DTO.
     */
    public MarketBasketResponseDto toResponseDto(MarketBasket segment) {
        return new MarketBasketResponseDto(
                segment.getId(),
                segment.getName(),
                segment.getDescription(),
                segment.getBasketType(),
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
     * Convert BasketCriteria to Map for DTO.
     */
    private Map<String, Object> criteriaToMap(BasketCriteria criteria) {
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
