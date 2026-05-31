package com.gogidix.aiservices.aiuserprofilingservice.application.mapper;

import com.gogidix.aiservices.aiuserprofilingservice.application.dto.UserProfileResponseDto;
import com.gogidix.aiservices.aiuserprofilingservice.domain.model.UserProfile;
import com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileCriteria;
import com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper for UserProfile entity and DTOs.
 */
@Component
public class UserProfileMapper {

    /**
     * Convert domain entity to response DTO.
     */
    public UserProfileResponseDto toResponseDto(UserProfile segment) {
        return new UserProfileResponseDto(
                segment.getId(),
                segment.getName(),
                segment.getDescription(),
                segment.getProfileType(),
                criteriaToMap(segment.getCriteria()),
                segment.getUserIds() != null ? java.util.Set.copyOf(segment.getUserIds()) : java.util.Set.of(),
                (int) (long) segment.getUserCount(),
                segment.isActive(),
                segment.getTenantId(),
                segment.getCreatedAt(),
                segment.getUpdatedAt(),
                0L // version field - not in domain model yet
        );
    }

    /**
     * Convert ProfileCriteria to Map for DTO.
     */
    private Map<String, Object> criteriaToMap(ProfileCriteria criteria) {
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
