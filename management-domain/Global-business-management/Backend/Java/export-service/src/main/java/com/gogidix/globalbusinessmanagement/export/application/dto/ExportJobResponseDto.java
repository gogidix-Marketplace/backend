package com.gogidix.globalbusinessmanagement.export.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportJobResponseDto {
    private String id;
    private String tenantId;
    private String name;
    private String exportType;
    private String format;
    private String status;
    private String filePath;
    private String recordCount;
    private Instant createdAt;
    private Instant updatedAt;
}
