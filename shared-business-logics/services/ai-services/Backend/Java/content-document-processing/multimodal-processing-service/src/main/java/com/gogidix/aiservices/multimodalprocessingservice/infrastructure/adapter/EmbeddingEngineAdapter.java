package com.gogidix.aiservices.multimodalprocessingservice.infrastructure.adapter;

import com.gogidix.aiservices.multimodalprocessingservice.domain.model.ContentItem;
import com.gogidix.aiservices.multimodalprocessingservice.domain.model.ContentModality;
import com.gogidix.aiservices.multimodalprocessingservice.domain.port.out.EmbeddingEnginePort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * In-memory implementation of embedding generation for testing.
 * In production, this would integrate with actual ML models.
 */
@Component
public class EmbeddingEngineAdapter implements EmbeddingEnginePort {

    private static final int EMBEDDING_DIMENSION = 768;
    private final Random random = new Random();

    @Override
    public float[] generateEmbedding(ContentItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Content item cannot be null");
        }

        if (item.modality() == ContentModality.UNKNOWN) {
            throw new IllegalArgumentException("Unsupported modality: " + item.modality());
        }

        // Generate normalized embedding vector
        float[] embedding = new float[EMBEDDING_DIMENSION];

        // Generate pseudo-random embeddings based on URL hash for consistency
        int seed = item.url().hashCode();
        Random seededRandom = new Random(seed);

        for (int i = 0; i < EMBEDDING_DIMENSION; i++) {
            // Generate values roughly in [-1, 1] range
            embedding[i] = (seededRandom.nextFloat() * 2) - 1;
        }

        return embedding;
    }

    @Override
    public float[][] generateBatchEmbeddings(List<ContentItem> items) {
        if (items == null || items.isEmpty()) {
            return new float[0][];
        }

        float[][] embeddings = new float[items.size()][EMBEDDING_DIMENSION];

        for (int i = 0; i < items.size(); i++) {
            embeddings[i] = generateEmbedding(items.get(i));
        }

        return embeddings;
    }

    @Override
    public boolean isHealthy() {
        return true;
    }

    @Override
    public int getEmbeddingDimension() {
        return EMBEDDING_DIMENSION;
    }
}
