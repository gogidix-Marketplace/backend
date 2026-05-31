package com.gogidix.corporate.website.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SitemapEntry {
    private String url;
    private LocalDateTime lastModified;
    private String changeFrequency;
    private Double priority;
}
