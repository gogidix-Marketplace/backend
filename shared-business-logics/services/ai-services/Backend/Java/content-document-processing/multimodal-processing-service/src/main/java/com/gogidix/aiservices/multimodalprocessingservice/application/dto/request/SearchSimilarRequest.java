package com.gogidix.aiservices.multimodalprocessingservice.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SearchSimilarRequest(
        @NotNull(message = "Query embedding is required")
        double[] queryEmbedding,
        double threshold,
        @Min(1)
        int limit
) {
    public SearchSimilarRequest {
        if (threshold == 0.0) {
            threshold = 0.7;
        }
        if (limit == 0) {
            limit = 10;
        }
    }

    public SearchSimilarRequest(double[] queryEmbedding) {
        this(queryEmbedding, 0.7, 10);
    }

    public SearchSimilarRequest(double[] queryEmbedding, double threshold) {
        this(queryEmbedding, threshold, 10);
    }
}
