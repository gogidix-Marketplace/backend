package com.gogidix.shared.ai.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmbeddingRequestTest {

    @Test
    void builder_defaults() {
        EmbeddingRequest req = EmbeddingRequest.builder()
                .texts(java.util.List.of("hello"))
                .build();
        assertEquals("text-embedding-3-small", req.getModel());
        assertEquals(1536, req.getDimensions());
    }
}
