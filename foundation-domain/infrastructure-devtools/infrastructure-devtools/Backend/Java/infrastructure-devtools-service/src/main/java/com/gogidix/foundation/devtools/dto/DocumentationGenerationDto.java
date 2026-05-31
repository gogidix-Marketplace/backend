package com.gogidix.foundation.devtools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentationGenerationDto {

    private Long id;
    private UUID uuid;
    private Long projectId;
    private String status;
    private String outputPath;
    private String outputContent;
    private Integer pageCount;
    private String errorMessage;
    private Long generationTime;
    private String generatedBy;
    private LocalDateTime createdAt;
}
