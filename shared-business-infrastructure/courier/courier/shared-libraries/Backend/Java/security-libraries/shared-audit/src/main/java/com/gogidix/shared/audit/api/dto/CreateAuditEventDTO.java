package com.gogidix.shared.audit.api.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Map;

/**
 * DTO for creating audit events via REST API.
 * Contains validation rules and constraints for audit event creation.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAuditEventDTO {
    
    @NotBlank(message = "User ID is required")
    @Size(max = 255, message = "User ID must not exceed 255 characters")
    private String userId;
    
    @NotBlank(message = "Session ID is required")
    @Size(max = 255, message = "Session ID must not exceed 255 characters")
    private String sessionId;
    
    @NotBlank(message = "Action is required")
    @Size(max = 500, message = "Action must not exceed 500 characters")
    private String action;
    
    @Size(max = 500, message = "Resource type must not exceed 500 characters")
    private String resourceType;
    
    @Size(max = 255, message = "Resource ID must not exceed 255 characters")
    private String resourceId;
    
    @Size(max = 45, message = "IP address must not exceed 45 characters")
    private String ipAddress;
    
    @Size(max = 1000, message = "User agent must not exceed 1000 characters")
    private String userAgent;
    
    private Map<String, String> additionalData;
    
    /**
     * Validates if this DTO meets business rules for audit event creation
     */
    public boolean isValidForBusinessRules() {
        // Check required fields are present
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }
        if (sessionId == null || sessionId.trim().isEmpty()) {
            return false;
        }
        if (action == null || action.trim().isEmpty()) {
            return false;
        }
        
        // All basic business rules satisfied
        return true;
    }
    
}