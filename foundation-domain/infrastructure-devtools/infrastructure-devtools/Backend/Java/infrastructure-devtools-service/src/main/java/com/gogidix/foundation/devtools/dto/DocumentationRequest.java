package com.gogidix.foundation.devtools.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO for documentation generation requests.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentationRequest {

    private String generatedBy;
    private List<String> formats;
    private Map<String, Object> options;
    private Boolean includePrivate;
    private Boolean includeInternal;
}
