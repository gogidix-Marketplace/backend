package com.gogidix.shared.ai.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class EmbeddingResponse {

    String model;
    List<float[]> embeddings;
    TokenUsage tokenUsage;
}
