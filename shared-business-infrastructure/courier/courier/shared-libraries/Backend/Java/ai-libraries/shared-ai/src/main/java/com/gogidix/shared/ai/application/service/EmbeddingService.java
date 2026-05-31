package com.gogidix.shared.ai.application.service;

import com.gogidix.shared.ai.domain.model.EmbeddingRequest;
import com.gogidix.shared.ai.domain.model.EmbeddingResponse;
import com.gogidix.shared.ai.domain.port.in.EmbeddingUseCase;
import com.gogidix.shared.ai.domain.port.out.EmbeddingProviderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbeddingService implements EmbeddingUseCase {

    private final List<EmbeddingProviderPort> providers;

    @Override
    public EmbeddingResponse embed(EmbeddingRequest request) {
        EmbeddingProviderPort provider = providers.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No embedding provider configured"));
        return provider.embed(request);
    }

    @Override
    public float[] embedSingle(String text) {
        EmbeddingRequest request = EmbeddingRequest.builder()
                .texts(List.of(text))
                .build();
        EmbeddingResponse response = embed(request);
        return response.getEmbeddings().get(0);
    }

    @Override
    public double cosineSimilarity(float[] a, float[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < a.length; i++) {
            dotProduct += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        if (normA == 0.0 || normB == 0.0) {
            return 0.0;
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
