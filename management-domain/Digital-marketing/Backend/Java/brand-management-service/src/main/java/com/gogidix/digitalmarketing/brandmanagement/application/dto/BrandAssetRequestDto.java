package com.gogidix.digitalmarketing.brandmanagement.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandAssetRequestDto {
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

}