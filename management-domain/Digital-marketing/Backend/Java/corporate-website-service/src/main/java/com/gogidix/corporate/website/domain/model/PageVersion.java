package com.gogidix.corporate.website.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageVersion {
    private String version;
    private LocalDateTime createdAt;
    private String createdBy;
    private String comment;
}
