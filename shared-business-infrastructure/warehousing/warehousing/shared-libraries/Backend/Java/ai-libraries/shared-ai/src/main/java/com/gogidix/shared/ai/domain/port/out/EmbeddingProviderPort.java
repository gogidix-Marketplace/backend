package com.gogidix.shared.ai.domain.port.out;

import com.gogidix.shared.ai.domain.model.EmbeddingRequest;
import com.gogidix.shared.ai.domain.model.EmbeddingResponse;

public interface EmbeddingProviderPort {

    EmbeddingResponse embed(EmbeddingRequest request);

    String getProviderName();
}
