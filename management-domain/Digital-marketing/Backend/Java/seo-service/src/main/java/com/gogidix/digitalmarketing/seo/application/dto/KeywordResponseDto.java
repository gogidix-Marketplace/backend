package com.gogidix.digitalmarketing.seo.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeywordResponseDto {
    private String id;
    private String tenantId;
    private String keyword;
     private String domain;
     private String volume;
     private String difficulty;
     private String ranking;
     private String previousRanking;

    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;
}