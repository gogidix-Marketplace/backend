package com.gogidix.shared.ai.application.service;

import com.gogidix.shared.ai.domain.model.EmbeddingRequest;
import com.gogidix.shared.ai.domain.model.EmbeddingResponse;
import com.gogidix.shared.ai.domain.model.TokenUsage;
import com.gogidix.shared.ai.domain.port.out.EmbeddingProviderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmbeddingServiceTest {

    private EmbeddingService service;
    private EmbeddingProviderPort mockProvider;

    @BeforeEach
    void setUp() {
        mockProvider = mock(EmbeddingProviderPort.class);
        service = new EmbeddingService(List.of(mockProvider));
    }

    @Test
    void embed_delegatesToProvider() {
        float[] vec = {0.1f, 0.2f, 0.3f};
        EmbeddingResponse response = EmbeddingResponse.builder()
                .model("text-embedding-3-small")
                .embeddings(List.of(vec))
                .tokenUsage(TokenUsage.of(5, 0))
                .build();
        when(mockProvider.embed(any())).thenReturn(response);

        EmbeddingRequest request = EmbeddingRequest.builder().texts(List.of("hello")).build();
        EmbeddingResponse result = service.embed(request);

        assertEquals("text-embedding-3-small", result.getModel());
        assertEquals(1, result.getEmbeddings().size());
        assertArrayEquals(vec, result.getEmbeddings().get(0));
    }

    @Test
    void embedSingle_returnsSingleVector() {
        float[] vec = {0.5f, 0.6f};
        EmbeddingResponse response = EmbeddingResponse.builder()
                .embeddings(List.of(vec))
                .tokenUsage(TokenUsage.of(3, 0))
                .build();
        when(mockProvider.embed(any())).thenReturn(response);

        float[] result = service.embedSingle("test text");
        assertArrayEquals(vec, result);
    }

    @Test
    void cosineSimilarity_identicalVectors_returnsOne() {
        float[] v = {1.0f, 0.0f, 0.0f};
        double sim = service.cosineSimilarity(v, v);
        assertEquals(1.0, sim, 0.001);
    }

    @Test
    void cosineSimilarity_orthogonalVectors_returnsZero() {
        float[] a = {1.0f, 0.0f};
        float[] b = {0.0f, 1.0f};
        double sim = service.cosineSimilarity(a, b);
        assertEquals(0.0, sim, 0.001);
    }

    @Test
    void cosineSimilarity_oppositeVectors_returnsMinusOne() {
        float[] a = {1.0f, 0.0f};
        float[] b = {-1.0f, 0.0f};
        double sim = service.cosineSimilarity(a, b);
        assertEquals(-1.0, sim, 0.001);
    }

    @Test
    void cosineSimilarity_differentDimensions_throws() {
        float[] a = {1.0f, 2.0f};
        float[] b = {1.0f};
        assertThrows(IllegalArgumentException.class, () -> service.cosineSimilarity(a, b));
    }

    @Test
    void cosineSimilarity_zeroVector_returnsZero() {
        float[] a = {0.0f, 0.0f};
        float[] b = {1.0f, 1.0f};
        assertEquals(0.0, service.cosineSimilarity(a, b));
    }
}
