package com.gogidix.aiservices.aicontentgenerationservice.application.dto.response;

import lombok.Builder;

@Builder
public class OptimizationResponse {

    private String originalContent;
    private String optimizedContent;
    private String improvements;
    private Integer originalWordCount;
    private Integer optimizedWordCount;

    public String getOriginalContent() {
        return originalContent;
    }

    public String getOptimizedContent() {
        return optimizedContent;
    }

    public String getImprovements() {
        return improvements;
    }

    public Integer getOriginalWordCount() {
        return originalWordCount;
    }

    public Integer getOptimizedWordCount() {
        return optimizedWordCount;
    }
}
