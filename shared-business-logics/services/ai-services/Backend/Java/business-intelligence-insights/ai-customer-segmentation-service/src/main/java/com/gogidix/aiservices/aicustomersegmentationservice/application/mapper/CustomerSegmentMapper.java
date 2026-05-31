package com.gogidix.aiservices.aicustomersegmentationservice.application.mapper;

import com.gogidix.aiservices.aicustomersegmentationservice.application.dto.CustomerSegmentResponseDto;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.CustomerSegment;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentCriteria;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper for CustomerSegment entity and DTOs.
 */
@Component
public class CustomerSegmentMapper {

    /**
     * Convert domain entity to response DTO.
     */
    public CustomerSegmentResponseDto toResponseDto(CustomerSegment segment) {
        return new CustomerSegmentResponseDto(
                segment.getId(),
                segment.getName(),
                segment.getDescription(),
                segment.getSegmentType(),
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
     * Convert SegmentCriteria to Map for DTO.
     */
    private Map<String, Object> criteriaToMap(SegmentCriteria criteria) {
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
