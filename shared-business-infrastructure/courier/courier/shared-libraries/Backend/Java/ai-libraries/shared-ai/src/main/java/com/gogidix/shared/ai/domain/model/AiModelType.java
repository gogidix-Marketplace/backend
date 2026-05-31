package com.gogidix.shared.ai.domain.model;

public enum AiModelType {
    GPT_4("gpt-4", "openai"),
    GPT_4_TURBO("gpt-4-turbo", "openai"),
    GPT_35_TURBO("gpt-3.5-turbo", "openai"),
    CLAUDE_3_OPUS("claude-3-opus", "anthropic"),
    CLAU_3_SONNET("claude-3-sonnet", "anthropic"),
    CLAUDE_3_HAIKU("claude-3-haiku", "anthropic"),
    CUSTOM("custom", "custom");

    private final String modelId;
    private final String provider;

    AiModelType(String modelId, String provider) {
        this.modelId = modelId;
        this.provider = provider;
    }

    public String getModelId() { return modelId; }
    public String getProvider() { return provider; }
}
