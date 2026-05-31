package com.gogidix.aiservices.aidatavalidation.application.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ValidateDatasetRequest {
    private String dataSource;
    private Map<String, Object> schema;
    private List<String> validationRules;
    private Integer timeout;
}
