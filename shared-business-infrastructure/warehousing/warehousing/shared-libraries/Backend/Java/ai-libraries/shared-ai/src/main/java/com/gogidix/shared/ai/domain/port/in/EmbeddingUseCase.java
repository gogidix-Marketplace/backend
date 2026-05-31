package com.gogidix.shared.ai.domain.port.in;

import com.gogidix.shared.ai.domain.model.EmbeddingRequest;
import com.gogidix.shared.ai.domain.model.EmbeddingResponse;

public interface EmbeddingUseCase {

    EmbeddingResponse embed(EmbeddingRequest request);

    float[] embedSingle(String text);

    double cosineSimilarity(float[] a, float[] b);
}
