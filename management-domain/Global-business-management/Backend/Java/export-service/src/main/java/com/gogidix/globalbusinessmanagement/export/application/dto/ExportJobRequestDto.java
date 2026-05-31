package com.gogidix.globalbusinessmanagement.export.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportJobRequestDto {
    private String tenantId;
    private String name;
    private String exportType;
    private String format;
    private String status;
    private String filePath;
    private String recordCount;
}
