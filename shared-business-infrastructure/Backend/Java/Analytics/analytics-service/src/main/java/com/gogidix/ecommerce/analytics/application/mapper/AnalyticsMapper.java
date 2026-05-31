package com.gogidix.ecommerce.analytics.application.mapper;

import com.gogidix.ecommerce.analytics.application.dto.*;
import com.gogidix.ecommerce.analytics.domain.model.Analytics;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsMapper {

    public AnalyticsResponse toResponse(Analytics entity) {
        if (entity == null) return null;
        return new AnalyticsResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getEventType(),
            entity.getMetricName(),
            entity.getMetricValue(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public Analytics toEntity(CreateAnalyticsRequest request) {
        Analytics entity = new Analytics();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setEventType(request.eventType());
        entity.setMetricName(request.metricName());
        entity.setMetricValue(request.metricValue());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(Analytics entity, UpdateAnalyticsRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.eventType() != null) entity.setEventType(request.eventType());
        if (request.metricName() != null) entity.setMetricName(request.metricName());
        if (request.metricValue() != null) entity.setMetricValue(request.metricValue());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
