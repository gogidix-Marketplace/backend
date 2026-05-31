package com.gogidix.shared.warehousing.tenant.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Business Rules DTO
 *
 * Request/Response object for business rules
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessRulesDto {

    private Map<String, Object> businessRules;
}
