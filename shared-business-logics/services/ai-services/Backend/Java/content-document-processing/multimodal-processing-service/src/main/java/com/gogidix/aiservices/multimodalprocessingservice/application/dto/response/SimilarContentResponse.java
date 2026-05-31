package com.gogidix.aiservices.multimodalprocessingservice.application.dto.response;

import java.util.List;

public record SimilarContentResponse(
        List<SimilarItem> similarItems
) {
    public record SimilarItem(
            String contentId,
            double similarity
    ) {}
}
