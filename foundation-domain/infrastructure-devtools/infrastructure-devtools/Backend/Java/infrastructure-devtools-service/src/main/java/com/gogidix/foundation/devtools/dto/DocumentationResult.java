package com.gogidix.foundation.devtools.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for documentation generation results.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentationResult {

    private UUID projectUuid;
    private String projectName;
    private String status;
    private List<String> formats;
    private String outputPath;
    private Map<String, String> generatedFiles;
    private Integer pageCount;
    private Long generationTime;
    private String errorMessage;
}
