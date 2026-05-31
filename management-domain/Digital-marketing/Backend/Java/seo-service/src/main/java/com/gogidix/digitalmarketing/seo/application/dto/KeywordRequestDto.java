package com.gogidix.digitalmarketing.seo.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeywordRequestDto {
    private String tenantId;
    private String keyword;
     private String domain;
     private String volume;
     private String difficulty;
     private String ranking;
     private String previousRanking;

}