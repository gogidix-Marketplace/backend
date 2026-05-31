package com.gogidix.shared.ai.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AiModelTypeTest {

    @Test
    void allModelsHaveProvider() {
        for (AiModelType type : AiModelType.values()) {
            assertNotNull(type.getModelId());
            assertNotNull(type.getProvider());
        }
    }

    @Test
    void gpt4_hasCorrectProvider() {
        assertEquals("openai", AiModelType.GPT_4.getProvider());
        assertEquals("gpt-4", AiModelType.GPT_4.getModelId());
    }

    @Test
    void claude3Opus_hasCorrectProvider() {
        assertEquals("anthropic", AiModelType.CLAUDE_3_OPUS.getProvider());
        assertEquals("claude-3-opus", AiModelType.CLAUDE_3_OPUS.getModelId());
    }
}
