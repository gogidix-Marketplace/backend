package com.gogidix.aiservices.multimodalprocessingservice.application.dto.response;

import java.util.List;
import java.util.Map;

public record MultimodalProcessingResponse(
        String processingId,
        Map<String, List<Double>> embeddings,
        List<Double> fusedEmbedding,
        String summary,
        List<String> tags
) {}
