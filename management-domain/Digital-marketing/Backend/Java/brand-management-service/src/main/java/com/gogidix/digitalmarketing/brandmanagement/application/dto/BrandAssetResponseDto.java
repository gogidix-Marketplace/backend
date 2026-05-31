package com.gogidix.digitalmarketing.brandmanagement.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandAssetResponseDto {
    private String id;
    private String tenantId;
    private String name;
     private String type;
     private String url;
     private String category;
     private String description;
     private String status;
     private String version;
     private String fileFormat;
     private String fileSize;
     private String storageLocation;
     private String isActive;
     private String isPublic;

    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;
}