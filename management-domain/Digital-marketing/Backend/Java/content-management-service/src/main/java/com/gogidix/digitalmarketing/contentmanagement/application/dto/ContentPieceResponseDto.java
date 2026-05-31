package com.gogidix.digitalmarketing.contentmanagement.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentPieceResponseDto {
    private String id;
    private String tenantId;
    private String title;
     private String contentType;
     private String author;
     private String status;
     private String approvalStatus;
     private String isFeatured;

    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;
}