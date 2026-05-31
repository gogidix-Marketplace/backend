package com.gogidix.aiservices.aidatavalidation.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class ValidationResponse {
    private String validationId;
    private boolean isValid;
    private List<String> errors;
    private List<String> warnings;
    private Map<String, Object> statistics;
    private Instant validatedAt;
    private String status;
}
