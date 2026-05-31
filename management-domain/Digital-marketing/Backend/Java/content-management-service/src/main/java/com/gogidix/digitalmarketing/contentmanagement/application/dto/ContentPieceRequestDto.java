package com.gogidix.digitalmarketing.contentmanagement.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentPieceRequestDto {
    private String tenantId;
    private String title;
     private String contentType;
     private String author;
     private String status;
     private String approvalStatus;
     private String isFeatured;

}