package com.gogidix.aiservices.aifeaturestoreservice.application.dto;

/**
 * Response DTO for entity feature.
 */
public record EntityFeatureDto(
        String entityId,
        Object value
) {
}
