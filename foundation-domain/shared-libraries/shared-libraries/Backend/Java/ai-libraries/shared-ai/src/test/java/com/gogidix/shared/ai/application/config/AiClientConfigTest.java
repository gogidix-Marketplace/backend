package com.gogidix.shared.ai.application.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AiClientConfigTest {

    @Test
    void defaultValues() {
        AiClientConfig config = new AiClientConfig();
        assertNull(config.getOpenaiApiKey());
        assertNull(config.getAnthropicApiKey());
        assertEquals("gpt-4", config.getDefaultModel());
        assertEquals(0.7, config.getDefaultTemperature());
        assertEquals(4096, config.getDefaultMaxTokens());
        assertEquals(1536, config.getEmbeddingDimensions());
        assertEquals(60, config.getRequestTimeoutSeconds());
    }

    @Test
    void settersWork() {
        AiClientConfig config = new AiClientConfig();
        config.setOpenaiApiKey("sk-test");
        config.setAnthropicApiKey("sk-ant-test");
        config.setDefaultModel("claude-3-opus");
        config.setDefaultTemperature(0.3);
        config.setDefaultMaxTokens(2048);
        config.setEmbeddingDimensions(3072);
        config.setRequestTimeoutSeconds(120);

        assertEquals("sk-test", config.getOpenaiApiKey());
        assertEquals("sk-ant-test", config.getAnthropicApiKey());
        assertEquals("claude-3-opus", config.getDefaultModel());
        assertEquals(0.3, config.getDefaultTemperature());
        assertEquals(2048, config.getDefaultMaxTokens());
        assertEquals(3072, config.getEmbeddingDimensions());
        assertEquals(120, config.getRequestTimeoutSeconds());
    }
}
