package com.gogidix.aiservices.multimodalprocessingservice.domain.port.out;

import com.gogidix.aiservices.multimodalprocessingservice.domain.model.ContentItem;

import java.util.List;

public interface EmbeddingEnginePort {
    float[] generateEmbedding(ContentItem item);
    float[][] generateBatchEmbeddings(List<ContentItem> items);
    boolean isHealthy();
    int getEmbeddingDimension();
}
